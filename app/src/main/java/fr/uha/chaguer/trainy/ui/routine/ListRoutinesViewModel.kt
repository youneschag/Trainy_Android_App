package fr.uha.chaguer.trainy.ui.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.repository.RoutineRepository
import fr.uha.chaguer.android.viewmodel.Result
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
        data class OnRename(val routineId: Long, val newName: String) : UIEvent()
        data class OnAdd(val routine: Routine) : UIEvent()
    }

    fun send(uiEvent: UIEvent) {
        viewModelScope.launch {
            if (uiState.value !is Result.Success) return@launch
            val routines = (uiState.value as Result.Success<UIState>).content.routines

            when (uiEvent) {
                is UIEvent.OnDelete -> {
                    repository.deleteRoutine(uiEvent.routine.routineId)
                }

                is UIEvent.OnDeleteAll -> {
                    repository.deleteAllRoutines()
                }

                is UIEvent.OnRename -> {
                    val routine = routines.find { it.routineId == uiEvent.routineId }
                    if (routine != null) {
                        repository.updateName(routine.routineId, uiEvent.newName)
                    }
                }

                is UIEvent.OnAdd -> {
                    repository.create(uiEvent.routine)
                }
            }
        }
    }
}