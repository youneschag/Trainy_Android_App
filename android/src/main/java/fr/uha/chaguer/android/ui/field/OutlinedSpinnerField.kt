package fr.uha.chaguer.android.ui.field

import androidx.annotation.StringRes
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fr.uha.chaguer.android.R
import fr.uha.chaguer.android.ui.Converter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedSpinnerField (
    value : String?,
    onValueChange : (String) -> Unit,
    modifier : Modifier = Modifier,
    @StringRes labelId : Int? = null,
    @StringRes errorId : Int? = null,
    @StringRes hintId : Int? = null,
    optionLabels : Array<String>,
    optionValues : Array<String>,
)
{
    val options = optionLabels zip optionValues
    val selected = options.firstNotNullOfOrNull { pair -> pair.takeIf { pair.second == value } }
    var selectedOption by remember { mutableStateOf(selected?: options.first()) }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        val supportingTextId : Int? = errorId ?: hintId
        OutlinedTextField(
            value = selectedOption.first,
            readOnly = true,
            onValueChange = { },
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            label = { if (labelId != null) Text (text = stringResource(labelId)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            supportingText = { if (supportingTextId != null) androidx.compose.material3.Text(
                stringResource(id = supportingTextId)
            )
            },
            isError = errorId != null,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { androidx.compose.material3.Text(option.first) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                        onValueChange(option.second)
                    },
                )
            }
        }
    }
}

@Composable
fun <T : Enum<T>> OutlinedEnumSpinnerField (
    value : T?,
    onValueChange : (String) -> Unit,
    modifier : Modifier = Modifier,
    @StringRes labelId : Int? = null,
    @StringRes errorId : Int? = null,
    @StringRes hintId : Int? = null,
    optionValues : Array<T>,
    optionLabels : Array<String>? = null,
) {
    OutlinedSpinnerField(
        value = value?.name ?: "",
        onValueChange = onValueChange,
        modifier = modifier,
        labelId = labelId,
        errorId = errorId,
        hintId = hintId,
        optionLabels = optionLabels ?: optionValues.map { it.name.lowercase().replace('_', ' ') }.toTypedArray(),
        optionValues = optionValues.map { it.name }.toTypedArray(),
    )
}

@Composable
fun <T : Enum<T>> OutlinedEnumSpinnerFieldWrapper (
    field : FieldWrapper<T>,
    onValueChange : (String) -> Unit,
    modifier : Modifier = Modifier,
    @StringRes labelId : Int? = null,
    @StringRes hintId : Int? = null,
    optionValues : Array<T>,
    optionLabels : Array<String>? = null,
) {
    OutlinedEnumSpinnerField(
        value = field.value,
        onValueChange = onValueChange,
        modifier = modifier,
        labelId = labelId,
        errorId = field.errorId,
        hintId = hintId,
        optionLabels = optionLabels,
        optionValues = optionValues,
    )
}

@Composable
fun OutlinedStringSpinnerFieldWrapper (
    field : FieldWrapper<String>,
    onValueChange : (String) -> Unit,
    modifier : Modifier = Modifier,
    @StringRes labelId : Int? = null,
    @StringRes hintId : Int? = null,
    optionLabels : Array<String>,
    optionValues : Array<String>,
) {
    OutlinedSpinnerField(
        value = field.value,
        onValueChange = onValueChange,
        modifier = modifier,
        labelId = labelId,
        errorId = field.errorId,
        hintId = hintId,
        optionLabels = optionLabels,
        optionValues = optionValues,
    )
}

@Composable
fun OutlinedIntSpinnerFieldWrapper (
    field : FieldWrapper<Int>,
    onValueChange : (Int) -> Unit,
    modifier : Modifier = Modifier,
    @StringRes labelId : Int? = null,
    @StringRes hintId : Int? = null,
    optionLabels : Array<String>,
    optionValues : Array<String>,
) {
    OutlinedSpinnerField(
        value = field.value.toString(),
        onValueChange = { val v : Int? = Converter.safeStringToInt(it); if (v != null) onValueChange(v) },
        modifier = modifier,
        labelId = labelId,
        errorId = field.errorId,
        hintId = hintId,
        optionLabels = optionLabels,
        optionValues = optionValues,
    )
}

@Preview(showBackground = true)
@Composable
fun OutlinedSpinnerFieldPreview() {
    OutlinedSpinnerField (
        value = "1",
        onValueChange = { },
        labelId = R.string.duration,
        optionLabels = arrayOf("0", "1", "3", "5"),
        optionValues = arrayOf("0", "1", "3", "5"),
        errorId = null
    )
}