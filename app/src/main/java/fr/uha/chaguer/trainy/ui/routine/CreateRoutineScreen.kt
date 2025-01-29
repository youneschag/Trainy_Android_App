package fr.uha.chaguer.trainy.ui.routine

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.ui.StateScreen
import fr.uha.chaguer.android.ui.field.OutlinedDateFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedIntFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.ui.exercise.ExerciseViewModel
import fr.uha.chaguer.trainy.ui.exercise.FieldWithIcon
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun CreateRoutineScreen(
    vm: RoutineViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val existingRoutines by vm.getAllRoutines().collectAsStateWithLifecycle(emptyList())

    var showErrorDialog by remember { mutableStateOf(false) } // État pour afficher le popup
    var showDuplicateDialog by remember { mutableStateOf(false) } // Popup pour l'unicité
    val context = LocalContext.current

    // Variables pour stocker les valeurs des champs
    var routineName by remember { mutableStateOf("") }
    var routineDay by remember { mutableStateOf(Date()) }
    var routineObjective by remember { mutableStateOf("") }
    var routineFrequency by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        vm.initializeFields()
        vm.titleBuilder.setScreenNameId(R.string.create_routine)
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
                text = "✏ Créer une routine",
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
            routineName = content.nameState.value ?: ""
            routineDay = content.dayState.value ?: Date()
            routineObjective = content.objectiveState.value ?: ""
            routineFrequency = content.frequencyState.value ?: 0

            FieldWithIcon(
                icon = Icons.Default.FitnessCenter, // 🏋️ Icône pour le nom
                content = {
                    OutlinedTextFieldWrapper(
                        field = content.nameState,
                        onValueChange = {
                            routineName = it
                            vm.send(RoutineViewModel.UIEvent.NameChanged(it))
                        },
                        labelId = R.string.routine_name,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )

            FieldWithIcon(
                icon = Icons.Default.DateRange, // 📝 Icône pour la date
                content = {
                    OutlinedDateFieldWrapper(
                        field = content.dayState,
                        onValueChange = {
                            routineDay = it
                            vm.send(RoutineViewModel.UIEvent.StartDayChanged(it))
                        },
                        labelId = R.string.routine_frequency,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )

            FieldWithIcon(
                icon = Icons.Default.List, // 📝 Icône pour l'objectif
                content = {
                    OutlinedTextFieldWrapper(
                        field = content.objectiveState,
                        onValueChange = {
                            routineObjective = it
                            vm.send(RoutineViewModel.UIEvent.ObjectiveChanged(it))
                        },
                        labelId = R.string.routine_objective,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )

            FieldWithIcon(
                icon = Icons.Default.Repeat, // 🔄 Icône pour les répétitions
                content = {
                    OutlinedIntFieldWrapper(
                        field = content.frequencyState,
                        onValueChange = {
                            routineFrequency = it
                            vm.send(RoutineViewModel.UIEvent.FrequencyChanged(it))
                        },
                        labelId = R.string.routine_frequency,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }

        Button(
            onClick = {
                if (routineName.isNotBlank() &&
                    routineObjective.isNotBlank() &&
                    routineFrequency > 0
                ) {
                    // Vérifier l'unicité avant la création
                    val isDuplicate = existingRoutines.any { existing ->
                        existing.name == routineName &&
                                existing.startDay.time == routineDay.time && // Compare les valeurs de date
                                existing.objective == routineObjective &&
                                existing.frequency == routineFrequency
                    }

                    if (isDuplicate) {
                        showDuplicateDialog = true // Affiche le popup pour l'unicité
                    }
                    else {
                        // Crée la routine
                        vm.create(
                            Routine(
                                routineId = 0, // Auto-généré
                                name = routineName,
                                frequency = routineFrequency,
                                objective = routineObjective,
                                startDay = Date()
                            )
                        )
                        Toast.makeText(context,"Routine ajoutée avec succès",Toast.LENGTH_SHORT).show()
                        navigator.popBackStack()
                    }
                } else {
                    showErrorDialog = true // Affiche le popup d'erreur de validation
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

    // Popup d'erreur
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) { Text("OK") }
            },
            title = { Text("Erreur") },
            text = { Text("Veuillez remplir tous les champs avant d'enregistrer la routine") }
        )
    }

    // Popup d'erreur d'unicité
    if (showDuplicateDialog) {
        AlertDialog(
            onDismissRequest = { showDuplicateDialog = false },
            confirmButton = {
                Button(onClick = { showDuplicateDialog = false }) { Text("OK") }
            },
            title = { Text("Routine dupliquée") },
            text = { Text("Une routine avec les mêmes caractéristiques existe déjà") }
        )
    }
}