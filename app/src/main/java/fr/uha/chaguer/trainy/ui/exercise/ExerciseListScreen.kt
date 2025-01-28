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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun ExerciseListScreen(
    vm: ListExercisesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    var exerciseToDelete by remember { mutableStateOf<Exercise?>(null) } // État pour la suppression
    var showDeleteAllDialog by remember { mutableStateOf(false) } // État pour le popup de confirmation
    var searchQuery by remember { mutableStateOf("") } // État pour la recherche
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.list_exercise), style = MaterialTheme.typography.titleLarge) }
            )
        },
        bottomBar = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ) {
                // Bouton Ajouter
                Button(
                    onClick = { navigator.navigate(CreateExerciseScreenDestination) },
                    modifier = Modifier
                        .weight(1f) // Prend 50 % de la largeur
                        .fillMaxWidth()
                        .shadow(4.dp, shape = MaterialTheme.shapes.medium),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Text(text = "Ajouter", modifier = Modifier.padding(start = 8.dp))
                }

                // Bouton Supprimer tout
                Button(
                    onClick = { showDeleteAllDialog = true }, // Affiche le popup
                    modifier = Modifier
                        .weight(1f) // Prend 50 % de la largeur
                        .fillMaxWidth()
                        .shadow(4.dp, shape = MaterialTheme.shapes.medium),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                    Text(text = "Tout supprimer", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            // Barre de recherche
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Rechercher un exercice") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )

            StateScreen(state = uiState) { content ->
                // Filtrer les exercices en fonction de la recherche
                val filteredExercises = content.exercises.filter { exercise ->
                    searchQuery.length < 3 || exercise.name.contains(searchQuery, ignoreCase = true)
                }

                when {
                    // Si la liste globale est vide
                    content.exercises.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.no_exercises_message),
                                fontSize = 16.sp
                            )
                        }
                    }

                    // Si la liste filtrée est vide
                    filteredExercises.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Aucun exercice trouvé",
                                fontSize = 16.sp
                            )
                        }
                    }

                    // Sinon, afficher les exercices filtrés
                    else -> {
                        LazyColumn {
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
            }
        }

        // Boîte de dialogue de confirmation
        if (exerciseToDelete != null) {
            AlertDialog(
                onDismissRequest = { exerciseToDelete = null },
                title = {
                    Text("Confirmer la suppression")
                },
                text = {
                    Text("Voulez-vous vraiment supprimer cet exercice ?")
                },
                confirmButton = {
                    Button(onClick = {
                        exerciseToDelete?.let { vm.send(ListExercisesViewModel.UIEvent.OnDelete(it)) }
                        exerciseToDelete = null
                    }) {
                        Text("Supprimer")
                    }
                },
                dismissButton = {
                    Button(onClick = { exerciseToDelete = null }) {
                        Text("Annuler")
                    }
                }
            )
        }

        // Popup de confirmation
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
                    ) {
                        Text("Oui")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteAllDialog = false }) {
                        Text("Non")
                    }
                },
                title = { Text("Confirmation") },
                text = { Text("Êtes-vous sûr de vouloir supprimer tous les exercices ?") }
            )
        }
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Ajout de padding externe
            .shadow(8.dp, shape = MaterialTheme.shapes.medium) // Ajout d'ombre
            .background(
                if (isEven) Color(0xFFF5F5DC) else Color.White, // Alterne les couleurs du conteneur
                shape = MaterialTheme.shapes.medium
            )
            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium), // Contours pour tout l'exercice
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Informations sur l'exercice
        Column(
            modifier = Modifier.weight(1f).padding(8.dp) // Ajout de padding interne
        ) {
            Text(
                text = "${exercise.name} - ${exercise.duration} min",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = exercise.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
        }

        // Nombre de répétitions
        Text(
            text = "${exercise.repetitions}x",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(48.dp),
            color = Color.Black
        )

        // Boutons Modifier et Supprimer
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Bouton Modifier (vert)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.Green, shape = MaterialTheme.shapes.small)
                    .clickable { onEdit(exercise) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Modifier",
                    tint = Color.White
                )
            }

            // Bouton Supprimer (rouge)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.Red, shape = MaterialTheme.shapes.small)
                    .clickable { onDelete(exercise) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = Color.White
                )
            }
        }
    }
}