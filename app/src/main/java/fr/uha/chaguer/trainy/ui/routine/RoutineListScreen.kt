package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateExerciseScreenDestination
import com.ramcosta.composedestinations.generated.destinations.RoutineDetailsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.CreateRoutineScreenDestination
import com.ramcosta.composedestinations.generated.destinations.EditRoutineScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.database.DateUtils.formatDate
import fr.uha.chaguer.android.ui.StateScreen
import fr.uha.chaguer.android.ui.SwipeableItem
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.FullRoutine
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.ui.theme.MontserratFont
import okhttp3.internal.format
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Destination<RootGraph>
@Composable
fun RoutineListScreen(
    vm: ListRoutinesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    var routineToDelete by remember { mutableStateOf<Routine?>(null) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    val fullRoutines by vm.getAllFullRoutines().collectAsStateWithLifecycle(emptyList())
    val expandedRoutines = remember { mutableStateMapOf<Long, Boolean>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        // ✅ Titre et barre violette
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.list_routine),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFont
            )
            Divider(
                color = Color(0xFF673AB7),
                thickness = 2.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        var isRoutineListEmpty by remember { mutableStateOf(true) } // État pour vérifier si la liste est vide

        StateScreen(state = uiState) { content ->
            isRoutineListEmpty = content.routines.isEmpty() // Mise à jour de l'état

            if (content.routines.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aucune routine disponible",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    itemsIndexed(
                        items = fullRoutines,
                        key = { _, fullRoutine -> fullRoutine.routine.routineId } // Clé unique
                    ) { index, fullRoutine ->
                        val isExpanded = expandedRoutines[fullRoutine.routine.routineId] == true

                        SwipeableItem(
                            modifier = Modifier.padding(8.dp),
                            onDelete = { routineToDelete = fullRoutine.routine },
                            onEdit = { navigator.navigate(EditRoutineScreenDestination(fullRoutine.routine.routineId)) }
                        ) {
                            RoutineItem(
                                fullRoutine = fullRoutine,
                                isExpanded = isExpanded,
                                onExpandToggle = {
                                    expandedRoutines[fullRoutine.routine.routineId] = !isExpanded
                                },
                                onAddExercise = { navigator.navigate(RoutineDetailsScreenDestination(fullRoutine.routine.routineId)) },
                                onRemoveExercise = { exerciseId ->
                                    vm.removeExerciseFromRoutine(fullRoutine.routine.routineId, exerciseId)
                                },
                                isEven = index % 2 == 0
                            )
                        }
                    }
                }
            }
        }

        // ✅ Boutons "Ajouter" et "Tout supprimer" RESTENT visibles
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = { navigator.navigate(CreateRoutineScreenDestination) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5E35B1),
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Ajouter")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ajouter")
            }

            Button(
                onClick = { showDeleteAllDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White
                ),
                enabled = !isRoutineListEmpty, // ✅ Désactivation propre si la liste est vide
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Tout supprimer")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tout supprimer")
            }
        }
    }

    // Boîte de dialogue de confirmation pour suppression
    if (routineToDelete != null) {
        AlertDialog(
            onDismissRequest = { routineToDelete = null },
            title = { Text("❗Confirmer la suppression") },
            text = { Text("Voulez-vous vraiment supprimer cette routine ?") },
            confirmButton = {
                Button(onClick = {
                    routineToDelete?.let { vm.send(ListRoutinesViewModel.UIEvent.OnDelete(it)) }
                    routineToDelete = null
                }) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                Button(onClick = { routineToDelete = null }) {
                    Text("Annuler")
                }
            }
        )
    }

    // Boîte de dialogue de confirmation pour supprimer toutes les routines
    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            confirmButton = {
                Button(onClick = {
                    vm.deleteAllRoutines()
                    showDeleteAllDialog = false
                }) {
                    Text("Oui")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteAllDialog = false }) {
                    Text("Non")
                }
            },
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr de vouloir supprimer toutes les routines ?") }
        )
    }
}

@Composable
fun SuccessListRoutinesScreen(
    fullRoutines: List<FullRoutine>,
    navigator: DestinationsNavigator,
    expandedRoutines: MutableMap<Long, Boolean>,
    onDeleteRequest: (Routine) -> Unit,
    onEditRequest: (Routine) -> Unit,
    onAddExercise: (Long) -> Unit,
    removeExerciseFromRoutine: (Long, Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(
            items = fullRoutines,
            key = { _, fullRoutine -> fullRoutine.routine.routineId } // Clé unique
        ) { index, fullRoutine ->
            val isExpanded = expandedRoutines[fullRoutine.routine.routineId] == true

            SwipeableItem(
                modifier = Modifier.padding(8.dp),
                onDelete = { onDeleteRequest(fullRoutine.routine) },
                onEdit = { onEditRequest(fullRoutine.routine) }
            ) {
                RoutineItem(
                    fullRoutine = fullRoutine,
                    isExpanded = isExpanded,
                    onExpandToggle = {
                        expandedRoutines[fullRoutine.routine.routineId] = !isExpanded
                    },
                    onAddExercise = { onAddExercise(fullRoutine.routine.routineId) },
                    onRemoveExercise = { exerciseId ->
                        removeExerciseFromRoutine(fullRoutine.routine.routineId, exerciseId)
                    },
                    isEven = index % 2 == 0
                )
            }
        }
    }
}

@Composable
fun RoutineItem(
    fullRoutine: FullRoutine,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    onAddExercise: () -> Unit,
    onRemoveExercise: (Long) -> Unit,
    isEven: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp, shape = MaterialTheme.shapes.medium)
            .background(
                if (isEven) Color(0xFFF5F5DC) else Color.White,
                shape = MaterialTheme.shapes.medium
            )
            .border(1.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "📌 ${fullRoutine.routine.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = { onExpandToggle() }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Réduire" else "Agrandir"
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = null, tint = Color(0xFF673AB7))
            Text(text = " Objectif :", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
            Text(text = fullRoutine.routine.objective, modifier = Modifier.padding(start = 8.dp))
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = Color(0xFF673AB7))
            Text(text = " Fréquence :", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
            Text(text = "${fullRoutine.routine.frequency} fois/semaine", modifier = Modifier.padding(start = 8.dp))
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(imageVector = Icons.Default.List, contentDescription = null, tint = Color(0xFF673AB7))
            Text(text = " Début :", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
            Text(text = formatDate(fullRoutine.routine.startDay), modifier = Modifier.padding(start = 8.dp))
        }

        // Show exercises if expanded
        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Exercices associés :",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            if (fullRoutine.exercises.isEmpty()) {
                Text(
                    text = "Aucun exercice associé.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            } else {
                fullRoutine.exercises.forEach { exercise ->
                    ExerciseItem(
                        exercise = exercise,
                        onRemove = { onRemoveExercise(exercise.exerciseId) }
                    )
                }
            }

            // Button to add an exercise
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onAddExercise,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Ajouter un exercice")
            }
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    onRemove: () -> Unit // Callback pour supprimer l'exercice de la routine
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, shape = MaterialTheme.shapes.medium)
            .background(Color(0xFFF0F0F0), shape = MaterialTheme.shapes.medium)
            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // ✅ Nom de l'exercice avec une icône
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = "Nom", tint = Color(0xFF673AB7))
                Text(
                    text = " ${exercise.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // ✅ Description avec icône + texte souligné et gras
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.List, contentDescription = "Description", tint = Color(0xFF673AB7))
                Text(
                    text = " Description :",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline),
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Text(
                    text = " ${exercise.description}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // ✅ Durée avec icône + texte souligné et gras
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Timer, contentDescription = "Durée", tint = Color(0xFF673AB7))
                Text(
                    text = " Durée :",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline),
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Text(
                    text = " ${exercise.duration} minutes",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // ✅ Répétitions avec icône + texte souligné et gras
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Repeat, contentDescription = "Répétitions", tint = Color(0xFF673AB7))
                Text(
                    text = " Répétitions :",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline),
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Text(
                    text = " ${exercise.repetitions}x",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // ✅ Bouton de suppression
        IconButton(
            onClick = onRemove,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Supprimer l'association",
                tint = Color.Red
            )
        }
    }
}