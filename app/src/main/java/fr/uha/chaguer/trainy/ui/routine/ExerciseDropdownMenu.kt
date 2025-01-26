package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.uha.chaguer.trainy.model.Exercise

@Composable
fun ExerciseDropdownMenu(
    exercises: List<Exercise>,
    selectedExercise: Exercise?,
    onExerciseSelected: (Exercise) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // Le bouton pour afficher le menu déroulant
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = selectedExercise?.name ?: "Sélectionner un exercice",
                modifier = Modifier.padding(8.dp)
            )
        }

        // Le menu déroulant
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            exercises.forEach { exercise ->
                DropdownMenuItem(
                    onClick = {
                        onExerciseSelected(exercise)
                        expanded = false
                    },
                    text = { Text(text = exercise.name) }
                )
            }
        }
    }
}