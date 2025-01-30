package fr.uha.chaguer.trainy.ui.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.uha.chaguer.android.ui.field.OutlinedIntFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.chaguer.trainy.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessExerciseScreen(
    exercise: ExerciseViewModel.UIState,
    send: (ExerciseViewModel.UIEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exercice", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF673AB7)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextFieldWrapper(
                field = exercise.nameState,
                onValueChange = { send(ExerciseViewModel.UIEvent.NameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.exercise_name,
            )

            OutlinedTextFieldWrapper(
                field = exercise.descriptionState,
                onValueChange = { send(ExerciseViewModel.UIEvent.DescriptionChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.exercise_description,
            )

            OutlinedIntFieldWrapper(
                field = exercise.durationState,
                onValueChange = { send(ExerciseViewModel.UIEvent.DurationChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.exercise_duration,
            )

            OutlinedIntFieldWrapper(
                field = exercise.repetitionsState,
                onValueChange = { send(ExerciseViewModel.UIEvent.RepetitionsChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.exercise_repetitions,
            )
        }
    }
}