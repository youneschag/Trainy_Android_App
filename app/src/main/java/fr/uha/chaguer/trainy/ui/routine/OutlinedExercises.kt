package fr.uha.chaguer.trainy.ui.routine

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.uha.chaguer.android.R
import fr.uha.chaguer.android.ui.field.FieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedDecorator
import fr.uha.chaguer.trainy.model.Exercise

@Composable
fun OutlinedExercisesField(
    value: List<Exercise>,
    onAddExercise: (Exercise) -> Unit,
    onRemoveExercise: (Exercise) -> Unit,
    modifier: Modifier,
    @StringRes labelId: Int?,
    errorId: Int?
) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        ExercisePicker(
            titleId = R.string.select_exercise,
        ) { selectedExercise ->
            showDialog.value = false
            if (selectedExercise != null) {
                onAddExercise(selectedExercise)
            }
        }
    }

    OutlinedDecorator(
        modifier = modifier,
        labelId = labelId,
        errorId = errorId,
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDialog.value = true },
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Ajouter un exercice")
                }
            }
        ) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(
                    items = value,
                    key = { exercise -> exercise.exerciseId }
                ) { exercise ->
                    Row(
                        modifier = Modifier.clickable(
                            onClick = { onRemoveExercise(exercise) }
                        )
                    ){
                        RoutineExercise(exercise)
                    }
                }
            }
        }
    }
}

@Composable
fun OutlinedExercisesFieldWrapper(
    field: FieldWrapper<List<Exercise>>,
    onAddExercise: (Exercise) -> Unit,
    onRemoveExercise: (Exercise) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes labelId: Int? = null,
) {
    OutlinedExercisesField(
        value = field.value ?: emptyList(),
        onAddExercise = onAddExercise,
        onRemoveExercise = onRemoveExercise,
        modifier = modifier,
        labelId = labelId,
        errorId = field.errorId,
    )
}