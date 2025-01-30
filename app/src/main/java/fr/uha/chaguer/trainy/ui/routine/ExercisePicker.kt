package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.chaguer.android.ui.app.AppTitle
import fr.uha.chaguer.trainy.database.ExerciseDao
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.ui.routine.RoutineExercise
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ExercisePickerViewModel @Inject constructor(private val dao: ExerciseDao) : ViewModel() {
    val exercises: Flow<List<Exercise>> = dao.getAllExercises()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisePicker(
    vm: ExercisePickerViewModel = hiltViewModel(),
    titleId: Int,
    onSelect: (Exercise?) -> Unit
) {
    val exercises = vm.exercises.collectAsStateWithLifecycle(initialValue = emptyList())
    Dialog(onDismissRequest = { onSelect(null) }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { AppTitle(screenTitleId = titleId) }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(
                    items = exercises.value,
                    key = { exercise -> exercise.exerciseId }
                ) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { onSelect(item) })
                            .padding(8.dp)
                            .background(Color.White)
                    ) {
                        RoutineExercise(item)
                    }
                }
            }
        }
    }
}