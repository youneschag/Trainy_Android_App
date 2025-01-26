package fr.uha.chaguer.trainy.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import fr.uha.chaguer.android.viewmodel.Result
import javax.inject.Inject

@HiltViewModel
class ListExercisesViewModel @Inject constructor(
    private val repository: ExerciseRepository
) : ViewModel() {

    private val _exercises: Flow<List<Exercise>> = repository.getAllExercises()

    data class UIState(
        val exercises: List<Exercise>
    )

    val uiState: StateFlow<Result<UIState>> = _exercises
        .map { list: List<Exercise> ->
            Result.Success(UIState(list))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )

    sealed class UIEvent {
        data class OnDelete(val exercise: Exercise) : UIEvent()
        data class OnRename(val exerciseId: Long, val newName: String) : UIEvent()
        data class OnAdd(val exercise: Exercise) : UIEvent()
    }

    fun send(uiEvent: UIEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is UIEvent.OnDelete -> onDelete(uiEvent.exercise)
                is UIEvent.OnRename -> onRename(uiEvent.exerciseId, uiEvent.newName)
                is UIEvent.OnAdd -> onAdd(uiEvent.exercise)
            }
        }
    }

    private fun onDelete(exercise: Exercise) = viewModelScope.launch {
        repository.deleteExercise(exercise)
    }

    private suspend fun onRename(exerciseId: Long, newName: String) {
        repository.updateExerciseName(exerciseId, newName)
    }

    fun deleteAllExercises() = viewModelScope.launch {
        repository.deleteAllExercises()
    }

    private suspend fun onAdd(exercise: Exercise) {
        repository.addExercise(exercise)
    }
}