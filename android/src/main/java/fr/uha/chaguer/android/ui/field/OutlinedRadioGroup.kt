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
import androidx.compose.ui.unit.dp

private class RadioButtonEntry (
    val value : String,
    val label : String? = null,
    val icon: ImageVector? = null,
    val iconFocused: ImageVector? = null,
)

@Suppress("KotlinConstantConditions")
@Composable
private fun InternalRadioButtonContent (
    onSelectedChanged: (String) -> Unit,
    entry : RadioButtonEntry,
    modifier: Modifier = Modifier,
    selectedValue: String? = null,
) {
    val selected = entry.value == selectedValue
    val background =
        if (entry.icon == null && entry.iconFocused != null && selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else Color.Transparent
    val contentColor =
        if (entry.icon == null && entry.iconFocused != null && selected) Color.White else MaterialTheme.colorScheme.onBackground

    Row (
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = { onSelectedChanged(entry.value) }
            )
            .background(background),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when {
            entry.icon != null && entry.iconFocused != null -> {
                IconToggleButton(
                    checked = selected,
                    onCheckedChange = { onSelectedChanged(entry.value) }
                ) {
                    Icon(
                        painter = rememberVectorPainter(
                            if (selected) entry.iconFocused else entry.icon
                        ),
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            entry.icon != null && entry.iconFocused == null -> {
                RadioButton(
                    selected = selected,
                    onClick = { onSelectedChanged(entry.value) }
                )
                Icon(
                    painter = rememberVectorPainter(entry.icon),
                    contentDescription = null,
                    tint = contentColor
                )
            }
            entry.icon == null && entry.iconFocused != null -> {
                Icon(
                    painter = rememberVectorPainter(entry.iconFocused),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(48.dp)
                )
            }
            entry.icon == null && entry.iconFocused == null -> {
                RadioButton(
                    selected = selected,
                    onClick = { onSelectedChanged(entry.value) }
                )
            }
        }
        Text(
            text = entry.label ?: entry.value.lowercase().replace('_', ' '),
            color = contentColor,
            modifier = Modifier.padding(horizontal = 0.dp)
        )
    }

}

@Composable
private fun InternalRadioGroupContent (
    selected: String?,
    onChanged: (String) -> Unit,
    modifier : Modifier = Modifier,
    orientation : Orientation = Orientation.NO,
    entries : List<RadioButtonEntry>,
) {
    val finalOrientation = when(orientation) {
        Orientation.NO -> orientation
        Orientation.HORIZONTAL -> orientation
        Orientation.VERTICAL -> orientation
        Orientation.OPTIMUM -> Orientation.VERTICAL
        Orientation.GRID2 -> orientation
        Orientation.GRID3 -> orientation
    }
    when(finalOrientation) {
        Orientation.HORIZONTAL ->
            Row(modifier = modifier.padding(top = 4.dp)) {
                entries.forEach { entry -> InternalRadioButtonContent (onChanged, entry, modifier, selected) }
            }
        Orientation.VERTICAL ->
            Column(modifier = modifier.padding(top = 0.dp, bottom = 0.dp)) {
                entries.forEach { entry -> InternalRadioButtonContent (onChanged, entry, modifier, selected) }
            }
        Orientation.GRID2 ->
            LazyVerticalGrid(
                modifier = Modifier.height(120.dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(entries) { item ->
                    InternalRadioButtonContent (onChanged, item, modifier = modifier.size(24.dp), selected)
                }
            }
        Orientation.GRID3 ->
            LazyVerticalGrid(
                modifier = Modifier.height(90.dp),
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(entries) { item ->
                    InternalRadioButtonContent (onChanged, item, modifier = modifier.size(24.dp), selected)
                }
            }
        else -> {}
    }
}

@Composable
fun OutlinedRadioGroup (
    selected: String?,
    onChanged: (String) -> Unit,
    modifier : Modifier = Modifier,
    orientation : Orientation = Orientation.NO,
    @StringRes labelId: Int? = null,
    @StringRes errorId : Int? = null,
    @StringRes hintId : Int? = null,
    itemValues : Array<String>,
    itemLabels : Array<String>? = null,
    itemIcons : Array<ImageVector>? = null,
    itemIconsFocused : Array<ImageVector>? = null
) {
    OutlinedDecorator(
        modifier = modifier,
        labelId = labelId,
        errorId = errorId,
        hintId = hintId,
    ) {
        val entries = itemValues.mapIndexed { index, item -> RadioButtonEntry(
            label = itemLabels?.let { itemLabels[index] },
            icon = itemIcons?.let { itemIcons[index] },
            iconFocused = itemIconsFocused?.let { itemIconsFocused[index] },
            value = item
        )
        }
        InternalRadioGroupContent(
            selected = selected,
            onChanged = onChanged,
            orientation = orientation,
            entries = entries,
        )
    }
}

@Composable
fun <T : Enum<T>> OutlinedEnumRadioGroup(
    value : T?,
    onValueChange : (String) -> Unit,
    modifier : Modifier = Modifier,
    orientation : Orientation = Orientation.NO,
    @StringRes labelId : Int? = null,
    @StringRes errorId : Int? = null,
    @StringRes hintId : Int? = null,
    itemValues : Array<T>,
    itemLabels : Array<String>? = null,
    itemIcons : Array<ImageVector>? = null,
    itemIconsFocused : Array<ImageVector>? = null
) {
    OutlinedRadioGroup (
        onChanged = onValueChange,
        modifier = modifier,
        selected = value?.name,
        orientation = orientation,
        errorId = errorId,
        labelId = labelId,
        hintId = hintId,
        itemValues = itemValues.map { it.name }.toTypedArray(),
        itemLabels = itemLabels,
        itemIcons = itemIcons,
        itemIconsFocused = itemIconsFocused,
    )
}

@Composable
fun <T : Enum<T>> OutlinedEnumRadioGroupWrapper(
    field : FieldWrapper<T>,
    onValueChange : (String) -> Unit,
    modifier : Modifier = Modifier,
    orientation : Orientation = Orientation.NO,
    @StringRes labelId : Int? = null,
    @StringRes hintId : Int? = null,
    itemValues : Array<T>,
    itemLabels : Array<String>? = null,
    itemIcons : Array<ImageVector>? = null,
    itemIconsFocused : Array<ImageVector>? = null
) {
    OutlinedEnumRadioGroup(
        value = field.value,
        onValueChange = onValueChange,
        modifier = modifier,
        orientation = orientation,
        labelId = labelId,
        errorId = field.errorId,
        hintId = hintId,
        itemValues = itemValues,
        itemLabels = itemLabels,
        itemIcons = itemIcons,
        itemIconsFocused = itemIconsFocused,
    )
}
