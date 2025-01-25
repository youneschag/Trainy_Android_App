package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.uha.chaguer.android.ui.field.OutlinedIntFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedDateFieldWrapper
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.ui.exercise.OutlinedExercisesFieldWrapper

@Composable
fun SuccessRoutineScreen(
    routine: RoutineViewModel.UIState,
    send: (RoutineViewModel.UIEvent) -> Unit,
) {
    Scaffold(
        topBar = {
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            // Champ pour le nom de la routine
            OutlinedTextFieldWrapper(
                field = routine.name,
                onValueChange = { send(RoutineViewModel.UIEvent.NameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.name,
            )

            // Champ pour la date de début
            OutlinedDateFieldWrapper(
                field = routine.startDay,
                onValueChange = { send(RoutineViewModel.UIEvent.StartDayChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.start_date,
            )

            // Champ pour la fréquence
            OutlinedIntFieldWrapper(
                field = routine.frequency,
                onValueChange = { send(RoutineViewModel.UIEvent.FrequencyChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.frequency,
            )

            // Nouveau champ pour l'objectif
            OutlinedTextFieldWrapper(
                field = routine.objective,
                onValueChange = { send(RoutineViewModel.UIEvent.ObjectiveChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.objective,
            )

            // Liste des exercices
            /*OutlinedExercisesFieldWrapper(
                field = routine.exercises,
                onExerciseChange = { updatedExercise ->
                    send(RoutineViewModel.UIEvent.UpdateExercise(updatedExercise.id, updatedExercise.name))
                },
                onExerciseDelete = { exerciseToDelete ->
                    send(RoutineViewModel.UIEvent.RemoveExercise(exerciseToDelete.id))
                },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.exercises
            )*/
        }
    }
}