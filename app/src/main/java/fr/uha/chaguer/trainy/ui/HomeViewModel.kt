package fr.uha.chaguer.trainy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.repository.ExerciseRepository
import fr.uha.chaguer.trainy.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _allExercises = MutableStateFlow<List<Exercise>>(emptyList())
    val allExercises = _allExercises.asStateFlow()

    private val _allRoutines = MutableStateFlow<List<Routine>>(emptyList())
    val allRoutines = _allRoutines.asStateFlow()

    fun getAllExercises(): Flow<List<Exercise>> {
        return allExercises
    }

    fun getAllRoutines(): Flow<List<Routine>> {
        return allRoutines
    }

    init {
        fetchAllExercises()
        fetchAllRoutines()
    }

    private fun fetchAllExercises() {
        viewModelScope.launch {
            exerciseRepository.getAllExercises()
                .stateIn(viewModelScope)
                .collect { exercises ->
                    _allExercises.value = exercises
                }
        }
    }

    private fun fetchAllRoutines() {
        viewModelScope.launch {
            routineRepository.getAll()
                .stateIn(viewModelScope)
                .collect { routines ->
                    _allRoutines.value = routines
                }
        }
    }
}