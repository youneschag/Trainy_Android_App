package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.uha.chaguer.android.ui.field.OutlinedIntFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedDateFieldWrapper
import fr.uha.chaguer.trainy.R

@Composable
fun SuccessRoutineScreen(
    routine: RoutineViewModel.UIState,
    send: (RoutineViewModel.UIEvent) -> Unit,
) {
    Column(
        modifier = Modifier
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