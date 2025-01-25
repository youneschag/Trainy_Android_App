package fr.uha.chaguer.android.ui.field

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.uha.chaguer.android.R
import fr.uha.chaguer.android.ui.Converter
import java.time.temporal.ChronoUnit
import java.util.*

private fun buildDateResource (date : Date?) : Int? {
    if (date == null) return R.string.date_now
    val day = Converter.trimToMidnight(date)
    val today = Converter.trimToMidnight(Date())
    return when (ChronoUnit.DAYS.between(today.toInstant(), day.toInstant())) {
        -1L -> R.string.date_yesterday
        0L -> R.string.date_today
        1L -> R.string.date_tomorrow
        else -> null
    }
}

private fun dateValidator (chose : Long, daysBefore : Long?, daysAfter : Long? ) : Boolean {
    val today = Date()
    if (daysBefore != null) {
        val start = today.toInstant().minus(daysBefore, ChronoUnit.DAYS).toEpochMilli()
        if (chose < start) return false
    }
    if (daysAfter != null) {
        val end = today.toInstant().plus(daysAfter, ChronoUnit.DAYS).toEpochMilli()
        if (chose > end) return false
    }
    return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("SameParameterValue")
@Composable
private fun CustomDatePicker (
    titleId : Int?,
    value : Date,
    daysBefore : Long? = null,
    daysAfter : Long? = null,
    onSelect : (Date?) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = value.time
    )
    DatePickerDialog(
        onDismissRequest = { onSelect(null) },
        confirmButton = {
            TextButton(
                onClick = { onSelect(Date(datePickerState.selectedDateMillis ?: 0)) }
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
        DatePicker(
            state = datePickerState,
// seems move away
//            dateValidator = { timestamp -> dateValidator(timestamp, daysBefore, daysAfter) },
            title = { if (titleId != null) Text(text = stringResource (id = titleId) ) }
        )
    }
}

private fun changeDay (current : Date, amount : Int) : Date {
    val calendar = GregorianCalendar()
    calendar.time = current
    calendar.set(Calendar.MILLISECOND, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.add(Calendar.DAY_OF_YEAR, amount)
    return calendar.time
}

@Composable
private fun InternalDateContent(
    value : Date?,
    onValueChange : ((Date) -> Unit)?,
    modifier : Modifier = Modifier,
    paging : FieldPaging? = null,
) {
    val showDialog =  remember { mutableStateOf(false) }

    if (showDialog.value) {
        CustomDatePicker (
            titleId = R.string.title_day,
            value ?: Date(),
            null, null
        ) { showDialog.value = false; if (it != null) if (onValueChange != null) onValueChange(it) }
    }

    Row(modifier = modifier
        .clickable(enabled = onValueChange != null, onClick = { showDialog.value = true })
        .padding(top = 4.dp)
    ) {
        @StringRes val dayAsResource: Int? = buildDateResource(value)
        val dayAsText =
            if (dayAsResource != null) stringResource(id = dayAsResource) else Converter.convert(
                value
            )
        Text(dayAsText, modifier = Modifier.weight(1.0f))
        if (onValueChange != null) {
            if (hasPagingIcon(paging)) {
                BuildPagingIcons(paging = paging, { onValueChange(changeDay(value ?: Date(), it)) })
            }
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = "date picker"
            )
        }
    }
}

@Composable
fun OutlinedDateField(
    value : Date?,
    onValueChange : ((Date) -> Unit)?,
    modifier : Modifier = Modifier,
    paging : FieldPaging? = null,
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
        InternalDateContent(
            value = value,
            onValueChange = onValueChange,
            paging = paging,
        )
    }
}

@Composable
fun OutlinedDateFieldWrapper(
    field : FieldWrapper<Date>,
    onValueChange : ((Date) -> Unit)?,
    modifier : Modifier = Modifier,
    paging : FieldPaging? = null,
    @StringRes labelId: Int? = null,
    @StringRes hintId : Int? = null,
) {
    OutlinedDateField(
        value = field.value,
        onValueChange = onValueChange,
        modifier = modifier,
        paging = paging,
        labelId = labelId,
        errorId = field.errorId,
        hintId = hintId,
    )
}
