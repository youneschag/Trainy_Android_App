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

    private val _nameState = MutableStateFlow(FieldWrapper<String>())
    private val _dayState = MutableStateFlow(FieldWrapper<Date>())
    private val _objectiveState = MutableStateFlow(FieldWrapper<String>())
    private val _frequencyState = MutableStateFlow(FieldWrapper<Int>())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialRoutineState: StateFlow<Result<Routine>> = _id
        .flatMapLatest { id -> repository.getRoutineById(id) }
        .map { routine ->
            if (routine != null) {
                _nameState.value = fieldBuilder.buildName(routine.name)
                _dayState.value = fieldBuilder.buildDay(routine.startDay)
                _objectiveState.value = fieldBuilder.buildObjective(routine.objective)
                _frequencyState.value = fieldBuilder.buildFrequency(routine.frequency)
                Result.Success(content = routine)
            } else {
                Result.Error()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Result.Loading)

    @Suppress("UNCHECKED_CAST")
    data class UIState(
        val nameState: FieldWrapper<String>,
        val dayState: FieldWrapper<Date>,
        val objectiveState: FieldWrapper<String>,
        val frequencyState: FieldWrapper<Int>
    )

    val uiState: StateFlow<Result<UIState>> = combine(
        _nameState, _dayState, _objectiveState, _frequencyState
    ) { name, day, objective, frequency ->
        Result.Success(
            UIState(
                nameState = name,
                dayState = day,
                objectiveState = objective,
                frequencyState = frequency
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.Loading
    )

    private class FieldBuilder(private val validator: RoutineUIValidator) {
        fun buildName(newValue: String): FieldWrapper<String> {
            val errorId: Int? = validator.validateName(newValue)
            return FieldWrapper(newValue, errorId)
        }

        fun buildDay(newValue: Date): FieldWrapper<Date> {
            val errorId: Int? = validator.validateStartDay(newValue)
            return FieldWrapper(newValue, errorId)
        }

        fun buildObjective(newValue: String): FieldWrapper<String> {
            val errorId: Int? = validator.validateObjective(newValue)
            return FieldWrapper(newValue, errorId)
        }

        fun buildFrequency(newValue: Int): FieldWrapper<Int> {
            val errorId: Int? = validator.validateFrequency(newValue)
            return FieldWrapper(newValue, errorId)
        }
    }

    private val fieldBuilder = FieldBuilder(RoutineUIValidator(uiState))

    val titleBuilder = UITitleBuilder()

    val uiTitleState: StateFlow<UITitleState> = titleBuilder.uiTitleState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UITitleState()
    )

    private fun isModified(initial: Result<Routine>, fields: Result<UIState>): Boolean? {
        if (initial !is Result.Success) return null
        if (fields !is Result.Success) return null
        if (fields.content.nameState.value != initial.content.name) return true
        if (fields.content.dayState.value != initial.content.startDay) return true
        if (fields.content.objectiveState.value != initial.content.objective) return true
        if (fields.content.frequencyState.value != initial.content.frequency) return true
        return false
    }

    private fun hasError(fields: Result<UIState>): Boolean? {
        if (fields !is Result.Success) return null
        val content = fields.content
        return listOf(
            content.nameState.errorId,
            content.dayState.errorId,
            content.objectiveState.errorId,
            content.frequencyState.errorId
        ).any { it != null }
    }

    sealed class UIEvent {
        data class NameChanged(val newValue: String) : UIEvent()
        data class StartDayChanged(val newValue: Date) : UIEvent()
        data class ObjectiveChanged(val newValue: String) : UIEvent()
        data class FrequencyChanged(val newValue: Int) : UIEvent()
        object SaveChanges : UIEvent() // Événement pour sauvegarder les modifications
    }

    fun send(uiEvent: UIEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is UIEvent.NameChanged -> _nameState.value = fieldBuilder.buildName(uiEvent.newValue)
                is UIEvent.StartDayChanged -> _dayState.value = fieldBuilder.buildDay(uiEvent.newValue)
                is UIEvent.ObjectiveChanged -> _objectiveState.value =
                    fieldBuilder.buildObjective(uiEvent.newValue)
                is UIEvent.FrequencyChanged -> _frequencyState.value =
                    fieldBuilder.buildFrequency(uiEvent.newValue)
                UIEvent.SaveChanges -> save()
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

    private fun save() = viewModelScope.launch {
        if (uiState.value !is Result.Success) return@launch
        val currentState = uiState.value as Result.Success
        val routine = Routine(
            routineId = _id.value,
            name = currentState.content.nameState.value!!,
            frequency = currentState.content.frequencyState.value!!,
            objective = currentState.content.objectiveState.value!!,
            startDay = currentState.content.dayState.value!!
        )
        repository.updateRoutine(routine)
    }

    init {
        initializeFields() // Initialise les champs au lancement
    }

    fun initializeFields() {
        _nameState.value = FieldWrapper("Nouvelle routine")
        _dayState.value = FieldWrapper(Date())
        _objectiveState.value = FieldWrapper("Améliorer la forme")
        _frequencyState.value = FieldWrapper(3)
    }

    fun getAllRoutines(): Flow<List<Routine>> {
        return repository.getAll()
    }

    fun updateRoutine(updatedRoutine: Routine) {
        viewModelScope.launch {
            repository.updateRoutine(updatedRoutine)
        }
    }
    fun getAllExercises(): Flow<List<Exercise>> {
        return repository.getAllExercises()
    }

    fun getRoutineWithExercises(routineId: Long): Flow<FullRoutine?> {
        return repository.getRoutineWithExercises(routineId)
    }

    fun addExerciseToRoutine(routineId: Long, exerciseId: Long) = viewModelScope.launch {
        repository.addExerciseToRoutine(routineId, exerciseId)
    }
}