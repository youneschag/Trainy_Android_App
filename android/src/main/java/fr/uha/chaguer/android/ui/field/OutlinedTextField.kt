package fr.uha.chaguer.android.ui.field

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun OutlinedTextField(
    value : String?,
    onValueChange : ((String) -> Unit)?,
    modifier : Modifier = Modifier,
    keyboardType : KeyboardType = KeyboardType.Text,
    @StringRes labelId : Int? = null,
    @StringRes errorId : Int? = null,
    @StringRes hintId : Int? = null,
) {
    val supportingTextId : Int? = errorId ?: hintId
    OutlinedTextField(
        value = value ?: "",
        onValueChange = onValueChange ?: { },
        modifier = modifier,
        enabled = onValueChange != null,
        label = { if (labelId != null) Text(text = stringResource(id = labelId)) },
        supportingText = { if (supportingTextId != null) Text(stringResource(id = supportingTextId)) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = errorId != null,
    )
}

@Composable
fun OutlinedTextFieldWrapper(
    field: FieldWrapper<String>,
    onValueChange: ((String) -> Unit)?,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    @StringRes labelId: Int? = null,
    @StringRes hintId: Int? = null,
) {
    OutlinedTextField(
        value = field.value,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardType = keyboardType,
        labelId = labelId,
        errorId = field.errorId,
        hintId = hintId,
    )
}

