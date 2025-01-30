package fr.uha.chaguer.trainy.ui.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.repository.RoutineRepository
import fr.uha.chaguer.android.viewmodel.Result
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.FullRoutine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListRoutinesViewModel @Inject constructor(
    private val repository: RoutineRepository
) : ViewModel() {

    private val _routines: Flow<List<Routine>> = repository.getAll()

    data class UIState(
        val routines: List<Routine>
    )

    val uiState: StateFlow<Result<UIState>> = _routines
        .map { list: List<Routine> ->
            Result.Success(UIState(list))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )

    sealed class UIEvent {
        data class OnDelete(val routine: Routine) : UIEvent()
        object OnDeleteAll : UIEvent()
    }

    fun send(uiEvent: UIEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is UIEvent.OnDelete -> {
                    repository.deleteRoutine(uiEvent.routine.routineId)
                }

                is UIEvent.OnDeleteAll -> {
                    repository.deleteAllRoutines()
                }
            }
        }
    }

    fun deleteAllRoutines() {
        viewModelScope.launch {
            repository.deleteAllRoutines()
        }
    }

    fun getAllFullRoutines(): Flow<List<FullRoutine>> {
        return repository.getAllFullRoutines()
    }

    fun removeExerciseFromRoutine(routineId: Long, exercise: Exercise) {
        viewModelScope.launch {
            repository.removeExercise(routineId, exercise)
        }
    }
}