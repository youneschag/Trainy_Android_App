package fr.uha.chaguer.android.ui.field

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

private class CheckboxEntry(
    val label: String? = null,
    val icon: ImageVector? = null,
    val iconFocused: ImageVector? = null,
    val value: Int
)

@Composable
private fun InternalCheckboxContent(
    onSelectedChanged: (Boolean, Int) -> Unit,
    entry: CheckboxEntry,
    modifier: Modifier = Modifier,
    selectedValue: Int
) {
    val selected = (entry.value and selectedValue) != 0
    val background = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent

    Row(
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = { onSelectedChanged(selected, entry.value) }
            )
            .background(background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (entry.icon != null) {
            Icon(
                painter = rememberVectorPainter(entry.icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
        entry.label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun InternalCheckboxGroup(
    entries: List<CheckboxEntry>,
    onChanged: (Boolean, Int) -> Unit,
    modifier: Modifier = Modifier,
    selected: Int,
    orientation: Orientation = Orientation.VERTICAL
) {
    when (orientation) {
        Orientation.HORIZONTAL -> Row(modifier = modifier) {
            entries.forEach { entry -> InternalCheckboxContent(onChanged, entry, Modifier.padding(8.dp), selected) }
        }
        Orientation.VERTICAL -> Column(modifier = modifier) {
            entries.forEach { entry -> InternalCheckboxContent(onChanged, entry, Modifier.padding(8.dp), selected) }
        }
        Orientation.GRID2 -> LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = modifier) {
            items(entries) { entry ->
                InternalCheckboxContent(onChanged, entry, Modifier.padding(8.dp), selected)
            }
        }
        Orientation.GRID3 -> LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = modifier) {
            items(entries) { entry ->
                InternalCheckboxContent(onChanged, entry, Modifier.padding(8.dp), selected)
            }
        }
        else -> {}
    }
}

@Composable
fun OutlinedCheckboxGroup(
    value: Int?,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.VERTICAL,
    @StringRes labelId: Int? = null,
    itemValues: Array<Int>,
    itemLabels: Array<String>? = null,
    itemIcons: Array<ImageVector>? = null
) {
    val entries = itemValues.mapIndexed { index, item ->
        CheckboxEntry(
            label = itemLabels?.getOrNull(index),
            icon = itemIcons?.getOrNull(index),
            value = item
        )
    }
    Column(modifier = modifier.padding(8.dp)) {
        labelId?.let {
            Text(
                text = stringResource(id = it),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        InternalCheckboxGroup(entries, { isSelected, newValue ->
            val updatedValue = if (isSelected) value ?: 0 and newValue.inv() else value ?: 0 or newValue
            onValueChange(updatedValue)
        }, selected = value ?: 0, orientation = orientation)
    }
}

@Composable
fun OutlinedBitCheckboxWrapper(
    field: FieldWrapper<Int>,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.VERTICAL,
    @StringRes labelId: Int? = null,
    itemValues: Array<Int>,
    itemLabels: Array<String>? = null,
    itemIcons: Array<ImageVector>? = null
) {
    OutlinedCheckboxGroup(
        value = field.value,
        onValueChange = onValueChange,
        modifier = modifier,
        orientation = orientation,
        labelId = labelId,
        itemValues = itemValues,
        itemLabels = itemLabels,
        itemIcons = itemIcons
    )
}