package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.ramcosta.composedestinations.generated.destinations.CreateRoutineScreenDestination
import com.ramcosta.composedestinations.generated.destinations.EditRoutineScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.ui.StateScreen
import fr.uha.chaguer.android.ui.SwipeableItem
import fr.uha.chaguer.android.ui.app.AppTopBar
import fr.uha.chaguer.android.ui.app.UITitleState
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Routine

@Destination<RootGraph>
@Composable
fun RoutineListScreen(
    vm: ListRoutinesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar(UITitleState(screenNameId = R.string.list_routine)) },
        floatingActionButton = {
            YourFloatingActionButton(navigator = navigator)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            StateScreen(state = uiState) { content ->
                if (content.routines.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Aucune routine n'est disponible.", fontSize = 16.sp)
                    }
                } else {
                    SuccessListRoutinesScreen(content, navigator, { vm.send(it) })
                }
            }
        }
    }
}

@Composable
fun YourFloatingActionButton(navigator: DestinationsNavigator) {
    val addRoutineDescription = stringResource(id = R.string.add_routine)

    FloatingActionButton(
        onClick = { navigator.navigate(CreateRoutineScreenDestination) },
        modifier = Modifier.semantics { contentDescription = addRoutineDescription }
    ) {
        Icon(Icons.Filled.Add, contentDescription = null)
    }
}

@Composable
fun SuccessListRoutinesScreen(
    uiState: ListRoutinesViewModel.UIState,
    navigator: DestinationsNavigator,
    send: (ListRoutinesViewModel.UIEvent) -> Unit
) {
    // Variable pour contrôler l'affichage de la boîte de dialogue
    var showDialog by remember { mutableStateOf(false) }
    var routineToDelete: Routine? by remember { mutableStateOf(null) }

    if (showDialog && routineToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Voulez-vous vraiment supprimer cette routine ?") },
            confirmButton = {
                TextButton(onClick = {
                    send(ListRoutinesViewModel.UIEvent.OnDelete(routineToDelete!!))
                    showDialog = false
                }) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    LazyColumn {
        items(
            items = uiState.routines,
            key = { item -> item.routineId}
        ) { item ->
            SwipeableItem(
                onEdit = {  },
                onDelete = {
                    // Activer la boîte de dialogue pour confirmer la suppression
                    routineToDelete = item
                    showDialog = true
                }
            ) {
                RoutineItem(item)
                Divider(modifier = Modifier.padding(vertical = 8.dp)) // Ajout de séparateurs
            }
        }
    }
}

@Composable
fun RoutineItem(routine: Routine) {
    ListItem(
        headlineContent = {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(routine.name, fontWeight = FontWeight.Bold)
            }
        },
        supportingContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(id = R.string.frequency, routine.frequency), fontSize = 16.sp)
                Text(stringResource(id = R.string.objective, routine.objective), fontSize = 16.sp)
            }
        }
    )
}