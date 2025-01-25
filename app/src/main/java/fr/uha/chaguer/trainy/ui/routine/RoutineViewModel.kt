package fr.uha.chaguer.trainy.ui.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.chaguer.trainy.model.FullRoutine
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.repository.RoutineRepository
import fr.uha.chaguer.android.ui.app.UITitleBuilder
import fr.uha.chaguer.android.ui.app.UITitleState
import fr.uha.chaguer.android.ui.field.FieldWrapper
import fr.uha.chaguer.android.viewmodel.Result
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
        val routine: FullRoutine,
        val name: FieldWrapper<String>,
        val startDay: FieldWrapper<Date>,
        val frequency: FieldWrapper<Int>,
        val objective: FieldWrapper<String>
    ) {
        companion object {
            fun create(routine: FullRoutine): UIState {
                val validator = RoutineUIValidator(routine)
                val name = FieldWrapper(routine.routine.name, validator.validateName(routine.routine.name))
                val startDay = FieldWrapper(routine.routine.startDay, validator.validateStartDay(routine.routine.startDay))
                val frequency = FieldWrapper(routine.routine.frequency, validator.validateFrequency(routine.routine.frequency))
                val objective = FieldWrapper(routine.routine.objective, validator.validateObjective(routine.routine.objective)) // Nouveau champ
                return UIState(routine, name, startDay, frequency, objective)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<Result<UIState>> = _id
        .flatMapLatest { id -> repository.getRoutineById(id) }
        .map { routine ->
            if (routine != null)
                Result.Success(UIState.create(routine))
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
        data class FrequencyChanged(val newValue: Int) : UIEvent()
        data class ObjectiveChanged(val newValue: String) : UIEvent()
        object DeleteRoutine : UIEvent()
    }
    fun send(uiEvent: UIEvent) {
        viewModelScope.launch {
            if (uiState.value !is Result.Success) return@launch
            val routineId = (uiState.value as Result.Success<UIState>).content.routine.routine.routineId

            when (uiEvent) {
                is UIEvent.NameChanged -> {
                    repository.updateName(routineId, uiEvent.newValue)
                }

                is UIEvent.StartDayChanged -> {
                    repository.updateStartDay(routineId, uiEvent.newValue)
                }

                is UIEvent.FrequencyChanged -> {
                    repository.updateFrequency(routineId, uiEvent.newValue)
                }

                is UIEvent.ObjectiveChanged -> {
                    repository.updateObjective(routineId, uiEvent.newValue)
                }

                is UIEvent.DeleteRoutine -> {
                    repository.deleteRoutine(routineId)
                    _id.value = 0 // Réinitialise l’état de la routine après suppression
                }
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

    fun save() {
        viewModelScope.launch {
            if (uiState.value !is Result.Success) return@launch
            val uiStateContent = (uiState.value as Result.Success<UIState>).content

            // Récupération de la routine actuelle
            val currentRoutine = uiStateContent.name.value?.let {
                uiStateContent.startDay.value?.let { it1 ->
                    uiStateContent.frequency.value?.let { it2 ->
                        uiStateContent.objective.value?.let { it3 ->
                            uiStateContent.routine.routine.copy(
                                name = it,
                                startDay = it1,
                                frequency = it2,
                                objective = it3,
                            )
                        }
                    }
                }
            }

            // Sauvegarde dans le référentiel
            if (currentRoutine != null) {
                repository.upsert(currentRoutine)
            }
        }
    }
}