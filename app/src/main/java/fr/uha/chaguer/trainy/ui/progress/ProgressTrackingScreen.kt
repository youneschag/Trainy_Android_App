package fr.uha.chaguer.trainy.ui.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.database.DateUtils.formatDate
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.model.RoutineProgress
import fr.uha.chaguer.trainy.ui.routine.ListRoutinesViewModel
import fr.uha.chaguer.trainy.ui.routine.RoutineViewModel
import fr.uha.chaguer.trainy.ui.theme.MontserratFont
import kotlinx.coroutines.launch

@Destination<RootGraph>
@Composable
fun ProgressTrackingScreen(
    vm: ListRoutinesViewModel = hiltViewModel(),
    dm: RoutineViewModel = hiltViewModel(),
    pm: ProgressViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val routines by dm.getAllRoutines().collectAsStateWithLifecycle(emptyList())
    val fullRoutines by vm.getAllFullRoutines().collectAsStateWithLifecycle(emptyList())
    val currentIndex = remember { mutableIntStateOf(0) }
    val selectedRoutine = routines.getOrNull(currentIndex.intValue)
    val progressList by pm.getProgressForRoutine(selectedRoutine?.routineId ?: 0L)
        .collectAsStateWithLifecycle(emptyList())
    val errorDialogState = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.progress),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFont
            )
            Divider(
                color = Color(0xFF673AB7),
                thickness = 2.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            RoutineNavigator(
                routines = routines,
                currentIndex = currentIndex.intValue,
                onPrevious = { if (currentIndex.intValue > 0) currentIndex.intValue-- },
                onNext = { if (currentIndex.intValue < routines.size - 1) currentIndex.intValue++ }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val exercises =
                    fullRoutines.find { it.routine.routineId == selectedRoutine?.routineId }?.exercises
                        ?: emptyList()
                items(exercises) { exercise ->
                    val isExerciseCompleted = progressList.any {
                        it.exerciseId == exercise.exerciseId &&
                                it.completedRepetitions >= exercise.repetitions &&
                                it.completedDuration >= exercise.duration
                    }

                    val differenceMessage = calculateDifferenceMessage(
                        exercise = exercise,
                        progress = progressList.find { it.exerciseId == exercise.exerciseId }
                    )

                    Column {
                        ExerciseItemWithDoneButton(
                            exercise = exercise,
                            onSaveProgress = { repetition, duration ->
                                pm.viewModelScope.launch {
                                    val result = pm.saveProgressWithValidation(
                                        routineId = selectedRoutine!!.routineId,
                                        exerciseId = exercise.exerciseId,
                                        repetitions = repetition,
                                        duration = duration
                                    )
                                    if (!result.isSuccess) {
                                        errorMessage.value = result.message
                                        errorDialogState.value = true
                                    }
                                }
                            },
                            isCompleted = isExerciseCompleted,
                            differenceMessage = differenceMessage
                        )

                        val progressForExercise =
                            progressList.filter { it.exerciseId == exercise.exerciseId }
                        if (progressForExercise.isNotEmpty()) {
                            Text(
                                text = "Progrès réalisés :",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                            progressForExercise.forEach { progress ->
                                var exerciseName by remember { mutableStateOf("Chargement...") }

                                LaunchedEffect(progress.exerciseId) {
                                    exerciseName = pm.getExerciseNameById(progress.exerciseId)
                                }

                                Text(
                                    text = "$exerciseName : ${progress.completedRepetitions} répétitions, ${progress.completedDuration} minutes (${
                                        formatDate(
                                            progress.date
                                        )
                                    })",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (errorDialogState.value) {
        AlertDialog(
            onDismissRequest = { errorDialogState.value = false },
            confirmButton = {
                TextButton(onClick = { errorDialogState.value = false }) {
                    Text("OK")
                }
            },
            text = {
                Text(text = errorMessage.value)
            }
        )
    }
}

fun calculateDifferenceMessage(exercise: Exercise, progress: RoutineProgress?): String {
    val completedRepetitions = progress?.completedRepetitions ?: 0
    val completedDuration = progress?.completedDuration ?: 0

    val remainingRepetitions = (exercise.repetitions - completedRepetitions).coerceAtLeast(0)
    val remainingDuration = (exercise.duration - completedDuration).coerceAtLeast(0)

    return if (remainingRepetitions == 0 && remainingDuration == 0) {
        "Exercice terminé !"
    } else {
        "Il reste $remainingRepetitions répétitions et $remainingDuration minutes"
    }
}

@Composable
fun RoutineNavigator(
    routines: List<Routine>,
    currentIndex: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onPrevious,
            enabled = currentIndex > 0
        ) {
            Text("<")
        }

        Text(
            text = routines.getOrNull(currentIndex)?.name ?: "Aucune routine",
            style = MaterialTheme.typography.titleMedium
        )

        Button(
            onClick = onNext,
            enabled = currentIndex < routines.size - 1
        ) {
            Text(">")
        }
    }
}

@Composable
fun ExerciseItemWithDoneButton(
    exercise: Exercise,
    isCompleted: Boolean,
    onSaveProgress: (Int, Int) -> Unit,
    differenceMessage: String
) {
    var enteredRepetition by remember { mutableStateOf("") }
    var enteredDuration by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
            .background(
                if (isCompleted) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = enteredRepetition,
                onValueChange = { enteredRepetition = it },
                label = { Text("Répétitions réalisées") },
                enabled = !isCompleted,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = enteredDuration,
                onValueChange = { enteredDuration = it },
                label = { Text("Durée réalisée (min)") },
                enabled = !isCompleted,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val repetition = enteredRepetition.toIntOrNull() ?: 0
                val duration = enteredDuration.toIntOrNull() ?: 0
                onSaveProgress(repetition, duration)

                enteredRepetition = ""
                enteredDuration = ""
            },
            enabled = !isCompleted,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enregistrer")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = differenceMessage,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}