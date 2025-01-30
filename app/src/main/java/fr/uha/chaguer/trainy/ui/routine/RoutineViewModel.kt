package fr.uha.chaguer.trainy.ui.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.repository.RoutineRepository
import fr.uha.chaguer.android.ui.app.UITitleBuilder
import fr.uha.chaguer.android.ui.app.UITitleState
import fr.uha.chaguer.android.ui.field.FieldWrapper
import fr.uha.chaguer.android.viewmodel.Result
import fr.uha.chaguer.trainy.database.RoutineUpdateDTO
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.FullRoutine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val repository: RoutineRepository
) : ViewModel() {

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    data class UIState(
        val nameState: FieldWrapper<String>,
        val dayState: FieldWrapper<Date>,
        val objectiveState: FieldWrapper<String>,
        val frequencyState: FieldWrapper<Int>,
        val exercises: FieldWrapper<List<Exercise>>,
        var routine: FullRoutine,
    ) {
        companion object {
            fun create(routine: FullRoutine): UIState {
                val validator = RoutineUIValidator(routine)
                val name = FieldWrapper(routine.routine.name, validator.validateName(routine.routine.name))
                val startDay =
                    FieldWrapper(routine.routine.startDay, validator.validateStartDay(routine.routine.startDay))
                val objective: FieldWrapper<String> =
                    FieldWrapper(routine.routine.objective, validator.validateObjective(routine.routine.objective))
                val frequency =
                    FieldWrapper(routine.routine.frequency, validator.validateFrequency(routine.routine.frequency))
                val exercises: FieldWrapper<List<Exercise>> =
                    FieldWrapper(routine.exercises, validator.validateExercises(routine.exercises))
                return UIState(name, startDay,objective, frequency, exercises, routine)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState : StateFlow<Result<UIState>> = _id
        .flatMapLatest { id -> repository.getRoutineById(id) }
        .map { routine ->
            if (routine != null) {
                Result.Success(UIState.create(routine))
            }
            else Result.Error()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )

    val titleBuilder = UITitleBuilder()

    val uiTitleState: StateFlow<UITitleState> = titleBuilder.uiTitleState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UITitleState()
    )

    sealed class UIEvent {
        data class NameChanged(val newValue: String) : UIEvent()
        data class StartDayChanged(val newValue: Date) : UIEvent()
        data class ObjectiveChanged(val newValue: String) : UIEvent()
        data class FrequencyChanged(val newValue: Int) : UIEvent()
        data class AddExercise(val newValue: Exercise) : UIEvent()
        data class RemoveExercise(val newValue: Exercise) : UIEvent()
    }

    fun send(uiEvent: UIEvent) {
        viewModelScope.launch {
            if (uiState.value !is Result.Success) return@launch
            val routineId = (uiState.value as Result.Success<UIState>).content.routine.routine.routineId
            when (uiEvent) {
                is UIEvent.NameChanged ->
                    repository.updateRoutine(RoutineUpdateDTO.Name(routineId, uiEvent.newValue))

                is UIEvent.StartDayChanged ->
                    repository.updateRoutine(RoutineUpdateDTO.StartDay(routineId, uiEvent.newValue))

                is UIEvent.ObjectiveChanged ->
                    repository.updateRoutine(RoutineUpdateDTO.Objectives(routineId, uiEvent.newValue))

                is UIEvent.FrequencyChanged ->
                    repository.updateRoutine(RoutineUpdateDTO.Frequency(routineId, uiEvent.newValue))

                is UIEvent.AddExercise ->
                    repository.addExerciseToRoutine(routineId, uiEvent.newValue)

                is UIEvent.RemoveExercise ->
                    repository.removeExercise(routineId, uiEvent.newValue)

                else -> {}
            }
        }
    }

    fun create(routine: Routine) = viewModelScope.launch {
        val rid: Long = repository.create(routine)
        _id.value = rid
    }

    fun edit(rid: Long) = viewModelScope.launch {
        _id.value = rid
    }

    fun addExerciseToRoutine(routineId: Long, exercise: Exercise) = viewModelScope.launch {
        repository.addExerciseToRoutine(routineId, exercise)
    }

    fun getAllRoutines(): Flow<List<Routine>> {
        return repository.getAll()
    }

    fun getRoutineWithExercises(routineId: Long): Flow<FullRoutine?> {
        return repository.getRoutineWithExercises(routineId)
    }
}