package fr.uha.chaguer.trainy.ui.exercise

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fr.uha.chaguer.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Exercise

@Destination<RootGraph>
@Composable
fun CreateExerciseScreen(
    vm: ExerciseViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val existingExercises by vm.getAllExercises().collectAsStateWithLifecycle(emptyList())

    var showErrorDialog by remember { mutableStateOf(false) }
    var showDuplicateDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var exerciseName by remember { mutableStateOf("") }
    var exerciseDescription by remember { mutableStateOf("") }
    var exerciseDuration by remember { mutableStateOf(0) }
    var exerciseRepetitions by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        vm.initializeFields()
        vm.titleBuilder.setScreenNameId(R.string.create_exercise)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // 🔹 Moins d'espace entre les champs
    ) {
        // ✅ Barre supérieure avec flèche de retour et titre
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navigator.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Retour",
                    tint = Color(0xFF673AB7)
                )
            }
            Text(
                text = "✏ Créer un exercice",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Divider(
            color = Color(0xFF673AB7),
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        StateScreen(state = uiState) { content ->
            exerciseName = content.nameState.value ?: ""
            exerciseDescription = content.descriptionState.value ?: ""
            exerciseDuration = content.durationState.value ?: 0
            exerciseRepetitions = content.repetitionsState.value ?: 0

            FieldWithIcon(
                icon = Icons.Default.FitnessCenter, // 🏋️ Icône pour le nom
                content = {
                    OutlinedTextFieldWrapper(
                        field = content.nameState,
                        onValueChange = {
                            exerciseName = it
                            vm.send(ExerciseViewModel.UIEvent.NameChanged(it))
                        },
                        labelId = R.string.exercise_name,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )

            FieldWithIcon(
                icon = Icons.Default.List, // 📝 Icône pour la description
                content = {
                    OutlinedTextFieldWrapper(
                        field = content.descriptionState,
                        onValueChange = {
                            exerciseDescription = it
                            vm.send(ExerciseViewModel.UIEvent.DescriptionChanged(it))
                        },
                        labelId = R.string.exercise_description,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )


            FieldWithIcon(
                icon = Icons.Default.Timer, // ⏳ Icône pour la durée
                content = {
                    OutlinedIntFieldWrapper(
                        field = content.durationState,
                        onValueChange = {
                            exerciseDuration = it
                            vm.send(ExerciseViewModel.UIEvent.DurationChanged(it))
                        },
                        labelId = R.string.exercise_duration,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )

            FieldWithIcon(
                icon = Icons.Default.Repeat, // 🔄 Icône pour les répétitions
                content = {
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
            )
        }

        // ✅ Bouton "Enregistrer"
        Button(
            onClick = {
                if (exerciseName.isNotBlank() &&
                    exerciseDescription.isNotBlank() &&
                    exerciseDuration > 0 &&
                    exerciseRepetitions > 0
                ) {
                    val isDuplicate = existingExercises.any { existing ->
                        existing.name == exerciseName &&
                                existing.description == exerciseDescription &&
                                existing.duration == exerciseDuration &&
                                existing.repetitions == exerciseRepetitions
                    }

                    if (isDuplicate) {
                        showDuplicateDialog = true
                    } else {
                        vm.create(
                            Exercise(
                                exerciseId = 0,
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
                    showErrorDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(50.dp), // 🔹 Augmente la taille du bouton
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50), // ✅ Vert harmonisé
                contentColor = Color.White
            )
        ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = "Enregistrer")
            Text(text = "Enregistrer", modifier = Modifier.padding(start = 8.dp))
        }
    }

    // ✅ Popup d'erreur
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

    // ✅ Popup d'erreur d'unicité
    if (showDuplicateDialog) {
        AlertDialog(
            onDismissRequest = { showDuplicateDialog = false },
            confirmButton = {
                Button(onClick = { showDuplicateDialog = false }) { Text("OK") }
            },
            title = { Text("Exercice dupliqué") },
            text = { Text("Un exercice avec les mêmes caractéristiques existe déjà.") }
        )
    }
}