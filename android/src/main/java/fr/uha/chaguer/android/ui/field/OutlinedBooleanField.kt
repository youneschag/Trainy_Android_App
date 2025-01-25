package fr.uha.chaguer.android.ui.field

import androidx.annotation.StringRes
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OutlinedBooleanField(
    value: Boolean?,
    onValueChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    @StringRes labelId: Int? = null,
    @StringRes errorId: Int? = null,
    @StringRes hintId: Int? = null,
) {
    OutlinedDecorator(
        modifier = modifier,
        labelId = labelId,
        errorId = errorId,
        hintId = hintId,
    ) {
        Switch(
            checked = value ?: false,
            onCheckedChange = onValueChange,
            modifier = Modifier,
        )
    }
}

@Composable
fun OutlinedBooleanFieldWrapper(
    field: FieldWrapper<Boolean>,
    onValueChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    @StringRes labelId: Int? = null,
    @StringRes hintId: Int? = null,
) {
    OutlinedBooleanField(
        value = field.value,
        onValueChange = onValueChange,
        modifier = modifier,
        labelId = labelId,
        errorId = field.errorId,
        hintId = hintId,
    )
}