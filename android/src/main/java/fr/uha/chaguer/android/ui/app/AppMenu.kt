package fr.uha.chaguer.android.ui.app

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

sealed class AppMenuEntry (open val key : String) {
    @Suppress("unused")
    data class OverflowEntry (
        val entry : String? = null,
        @StringRes val titleId: Int,
        val enabled: () -> Boolean = { true },
        val listener : () -> Unit
    ) : AppMenuEntry (entry?:titleId.toString())
    @Suppress("unused")
    data class ActionEntry (
        val entry : String? = null,
        @StringRes val titleId: Int,
        val icon: ImageVector?,
        val enabled: () -> Boolean = { true },
        val listener : () -> Unit
    ) : AppMenuEntry (entry?:titleId.toString())
}

@Composable
fun AppMenu (entries : List<AppMenuEntry>) {
    var showMenu by remember { mutableStateOf(false) }
    Row (modifier = Modifier){
        entries.filterIsInstance<AppMenuEntry.ActionEntry>().forEach { entry ->
            ActionMenuItem(entry.titleId, entry.icon, entry.enabled()) { entry.listener() }
        }
        if (entries.filterIsInstance<AppMenuEntry.OverflowEntry>().isNotEmpty()) {
            ActionMenuItem("more", Icons.Filled.MoreVert, true) { showMenu = !showMenu }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                entries.filterIsInstance<AppMenuEntry.OverflowEntry>().forEach { entry ->
                    OverflowMenuItem(entry.titleId, enabled = entry.enabled()) { showMenu = false; entry.listener() }
                }
            }
        }
    }
}

@Suppress("unused")
@Composable
private fun OverflowMenuItem (
    @StringRes title : Int,
    color: Color = MaterialTheme.colorScheme.inverseSurface,
    enabled : Boolean = true,
    listener : () -> Unit,
) {
    DropdownMenuItem(
        text = { Text(text = stringResource(id = title)) }, //, color = color) },
        onClick = listener,
        enabled = enabled
    )
}

@Composable
private fun ActionMenuItem (
    @StringRes titleId : Int,
    icon : ImageVector?,
    enabled : Boolean = true,
    listener : () -> Unit,
) {
    IconButton(onClick = listener, enabled = enabled) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = titleId),
            )
        } else {
            Text (stringResource(id = titleId))
        }
    }
}

@Suppress("SameParameterValue")
@Composable
private fun ActionMenuItem (
    title : String,
    icon : ImageVector,
    enabled : Boolean = true,
    listener : () -> Unit,
) {
    IconButton(onClick = listener, enabled = enabled) {
        Icon(
            imageVector = icon,
            contentDescription = title,
        )
    }
}