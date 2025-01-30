package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import fr.uha.chaguer.android.ui.field.OutlinedDateFieldWrapper
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.ui.routine.OutlinedExercisesFieldWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessRoutineScreen(
    routine: RoutineViewModel.UIState,
    send: (RoutineViewModel.UIEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(" Routine", color = Color.White) },
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
                    containerColor = Color(0xFF673AB7) // Violet
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
                field = routine.nameState,
                onValueChange = { send(RoutineViewModel.UIEvent.NameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.routine_name,
            )

            OutlinedDateFieldWrapper(
                field = routine.dayState,
                onValueChange = { send(RoutineViewModel.UIEvent.StartDayChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.routine_start_day,
            )

            OutlinedIntFieldWrapper(
                field = routine.frequencyState,
                onValueChange = { send(RoutineViewModel.UIEvent.FrequencyChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.routine_frequency,
            )

            OutlinedTextFieldWrapper(
                field = routine.objectiveState,
                onValueChange = { send(RoutineViewModel.UIEvent.ObjectiveChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.routine_objective,
            )
        }
    }
}