package fr.uha.chaguer.android.ui.field

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import fr.uha.chaguer.android.ui.Converter.Companion.safeStringToInt

@Composable
fun OutlinedIntField(
    value : Int?,
    onValueChange : ((Int) -> Unit)?,
    modifier : Modifier = Modifier,
    paging : FieldPaging? = null,
    @StringRes labelId : Int? = null,
    @StringRes errorId : Int? = null,
    @StringRes hintId : Int? = null,
) {
    val supportingTextId : Int? = errorId ?: hintId

    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { if (onValueChange != null) { val v : Int? = safeStringToInt(it); if (v != null) onValueChange(v) } },
        modifier = modifier,
        enabled = onValueChange != null,
        label = { if (labelId != null) Text (text = stringResource(labelId)) },
        leadingIcon = if (onValueChange != null && hasLeadingIcons(paging)) {
            { BuildLeadingIcons(paging, { onValueChange((value ?: 0) + it) }) }
        } else null,
        trailingIcon = if (onValueChange != null && hasTrailingIcon(paging)) {
            { BuildTrailingIcons(paging, { onValueChange((value ?: 0) + it) }) }
        } else null,
        supportingText = { if (supportingTextId != null) Text(stringResource(id = supportingTextId)) },
        isError = errorId != null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun OutlinedIntFieldWrapper(
    field: FieldWrapper<Int>,
    onValueChange: ((Int) -> Unit)?,
    modifier: Modifier = Modifier,
    paging: FieldPaging? = null,
    @StringRes labelId: Int? = null,
    @StringRes hintId: Int? = null,
) {
    OutlinedIntField(
        value = field.value,
        onValueChange = onValueChange,
        modifier = modifier,
        paging = paging,
        labelId = labelId,
        errorId = field.errorId,
        hintId = hintId,
    )
}
