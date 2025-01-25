package fr.uha.chaguer.trainy.ui.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Exercise

@Destination<RootGraph>
@Composable
fun ExerciseListScreen(
    vm: ListExercisesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.list_exercise), style = MaterialTheme.typography.titleLarge)
                Button(
                    onClick = { navigator.navigate(CreateExerciseScreenDestination) },
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .pointerInput(Unit) {}
                        .semantics { contentDescription = "Add Exercise" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Text(text = "Ajouter", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            StateScreen(state = uiState) { content ->
                if (content.exercises.isEmpty()) {
                    // Afficher un message si la liste est vide
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
                } else {
                    SuccessListExercisesScreen(content, navigator, { vm.send(it) })
                }
            }
        }
    }
}

@Composable
fun SuccessListExercisesScreen(
    uiState: ListExercisesViewModel.UIState,
    navigator: DestinationsNavigator,
    send: (ListExercisesViewModel.UIEvent) -> Unit
) {
    LazyColumn {
        itemsIndexed(
            items = uiState.exercises,
            key = { index, item -> item.exerciseId }
        ) { index, item ->
            val backgroundColor = if (index % 2 == 0) Color.White else Color(0xFFF5F5DC) // Blanc / Beige
            SwipeableItem(
                onEdit = { navigator.navigate(EditExerciseScreenDestination(item.exerciseId)) },
                onDelete = { send(ListExercisesViewModel.UIEvent.OnDelete(item)) }
            ) {
                ExerciseItem(exercise = item, backgroundColor = backgroundColor)
            }
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise, backgroundColor: Color) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .pointerInput(Unit) {}
            .semantics { contentDescription = "Exercise Item" },
        headlineContent = {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(exercise.name, fontWeight = FontWeight.Bold)
                Text("- ${exercise.duration} min")
            }
        },
        supportingContent = {
            Text(exercise.description, fontSize = 14.sp, fontWeight = FontWeight.Light)
        },
        trailingContent = {
            Text(
                "${exercise.repetitions}x",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.size(48.dp)
            )
        }
    )
}