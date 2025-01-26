package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateRoutineScreenDestination
import com.ramcosta.composedestinations.generated.destinations.EditRoutineScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.ui.StateScreen
import fr.uha.chaguer.android.ui.SwipeableItem
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Routine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun RoutineListScreen(
    vm: ListRoutinesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    var routineToDelete by remember { mutableStateOf<Routine?>(null) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.list_routine), style = MaterialTheme.typography.titleLarge) }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Bouton Ajouter
                Button(
                    onClick = { navigator.navigate(CreateRoutineScreenDestination) },
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
                    onClick = { showDeleteAllDialog = true },
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
            StateScreen(state = uiState) { content ->
                if (content.routines.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Aucune routine n'est disponible pour l'instant.",
                            fontSize = 16.sp
                        )
                    }
                } else {
                    SuccessListRoutinesScreen(
                        uiState = content,
                        navigator = navigator,
                        onDeleteRequest = { routine -> routineToDelete = routine },
                        send = { vm.send(it) }
                    )
                }
            }
        }

        // Boîte de dialogue de confirmation pour suppression
        if (routineToDelete != null) {
            AlertDialog(
                onDismissRequest = { routineToDelete = null },
                title = { Text("Confirmer la suppression") },
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
}

@Composable
fun SuccessListRoutinesScreen(
    uiState: ListRoutinesViewModel.UIState,
    navigator: DestinationsNavigator,
    onDeleteRequest: (Routine) -> Unit,
    send: (ListRoutinesViewModel.UIEvent) -> Unit
) {
    LazyColumn {
        itemsIndexed(
            items = uiState.routines,
            key = { _, item -> item.routineId }
        ) { index, item ->
            SwipeableItem(
                modifier = Modifier.padding(8.dp),
                onEdit = { navigator.navigate(EditRoutineScreenDestination(item.routineId)) }, // Passe l'ID
                onDelete = { onDeleteRequest(item) }
            ) {
                RoutineContent(routine = item, isEven = index % 2 == 0)
            }
        }
    }
}

@Composable
fun RoutineContent(routine: Routine, isEven: Boolean) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = MaterialTheme.shapes.medium) // Ajout d'ombre
            .background(
                if (isEven) Color(0xFFF5F5DC) else Color.White, // Alterne les couleurs du conteneur
                shape = MaterialTheme.shapes.medium
            )
            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
    ){
        Text(
            text = routine.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp) // Décaler à droite
        )
        Text(
            text = "Objectif: ${routine.objective}",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 24.dp) // Décaler à droite
        )
        Text(
            text = "Fréquence: ${routine.frequency} fois/semaine",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 24.dp) // Décaler à droite
        )
        Text(
            text = "Début: ${formatDate(routine.startDay)}",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 24.dp) // Décaler à droite
        )
    }
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(date)
}