package fr.uha.chaguer.android.ui.field

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import fr.uha.chaguer.android.model.DynamicText

@Composable
private fun buildLabel (value : DynamicText?) : String
{
    return when {
        value == null -> "(null)"
        value.label == null && value.labelId == null -> ""
        value.label != null && value.labelId == null -> value.label!!
        value.label == null && value.labelId != null -> stringResource(id = value.labelId)
        value.label != null && value.labelId != null -> "both"
        else -> "strange"
    }
}

@Composable
fun Text(
    value: DynamicText?,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
    hidden: Boolean = false,
) {
    if (hidden) {
        Text(
            text = buildLabel(value),
            modifier = modifier,
            color = Color.Gray,
            fontSize = fontSize,
            textDecoration = TextDecoration.LineThrough,
        )
    } else {
        Text(
            text = buildLabel(value),
            modifier = modifier,
            fontSize = fontSize,
        )
    }
}

@Composable
fun OutlinedDynamicTextField(
    value : DynamicText?,
    onValueChange : (String) -> Unit,
    modifier : Modifier = Modifier,
    keyboardType : KeyboardType = KeyboardType.Text,
    @StringRes labelId : Int? = null,
    @StringRes errorId : Int? = null,
    @StringRes hintId : Int? = null,
) {
    val supportingTextId : Int? = errorId ?: hintId
    OutlinedTextField(
        value = buildLabel(value),
        onValueChange = onValueChange,
        modifier = modifier,
        label = { if (labelId != null) Text(text = stringResource(id = labelId)) },
        supportingText = { if (supportingTextId != null) Text(stringResource(id = supportingTextId)) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = errorId != null,
    )
}

@Composable
fun OutlinedDynamicTextFieldWrapper(
    field : FieldWrapper<DynamicText>,
    onValueChange : (String) -> Unit,
    modifier : Modifier = Modifier,
    keyboardType : KeyboardType = KeyboardType.Text,
    @StringRes labelId : Int? = null,
    @StringRes hintId : Int? = null,
) {
    OutlinedDynamicTextField(
        value = field.value,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardType = keyboardType,
        labelId = labelId,
        errorId = field.errorId,
        hintId = hintId,
    )
}
