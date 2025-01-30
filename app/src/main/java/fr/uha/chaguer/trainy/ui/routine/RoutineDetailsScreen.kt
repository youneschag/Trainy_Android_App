package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.database.DateUtils.formatDate
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.ui.exercise.ExerciseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Destination<RootGraph>
@Composable
fun RoutineDetailsScreen(
    vm: RoutineViewModel = hiltViewModel(),
    em: ExerciseViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    routineId: Long,
) {
    val routineWithExercises by vm.getRoutineWithExercises(routineId)
        .collectAsStateWithLifecycle(initialValue = null)
    val allExercises by em.getAllExercises().collectAsStateWithLifecycle(emptyList())

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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
                text = "✍ Détails de la routine",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        routineWithExercises?.let { fullRoutine ->
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = fullRoutine.routine.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "🎯 Objectif: ${fullRoutine.routine.objective}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "📅 Fréquence: ${fullRoutine.routine.frequency} fois/semaine",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "⏳ Début: ${formatDate(fullRoutine.routine.startDay)}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Divider()

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "🏋️ Exercices associés",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (fullRoutine.exercises.isEmpty()) {
                    Text(
                        text = "Aucun exercice associé.",
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        fullRoutine.exercises.forEach { exercise ->
                            Text(
                                text = "✔ ${exercise.name}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                            vm.addExerciseToRoutine(routineId, it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedExercise != null
                ) {
                    Text("📌 Associer l'exercice")
                }
            }
        } ?: run {
            Text(text = "Chargement des détails...", color = Color.Gray)
        }
    }
}