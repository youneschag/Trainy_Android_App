package fr.uha.chaguer.trainy.ui.exercise

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateExerciseScreenDestination
import com.ramcosta.composedestinations.generated.destinations.EditExerciseScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.ui.StateScreen
import fr.uha.chaguer.android.ui.SwipeableItem
import fr.uha.chaguer.android.ui.app.AppMenuEntry
import fr.uha.chaguer.android.ui.app.AppTopBar
import fr.uha.chaguer.android.ui.app.UITitleState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.ui.theme.MontserratFont

@Destination<RootGraph>
@Composable
fun ExerciseListScreen(
    vm: ListExercisesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    var exerciseToDelete by remember { mutableStateOf<Exercise?>(null) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

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
                text = stringResource(R.string.list_exercise),
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

        // ✅ Barre de recherche
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("🔍 Rechercher un exercice") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        var isExerciseListEmpty by remember { mutableStateOf(true) } // État pour vérifier si la liste est vide

        // ✅ Liste des exercices
        StateScreen(state = uiState) { content ->
            val filteredExercises = content.exercises.filter {
                searchQuery.length < 3 || it.name.contains(searchQuery, ignoreCase = true)
            }

            isExerciseListEmpty = content.exercises.isEmpty() // Mise à jour de l'état

            if (content.exercises.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aucun exercice disponible",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            } else if (filteredExercises.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aucun exercice trouvé",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    itemsIndexed(filteredExercises) { index, exercise ->
                        ExerciseItem(
                            exercise = exercise,
                            onEdit = { navigator.navigate(EditExerciseScreenDestination(exercise.exerciseId)) },
                            onDelete = { exerciseToDelete = exercise },
                            isEven = index % 2 == 0
                        )
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
                onClick = { navigator.navigate(CreateExerciseScreenDestination) },
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
                enabled = !isExerciseListEmpty, // ✅ Désactivation propre si la liste est vide
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Tout supprimer")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tout supprimer")
            }
        }
    }

    // ✅ Popup confirmation suppression d'un exercice
    if (exerciseToDelete != null) {
        AlertDialog(
            onDismissRequest = { exerciseToDelete = null },
            title = { Text("❗ Confirmer la suppression") },
            text = { Text("Voulez-vous vraiment supprimer cet exercice ?") },
            confirmButton = {
                Button(onClick = {
                    exerciseToDelete?.let { vm.send(ListExercisesViewModel.UIEvent.OnDelete(it)) }
                    exerciseToDelete = null
                }) { Text("Supprimer") }
            },
            dismissButton = {
                Button(onClick = { exerciseToDelete = null }) { Text("Annuler") }
            }
        )
    }

    // ✅ Popup confirmation suppression de tous les exercices
    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        vm.deleteAllExercises()
                        Toast.makeText(context, "Tous les exercices ont été supprimés", Toast.LENGTH_SHORT).show()
                        showDeleteAllDialog = false
                    }
                ) { Text("Oui") }
            },
            dismissButton = {
                Button(onClick = { showDeleteAllDialog = false }) { Text("Non") }
            },
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr de vouloir supprimer tous les exercices ?") }
        )
    }
}

@Composable
fun SuccessListExercisesScreen(
    uiState: ListExercisesViewModel.UIState,
    navigator: DestinationsNavigator,
    onDeleteRequest: (Exercise) -> Unit,
    send: (ListExercisesViewModel.UIEvent) -> Unit
) {
    LazyColumn {
        itemsIndexed(
            items = uiState.exercises,
            key = { _, item -> item.exerciseId }
        ) { index, item ->
            ExerciseItem(
                exercise = item,
                onEdit = { navigator.navigate(EditExerciseScreenDestination(it.exerciseId)) },
                onDelete = { onDeleteRequest(it) }, // Appeler `onDeleteRequest` au lieu de supprimer directement
                isEven = index % 2 == 0 // Détermine si l'exercice est pair ou impair
            )
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    onEdit: (Exercise) -> Unit,
    onDelete: (Exercise) -> Unit,
    isEven: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = if (isEven) Color(0xFFF5F5DC) else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "🏋️ ${exercise.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF673AB7)
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "📝 Description:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = exercise.description,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(2f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "⏳ Durée:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${exercise.duration} min",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(2f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "🔄 Répétitions:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${exercise.repetitions}x",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(2f)
                    )
                }
            }

            Row {
                IconButton(onClick = { onEdit(exercise) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Modifier",
                        tint = Color.Green
                    )
                }

                IconButton(onClick = { onDelete(exercise) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Supprimer",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}