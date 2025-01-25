package fr.uha.chaguer.trainy.ui.exercise

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import fr.uha.chaguer.android.ui.field.OutlinedIntFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.chaguer.trainy.R

@Composable
fun SuccessExerciseScreen(
    exercise: ExerciseViewModel.UIState,
    send: (ExerciseViewModel.UIEvent) -> Unit,
) {
    val context = LocalContext.current

    Column {
        OutlinedTextFieldWrapper(
            field = exercise.nameState,
            onValueChange = { send(ExerciseViewModel.UIEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.exercise_name,
        )
        OutlinedTextFieldWrapper(
            field = exercise.descriptionState,
            onValueChange = { send(ExerciseViewModel.UIEvent.DescriptionChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.exercise_description,
        )
        OutlinedIntFieldWrapper(
            field = exercise.durationState,
            onValueChange = { send(ExerciseViewModel.UIEvent.DurationChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.exercise_duration,
        )
        OutlinedIntFieldWrapper(
            field = exercise.repetitionsState,
            onValueChange = { send(ExerciseViewModel.UIEvent.RepetitionsChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.exercise_repetitions,
        )
    }
}