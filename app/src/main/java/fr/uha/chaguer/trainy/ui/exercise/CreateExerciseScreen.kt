package fr.uha.chaguer.trainy.ui.exercise

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.ui.StateScreen
import androidx.compose.ui.platform.LocalContext
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Exercise

@Destination<RootGraph>
@Composable
fun CreateExerciseScreen(
    vm: ExerciseViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.create(
            Exercise(0, "Push-ups", "Arms in the bar while holding", 15, 3)
        )
        vm.titleBuilder.setScreenNameId(R.string.create_exercise)
    }

    StateScreen(state = uiState) { content ->
        SuccessExerciseScreen(
            exercise = content,
            send = { vm.send(it) },
            onBackClick = {
                if (content.nameState.value.isNullOrBlank() ||
                    content.descriptionState.value.isNullOrBlank() ||
                    (content.durationState.value ?: 0) <= 0 ||
                    (content.repetitionsState.value ?: 0) <= 0
                ) {
                    showErrorDialog = true
                } else {
                    Toast.makeText(context, "Exercice ajouté avec succès", Toast.LENGTH_SHORT).show()
                    navigator.popBackStack()
                }
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) { Text("OK") }
            },
            title = { Text("Erreur") },
            text = { Text("Veuillez remplir tous les champs avant d'enregistrer l'exercice") }
        )
    }
}