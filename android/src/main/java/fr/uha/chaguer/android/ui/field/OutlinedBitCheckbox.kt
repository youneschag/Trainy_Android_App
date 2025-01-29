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

private class CheckboxEntry (
    val label : String? = null,
    val icon: ImageVector? = null,
    val iconFocused: ImageVector? = null,
    val value : Int
)

@Suppress("KotlinConstantConditions")
@Composable
private fun InternalOutlinedCheckboxContent (
    onSelectedChanged: (Boolean, Int) -> Unit,
    entry : CheckboxEntry,
    modifier: Modifier = Modifier,
    selectedValue: Int,
) {
    val selected = ((entry.value and selectedValue) != 0)
    val background =
        if (entry.icon == null && entry.iconFocused != null && selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else Color.Transparent
    val contentColor =
        if (entry.icon == null && entry.iconFocused != null && selected) Color.White else MaterialTheme.colorScheme.onBackground

    Row (
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = { onSelectedChanged(selected, entry.value) }
            )
            .background(background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when {
            entry.icon != null && entry.iconFocused != null -> {
                IconToggleButton(
                    checked = selected,
                    onCheckedChange = { onSelectedChanged(selected, entry.value) }
                ) {
                    Icon(
                        painter = rememberVectorPainter(
                            if (selected) entry.iconFocused else entry.icon
                        ),
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            entry.icon != null && entry.iconFocused == null -> {
                Checkbox(
                    checked = selected,
                    onCheckedChange = { onSelectedChanged(selected, entry.value) }
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
                    modifier = Modifier.size(36.dp)
                )
            }

            entry.icon == null && entry.iconFocused == null -> {
                Checkbox(
                    checked = selected,
                    onCheckedChange = { onSelectedChanged(selected, entry.value) }
                )
            }
        }
        if (entry.label != null) {
            Text(
                text = entry.label,
                color = contentColor,
//                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
private fun InternalOutlinedCheckboxContent (
    entries : List<CheckboxEntry>,
    onChanged: (Boolean, Int) -> Unit,
    modifier : Modifier = Modifier,
    selected: Int,
    orientation : Orientation = Orientation.NO,
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
                entries.forEach { entry -> InternalOutlinedCheckboxContent(onChanged, entry, modifier, selected) }
            }
        Orientation.VERTICAL ->
            Column(modifier = modifier.padding(top = 0.dp)) {
                entries.forEach { entry -> InternalOutlinedCheckboxContent(onChanged, entry, modifier, selected) }
            }
        Orientation.GRID2 ->
            LazyVerticalGrid(
                modifier = Modifier.height(120.dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(entries) { item ->
                    InternalOutlinedCheckboxContent (onChanged, item, modifier = modifier.size(24.dp), selected)
                }
            }
        Orientation.GRID3 ->
            LazyVerticalGrid(
                modifier = Modifier.height(120.dp),
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(entries) { item ->
                    InternalOutlinedCheckboxContent (onChanged, item, modifier = modifier.size(24.dp), selected)
                }
            }
        else -> {}
    }
}

private fun toggle (how : Boolean, newValue: Int, previous: Int) : Int {
    return if (how) previous and newValue.inv() else previous or newValue
}

@Composable
private fun InternalOutlinedCheckboxGroup (
    value: Int,
    onChanged: (Int) -> Unit,
    modifier : Modifier = Modifier,
    orientation : Orientation = Orientation.NO,
    @StringRes labelId: Int? = null,
    @StringRes errorId : Int? = null,
    @StringRes hintId : Int? = null,
    itemValues : Array<Int>,
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
        val entries = itemValues.mapIndexed { index, item ->
            CheckboxEntry(
                label = itemLabels?.let { itemLabels[index] },
                icon = itemIcons?.let { itemIcons[index] },
                iconFocused = itemIconsFocused?.let { itemIconsFocused[index] },
                value = item
            )
        }
        InternalOutlinedCheckboxContent(
            selected = value,
            onChanged = { how : Boolean, newValue : Int -> onChanged(toggle (how, newValue, value)) },
            orientation = orientation,
            entries = entries,
        )
    }
}

@Composable
fun OutlinedCheckboxGroup (
    value : Int?,
    onValueChange : (Int) -> Unit,
    modifier : Modifier = Modifier,
    orientation : Orientation = Orientation.NO,
    @StringRes labelId : Int? = null,
    @StringRes errorId : Int? = null,
    @StringRes hintId : Int? = null,
    itemValues : Array<Int>,
    itemLabels : Array<String>? = null,
    itemIcons : Array<ImageVector>? = null,
    itemIconsFocused : Array<ImageVector>? = null
) {
    InternalOutlinedCheckboxGroup (
        value = value?:0,
        onChanged = onValueChange,
        modifier = modifier,
        orientation = orientation,
        errorId = errorId,
        labelId = labelId,
        hintId = hintId,
        itemValues = itemValues,
        itemLabels = itemLabels,
        itemIcons = itemIcons,
        itemIconsFocused = itemIconsFocused,
    )
}

@Composable
fun OutlinedBitCheckboxWrapper(
    field : FieldWrapper<Int>,
    onValueChange : (Int) -> Unit,
    modifier : Modifier = Modifier,
    orientation : Orientation = Orientation.NO,
    @StringRes labelId : Int? = null,
    @StringRes hintId : Int? = null,
    itemValues : Array<Int>,
    itemLabels : Array<String>? = null,
    itemIcons : Array<ImageVector>? = null,
    itemIconsFocused : Array<ImageVector>? = null
) {
    InternalOutlinedCheckboxGroup(
        value = field.value?:0,
        onChanged = onValueChange,
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
