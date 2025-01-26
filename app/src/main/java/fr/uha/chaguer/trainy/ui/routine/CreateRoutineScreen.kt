package fr.uha.chaguer.trainy.ui.routine

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Créer une routine", style = MaterialTheme.typography.titleLarge) },
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
                        if (routineName.isNotBlank() && routineObjective.isNotBlank() && routineFrequency > 0
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
                routineName = content.nameState.value ?: ""
                routineDay = content.dayState.value ?: Date()
                routineObjective = content.objectiveState.value ?: ""
                routineFrequency = content.frequencyState.value ?: 0

                // Champs de formulaire
                OutlinedTextFieldWrapper(
                    field = content.nameState,
                    onValueChange = {
                        routineName = it
                        vm.send(RoutineViewModel.UIEvent.NameChanged(it))
                    },
                    labelId = R.string.routine_name,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedDateFieldWrapper(
                    field = content.dayState,
                    onValueChange = {
                        routineDay = it
                        vm.send(RoutineViewModel.UIEvent.StartDayChanged(it))
                    },
                    labelId = R.string.routine_frequency,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextFieldWrapper(
                    field = content.objectiveState,
                    onValueChange = {
                        routineObjective = it
                        vm.send(RoutineViewModel.UIEvent.ObjectiveChanged(it))
                    },
                    labelId = R.string.routine_objective,
                    modifier = Modifier.fillMaxWidth()
                )

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
        }

        // Popup d'erreur
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                confirmButton = {
                    Button(onClick = { showErrorDialog = false }) {
                        Text("OK")
                    }
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
                    Button(onClick = { showDuplicateDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Routine dupliquée") },
                text = { Text("Une routine avec les mêmes caractéristiques existe déjà") }
            )
        }
    }
}