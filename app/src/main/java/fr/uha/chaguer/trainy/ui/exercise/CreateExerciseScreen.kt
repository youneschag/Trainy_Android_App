package fr.uha.chaguer.trainy.ui.exercise

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.ui.StateScreen
import fr.uha.chaguer.android.ui.app.AppMenuEntry
import fr.uha.chaguer.android.ui.app.AppTopBar
import fr.uha.chaguer.android.ui.field.FieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedIntFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedTextField
import androidx.compose.ui.platform.LocalContext
import fr.uha.chaguer.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun CreateExerciseScreen(
    vm: ExerciseViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val existingExercises by vm.getAllExercises().collectAsStateWithLifecycle(emptyList())

    var showErrorDialog by remember { mutableStateOf(false) } // État pour afficher le popup
    var showDuplicateDialog by remember { mutableStateOf(false) } // Popup pour l'unicité
    val context = LocalContext.current

    // Variables pour stocker les valeurs des champs
    var exerciseName by remember { mutableStateOf("") }
    var exerciseDescription by remember { mutableStateOf("") }
    var exerciseDuration by remember { mutableStateOf(0) }
    var exerciseRepetitions by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        vm.initializeFields()
        vm.titleBuilder.setScreenNameId(R.string.create_exercise)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Créer un exercice", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        bottomBar = {
            // Bouton Enregistrer dans la barre inférieure
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (exerciseName.isNotBlank() &&
                            exerciseDescription.isNotBlank() &&
                            exerciseDuration > 0 &&
                            exerciseRepetitions > 0
                        ) {
                            // Vérifier l'unicité avant la création
                            val isDuplicate = existingExercises.any { existing ->
                                existing.name == exerciseName &&
                                        existing.description == exerciseDescription &&
                                        existing.duration == exerciseDuration &&
                                        existing.repetitions == exerciseRepetitions
                            }

                            if (isDuplicate) {
                                showDuplicateDialog = true // Affiche le popup pour l'unicité
                            } else {
                                // Crée l'exercice avec les valeurs des variables
                                vm.create(
                                    Exercise(
                                        exerciseId = 0, // Auto-généré
                                        name = exerciseName,
                                        description = exerciseDescription,
                                        duration = exerciseDuration,
                                        repetitions = exerciseRepetitions
                                    )
                                )
                                Toast.makeText(context, "Exercice ajouté avec succès", Toast.LENGTH_SHORT).show()
                                navigator.popBackStack()
                            }
                        } else {
                            showErrorDialog = true // Affiche le popup d'erreur de validation
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Enregistrer")
                    Text(text = "Enregistrer", modifier = Modifier.padding(start = 8.dp))
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
            // Utilisation de StateScreen pour initialiser et afficher les champs
            StateScreen(state = uiState) { content ->
                exerciseName = content.nameState.value ?: ""
                exerciseDescription = content.descriptionState.value ?: ""
                exerciseDuration = content.durationState.value ?: 0
                exerciseRepetitions = content.repetitionsState.value ?: 0

                OutlinedTextFieldWrapper(
                    field = content.nameState,
                    onValueChange = {
                        exerciseName = it
                        vm.send(ExerciseViewModel.UIEvent.NameChanged(it))
                    },
                    labelId = R.string.exercise_name,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextFieldWrapper(
                    field = content.descriptionState,
                    onValueChange = {
                        exerciseDescription = it
                        vm.send(ExerciseViewModel.UIEvent.DescriptionChanged(it))
                    },
                    labelId = R.string.exercise_description,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedIntFieldWrapper(
                    field = content.durationState,
                    onValueChange = {
                        exerciseDuration = it
                        vm.send(ExerciseViewModel.UIEvent.DurationChanged(it))
                    },
                    labelId = R.string.exercise_duration,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedIntFieldWrapper(
                    field = content.repetitionsState,
                    onValueChange = {
                        exerciseRepetitions = it
                        vm.send(ExerciseViewModel.UIEvent.RepetitionsChanged(it))
                    },
                    labelId = R.string.exercise_repetitions,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Popup d'erreur
        if (showErrorDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                confirmButton = {
                    Button(onClick = { showErrorDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Erreur") },
                text = { Text("Veuillez remplir tous les champs avant d'enregistrer l'exercice") }
            )
        }

        // Popup d'erreur d'unicité
        if (showDuplicateDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showDuplicateDialog = false },
                confirmButton = {
                    Button(onClick = { showDuplicateDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Exercice dupliqué") },
                text = { Text("Un exercice avec les mêmes caractéristiques existe déjà.") }
            )
        }
    }
}