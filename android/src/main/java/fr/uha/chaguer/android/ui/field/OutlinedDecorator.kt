package fr.uha.chaguer.android.ui.field

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedDecorator(
    modifier : Modifier = Modifier,
    @StringRes labelId: Int? = null,
    @StringRes errorId : Int? = null,
    @StringRes hintId : Int? = null,
    content : @Composable () -> Unit,
) {
    Column (
        modifier = modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground), MaterialTheme.shapes.extraSmall)
            .padding(start = 16.dp),
    ) {
        val color = if (errorId == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        val supportingTextId : Int? = errorId ?: hintId
        if (labelId != null) {
            Text(
                text = stringResource(id = labelId),
                color = color,
                style = MaterialTheme.typography.bodySmall
            )
        }
        content()
        if (supportingTextId != null){
            Text(
                text = stringResource(id = supportingTextId),
                color = color,
                style = MaterialTheme.typography.bodySmall            )
        }
    }
}
