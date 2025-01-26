package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.database.DateUtils.formatDate
import fr.uha.chaguer.trainy.model.Exercise
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun RoutineDetailsScreen(
    routineId: Long,
    vm: RoutineViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val routineWithExercises by vm.getRoutineWithExercises(routineId)
        .collectAsStateWithLifecycle(initialValue = null)
    val allExercises by vm.getAllExercises().collectAsStateWithLifecycle(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Détails de la routine") },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { innerPadding ->
        routineWithExercises?.let { fullRoutine ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Display routine details
                Text(
                    text = fullRoutine.routine.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Objectif: ${fullRoutine.routine.objective}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Fréquence: ${fullRoutine.routine.frequency} fois/semaine",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Début: ${formatDate(fullRoutine.routine.startDay)}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Divider()

                // List of associated exercises
                Text(
                    text = "Exercices associés",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                if (fullRoutine.exercises.isEmpty()) {
                    Text(text = "Aucun exercice associé.", color = Color.Gray)
                } else {
                    fullRoutine.exercises.forEach { exercise ->
                        Text(text = exercise.name, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown to add existing exercises
                var selectedExercise by remember { mutableStateOf<Exercise?>(null) }

                ExerciseDropdownMenu(
                    exercises = allExercises.filter { it !in fullRoutine.exercises },
                    selectedExercise = selectedExercise,
                    onExerciseSelected = { selectedExercise = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        selectedExercise?.let {
                            vm.addExerciseToRoutine(routineId, it.exerciseId)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Associer l'exercice")
                }
            }
        }
    }
}