package fr.uha.chaguer.android.ui.app

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun AppTitle (
    @StringRes appNameId: Int? = null,
    @StringRes screenTitleId: Int? = null,
    documentName: String? = null,
    isModified : Boolean? = null,
    color: Color = MaterialTheme.colorScheme.inverseSurface,
) {
    Row {
        if (appNameId != null) {
            Text(stringResource(id = appNameId), color = color)
            Text("-", Modifier.padding(horizontal = 8.dp), color = color)
        }
        if (screenTitleId != null) {
            Text(stringResource(id = screenTitleId), color = color)
        }
        if (documentName != null) {
            Text("-", Modifier.padding(horizontal = 8.dp), color = color)
            Text(documentName, color = color)
        }
        if (isModified != null && isModified) {
            Text(text = "*", Modifier.padding(horizontal = 8.dp), color = color)
        }
    }
}