package fr.uha.chaguer.trainy.ui.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.ui.StateScreen
import fr.uha.chaguer.android.ui.app.AppMenuEntry
import fr.uha.chaguer.android.ui.app.AppTopBar
import fr.uha.chaguer.android.ui.field.OutlinedIntFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.chaguer.trainy.R

@Destination<RootGraph>
@Composable
fun EditExerciseScreen(
    vm: ExerciseViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    exerciseId: Long
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val uiTitleState by vm.uiTitleState.collectAsStateWithLifecycle()

    // Charger les données de l'exercice à modifier
    LaunchedEffect(Unit) {
        vm.edit(exerciseId)
        vm.titleBuilder.setScreenNameId(R.string.edit_exercise)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                uiTitleState = uiTitleState,
                navigator = navigator,
                menuEntries = emptyList() // Pas de menu, uniquement un bouton en bas
            )
        },
        bottomBar = {
            // Bouton "Modifier" en bas
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        vm.send(ExerciseViewModel.UIEvent.SaveChanges) // Envoi d'événement pour sauvegarder les modifications
                        navigator.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Modifier")
                    Text(
                        text = "Modifier",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Utilisation de StateScreen pour afficher et modifier les champs
            StateScreen(state = uiState) { content ->
                OutlinedTextFieldWrapper(
                    field = content.nameState,
                    onValueChange = { vm.send(ExerciseViewModel.UIEvent.NameChanged(it)) },
                    labelId = R.string.exercise_name,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextFieldWrapper(
                    field = content.descriptionState,
                    onValueChange = { vm.send(ExerciseViewModel.UIEvent.DescriptionChanged(it)) },
                    labelId = R.string.exercise_description,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedIntFieldWrapper(
                    field = content.durationState,
                    onValueChange = { vm.send(ExerciseViewModel.UIEvent.DurationChanged(it)) },
                    labelId = R.string.exercise_duration,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedIntFieldWrapper(
                    field = content.repetitionsState,
                    onValueChange = { vm.send(ExerciseViewModel.UIEvent.RepetitionsChanged(it)) },
                    labelId = R.string.exercise_repetitions,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}