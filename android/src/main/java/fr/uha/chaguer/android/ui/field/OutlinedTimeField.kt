package fr.uha.chaguer.android.ui.field

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.uha.chaguer.android.R
import fr.uha.chaguer.android.ui.Converter
import java.util.*

data class Time (
    val reference : Date = Date(),
    val hour : Int = 0,
    val minute : Int = 0,
)

@OptIn(ExperimentalMaterial3Api::class)
private fun timeValidator (
    chose : TimePickerState,
    hoursAllowed : Array<Int>?,
    minutesAllowed : Array<Int>?,
) : Boolean {
    if (hoursAllowed != null) {
        if (! hoursAllowed.contains(chose.hour))
            return false
    }
    if (minutesAllowed != null) {
        if (! minutesAllowed.contains(chose.minute))
            return false
    }
    return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("SameParameterValue")
@Composable
private fun CustomTimePicker (
    value : Time,
    onSelect : (Time?) -> Unit,
    hoursAllowed : Array<Int>?,
    minutesAllowed : Array<Int>?,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = value.hour,
        initialMinute = value.minute,
        is24Hour = true,
    )
    val isEnabled = remember { timeValidator(timePickerState, hoursAllowed, minutesAllowed) }

    DatePickerDialog(
        onDismissRequest = { onSelect(null) },
        confirmButton = {
            TextButton(
                onClick = { onSelect(Time(value.reference, timePickerState.hour, timePickerState.minute)) },
                enabled = isEnabled
            ) {
                Text(stringResource (id = R.string.valid))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onSelect(null) }
            ) {
                Text(stringResource (id = R.string.cancel))
            }
        }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}

@Composable
private fun InternalTimeContent(
    value : Time?,
    onValueChange : (Time) -> Unit,
    timeConverter : (Time?) -> String,
    modifier : Modifier = Modifier,
) {
    val showDialog =  remember { mutableStateOf(false) }

    if (showDialog.value) {
        CustomTimePicker (
            value ?: Time(),
            onSelect = { showDialog.value = false; if (it != null) onValueChange(it) },
            hoursAllowed = null,
            minutesAllowed = null,
        )
    }

    Row(modifier = modifier
        .clickable(onClick = { showDialog.value = true })
        .padding(top = 4.dp)
    ) {
        val timeAsText = timeConverter(value)
        Text(timeAsText, modifier = Modifier.weight(1.0f))
        Icon (
            imageVector = Icons.Outlined.Timelapse,
            contentDescription = "time picker"
        )
    }
}

@Composable
fun OutlinedTimeField(
    value : Time?,
    onValueChange : (Time) -> Unit,
    modifier : Modifier = Modifier,
    timeConverter : (Time?) -> String = { Converter.convert(it) },
    @StringRes labelId: Int? = null,
    @StringRes errorId : Int? = null,
    @StringRes hintId : Int? = null,
) {
    OutlinedDecorator (
        modifier = modifier,
        labelId = labelId,
        errorId = errorId,
        hintId = hintId,
    ) {
        InternalTimeContent(
            value = value,
            onValueChange = onValueChange,
            timeConverter = timeConverter,
        )
    }
}

@Composable
fun OutlinedTimeFieldWrapper(
    field : FieldWrapper<Time>,
    onValueChange : (Time) -> Unit,
    modifier : Modifier = Modifier,
    timeConverter : (Time?) -> String = { Converter.convert(it) },
    @StringRes labelId: Int? = null,
    @StringRes hintId : Int? = null,
) {
    OutlinedTimeField(
        value = field.value,
        onValueChange = onValueChange,
        modifier = modifier,
        timeConverter = timeConverter,
        labelId = labelId,
        errorId = field.errorId,
        hintId = hintId,
    )
}
