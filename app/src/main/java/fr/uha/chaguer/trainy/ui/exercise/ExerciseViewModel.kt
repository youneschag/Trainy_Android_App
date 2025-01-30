package fr.uha.chaguer.trainy.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.chaguer.android.ui.app.UITitleBuilder
import fr.uha.chaguer.android.ui.app.UITitleState
import fr.uha.chaguer.android.ui.field.FieldWrapper
import fr.uha.chaguer.android.viewmodel.Result
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.repository.ExerciseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val repository: ExerciseRepository
) : ViewModel() {

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    private val _nameState = MutableStateFlow(FieldWrapper<String>())
    private val _descriptionState = MutableStateFlow(FieldWrapper<String>())
    private val _durationState = MutableStateFlow(FieldWrapper<Int>())
    private val _repetitionsState = MutableStateFlow(FieldWrapper<Int>())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialExerciseState: StateFlow<Result<Exercise>> = _id
        .flatMapLatest { id -> repository.getExerciseById(id) }
        .map { exercise ->
            if (exercise != null) {
                _nameState.value = fieldBuilder.buildName(exercise.name)
                _descriptionState.value = fieldBuilder.buildDescription(exercise.description)
                _durationState.value = fieldBuilder.buildDuration(exercise.duration)
                _repetitionsState.value = fieldBuilder.buildRepetitions(exercise.repetitions)
                Result.Success(content = exercise)
            } else {
                Result.Error()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Result.Loading)

    @Suppress("UNCHECKED_CAST")
    data class UIState(
        val nameState: FieldWrapper<String>,
        val descriptionState: FieldWrapper<String>,
        val durationState: FieldWrapper<Int>,
        val repetitionsState: FieldWrapper<Int>
    )

    val uiState: StateFlow<Result<UIState>> = combine(
        _nameState, _descriptionState, _durationState, _repetitionsState
    ) { name, description, duration, repetitions ->
        Result.Success(
            UIState(
                nameState = name,
                descriptionState = description,
                durationState = duration,
                repetitionsState = repetitions
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.Loading
    )

    private class FieldBuilder(private val validator: ExerciseUIValidator) {
        fun buildName(newValue: String): FieldWrapper<String> {
            val errorId: Int? = validator.validateNameChange(newValue)
            return FieldWrapper(newValue, errorId)
        }

        fun buildDescription(newValue: String): FieldWrapper<String> {
            val errorId: Int? = validator.validateDescriptionChange(newValue)
            return FieldWrapper(newValue, errorId)
        }

        fun buildDuration(newValue: Int): FieldWrapper<Int> {
            val errorId: Int? = validator.validateDurationChange(newValue)
            return FieldWrapper(newValue, errorId)
        }

        fun buildRepetitions(newValue: Int): FieldWrapper<Int> {
            val errorId: Int? = validator.validateRepetitionsChange(newValue)
            return FieldWrapper(newValue, errorId)
        }
    }

    private val fieldBuilder = FieldBuilder(ExerciseUIValidator(uiState))

    val titleBuilder = UITitleBuilder()

    val uiTitleState: StateFlow<UITitleState> = titleBuilder.uiTitleState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UITitleState()
    )

    private fun isModified(initial: Result<Exercise>, fields: Result<UIState>): Boolean? {
        if (initial !is Result.Success) return null
        if (fields !is Result.Success) return null
        if (fields.content.nameState.value != initial.content.name) return true
        if (fields.content.descriptionState.value != initial.content.description) return true
        if (fields.content.durationState.value != initial.content.duration) return true
        if (fields.content.repetitionsState.value != initial.content.repetitions) return true
        return false
    }

    private fun hasError(fields: Result<UIState>): Boolean? {
        if (fields !is Result.Success) return null
        val content = fields.content
        return listOf(
            content.nameState.errorId,
            content.descriptionState.errorId,
            content.durationState.errorId,
            content.repetitionsState.errorId
        ).any { it != null }
    }

    init {
        combine(_initialExerciseState, uiState) { i, s ->
            titleBuilder.setModified(isModified(i, s))
            titleBuilder.setError(hasError(s))
        }.launchIn(viewModelScope)
    }

    sealed class UIEvent {
        data class NameChanged(val newValue: String) : UIEvent()
        data class DescriptionChanged(val newValue: String) : UIEvent()
        data class DurationChanged(val newValue: Int) : UIEvent()
        data class RepetitionsChanged(val newValue: Int) : UIEvent()
    }

    fun send(uiEvent: UIEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is UIEvent.NameChanged -> _nameState.value =
                    fieldBuilder.buildName(uiEvent.newValue)

                is UIEvent.DescriptionChanged -> _descriptionState.value =
                    fieldBuilder.buildDescription(uiEvent.newValue)

                is UIEvent.DurationChanged -> _durationState.value =
                    fieldBuilder.buildDuration(uiEvent.newValue)

                is UIEvent.RepetitionsChanged -> _repetitionsState.value =
                    fieldBuilder.buildRepetitions(uiEvent.newValue)

                else -> {}
            }
        }
    }

    fun edit(id: Long) = viewModelScope.launch {
        _id.value = id
    }

    fun create(exercise: Exercise) = viewModelScope.launch {
        val id: Long = repository.createExercise(exercise)
        _id.value = id
    }

    fun save() = viewModelScope.launch {
        if (_initialExerciseState.value !is Result.Success) return@launch
        if (uiState.value !is Result.Success) return@launch
        val oldExercise = _initialExerciseState.value as Result.Success
        val exercise = Exercise(
            _id.value,
            _nameState.value.value!!,
            _descriptionState.value.value!!,
            _durationState.value.value!!,
            _repetitionsState.value.value!!
        )
        repository.updateExercise(exercise)
    }

    fun getAllExercises(): Flow<List<Exercise>> {
        return repository.getAllExercises()
    }
}