package fr.uha.chaguer.android.ui.field

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

@Suppress("unused")
class DisplayMode private constructor (
    private val mode : Int,
    val columns: Int
) {
    private fun hasMask (mask : Int, onLine : Int) : Boolean {
        return (mode and (mask shl (onLine * 8))) != 0
    }

    fun rows (onLine: Int) : Int {
        var n = 0
        val v = mode shr (onLine * 8)
        if ((v and BY_NAME) != 0) ++n
        if ((v and FILTER_WITH_SPINNER) != 0) ++n
        if ((v and FILTER_WITH_RADIOBUTTON) != 0) ++n
        if ((v and SORT_WITH_SPINNER) != 0) ++n
        if ((v and SORT_WITH_RADIOBUTTON) != 0) ++n
        if ((v and SORT_ORDER) != 0) ++n
        return n
    }

    fun hasName (onLine : Int) : Boolean {
        return hasMask(BY_NAME, onLine)
    }

    fun hasSpinnerFilter (onLine : Int) : Boolean {
        return hasMask(FILTER_WITH_SPINNER, onLine)
    }

    fun hasRadioButtonFilter (onLine : Int) : Boolean {
        return hasMask(FILTER_WITH_RADIOBUTTON, onLine)
    }

    fun hasSpinnerSort (onLine : Int) : Boolean {
        return hasMask(SORT_WITH_SPINNER, onLine)
    }

    fun hasRadioButtonSort (onLine : Int) : Boolean {
        return hasMask(SORT_WITH_RADIOBUTTON, onLine)
    }

    fun hasOrder (onLine : Int) : Boolean {
        return hasMask(SORT_ORDER, onLine)
    }

    @Suppress("unused")
    class Builder {

        private var mode : Int = 0
        private var line : Int = -1

        private fun addMask (mask : Int, onLine : Int) {
            mode = mode or (mask shl (onLine * 8))
        }

        fun withName(): Builder {
            addMask(BY_NAME, line)
            return this
        }

        fun withSpinnerFilter(): Builder {
            addMask(FILTER_WITH_SPINNER, line)
            return this
        }

        fun withRadioButtonFilter(): Builder {
            addMask(FILTER_WITH_RADIOBUTTON, line)
            return this
        }

        fun withSpinnerSort(): Builder {
            addMask(SORT_WITH_SPINNER, line)
            return this
        }

        fun withRadioButtonSort(): Builder {
            addMask(SORT_WITH_RADIOBUTTON, line)
            return this
        }

        fun withOrder(): Builder {
            addMask(SORT_ORDER, line)
            return this
        }

        fun onLine(): Builder {
            ++line
            return this
        }

        fun build () : DisplayMode {
            return DisplayMode(mode, line+1)
        }

    }

    companion object {
        const val BY_NAME : Int                 = 0x001
        const val FILTER_WITH_SPINNER : Int     = 0x002
        const val FILTER_WITH_RADIOBUTTON : Int = 0x004
        const val SORT_WITH_SPINNER : Int       = 0x008
        const val SORT_WITH_RADIOBUTTON : Int   = 0x010
        const val SORT_ORDER : Int              = 0x020
    }
}

@Suppress("ArrayInDataClass")
data class QueryProperties (
    val mode : DisplayMode,
    val labelId : Int? = null,
    val filterItemValues : Array<String>? = null,
    val filterItemLabels : Array<String>? = null,
    val sortItemValues : Array<String>? = null,
    val sortItemLabels : Array<String>? = null,
    val orderItemValues : Array<String>? = null,
)

data class QueryValue (
    val name : String = "",
    val filter: String? = null,
    val sort: String? = null,
    val order: String? = null,
)

class QueryBuilder {

    private val _nameState = MutableStateFlow("")
    private val _filterState = MutableStateFlow("")
    private val _sortState = MutableStateFlow("")
    private val _orderState = MutableStateFlow("")

    val uiState : Flow<QueryValue> = combine (
        _nameState, _filterState, _sortState, _orderState
    ) { name, filter, sort, order -> QueryValue (name, filter, sort, order) }

    sealed class UIEvent {
        data class NameChanged(val newValue: String): UIEvent()
        data class FilterChanged(val newValue: String): UIEvent()
        data class SortChanged(val newValue: String): UIEvent()
        data class OrderChanged(val newValue: String): UIEvent()
    }

    fun send (uiEvent : UIEvent) {
        when (uiEvent) {
            is UIEvent.NameChanged -> _nameState.value = uiEvent.newValue
            is UIEvent.FilterChanged -> _filterState.value = uiEvent.newValue
            is UIEvent.SortChanged -> _sortState.value = uiEvent.newValue
            is UIEvent.OrderChanged -> _orderState.value = uiEvent.newValue
        }
    }

    fun fetch (initialName : String, initialFilter : String, initialSort : String, initialOrder : String) {
        _nameState.value = initialName
        _filterState.value = initialFilter
        _sortState.value = initialSort
        _orderState.value = initialOrder
    }

}

@Composable
private fun QueryFieldByName (
    value : String,
    send : (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = value,
        onValueChange = { send(it) },
        modifier = modifier,
        leadingIcon = { Icon(painter = rememberVectorPainter(image = Icons.Default.Search), contentDescription = null) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QueryFieldSpinner (
    value : String,
    send : (String) -> Unit,
    itemValues : Array<String>,
    modifier: Modifier = Modifier,
    itemLabels : Array<String>?,
) {
    val labels = itemLabels ?: itemValues.map { it.lowercase().replace("_", " ")}.toTypedArray()
    val options = labels zip itemValues
    val selected = options.firstNotNullOfOrNull { pair -> pair.takeIf { pair.second == value } }
    var selectedOption by remember { mutableStateOf(selected?: options.first()) }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            value = selectedOption.first,
            readOnly = true,
            onValueChange = { },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.first) },
                    onClick = { selectedOption = option; expanded = false; send(option.second) },
                )
            }
        }
    }
}

@Composable
private fun QueryFieldRadioButton (
    value : String,
    send : (String) -> Unit,
    itemValues : Array<String>,
    modifier: Modifier = Modifier,
    itemLabels : Array<String>?,
) {
    val labels = itemLabels ?: itemValues.map { it.lowercase().replace("_", " ")}.toTypedArray()
    val options = labels zip itemValues
    val selected = options.firstNotNullOfOrNull { pair -> pair.takeIf { pair.second == value } }

    Row(modifier = modifier.padding(top = 4.dp)) {
        options.forEach { entry ->
            RadioButton(
                selected = (selected?.first == entry.first),
                onClick = { send(entry.second) }
            )
            Text(
                text = entry.first,
                modifier = Modifier.padding(horizontal = 0.dp)
            )
        }
    }
}

@Composable
private fun QueryFieldRadioButton (
    value: String,
    send: (String) -> Unit,
    itemValues: Array<String>,
    modifier: Modifier = Modifier,
    itemIcons: Array<ImageVector>,
) {
    val options = itemIcons zip itemValues
    val selected = options.firstNotNullOfOrNull { pair -> pair.takeIf { pair.second == value } }

    Row(modifier = modifier.padding(top = 4.dp)) {
        options.forEach { entry ->
            IconToggleButton(
                checked = (selected?.second == entry.second),
                onCheckedChange = { send(entry.second) }
            ) {
                Icon(
                    painter = rememberVectorPainter(entry.first),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
private fun QueryFieldInARow (
    line : Int,
    value : QueryValue,
    send : (QueryBuilder.UIEvent) -> Unit,
    modifier: Modifier = Modifier,
    properties : QueryProperties,
) {
    Row {
        val widthModifier = when (properties.mode.rows(line)) {
            1 -> modifier.fillMaxWidth()
            2 -> modifier.width(200.dp)
            3 -> modifier.width(150.dp)
            else -> modifier.weight(1.0f)
        }
        if (properties.mode.hasName(line)) {
            QueryFieldByName(
                value = value.name,
                modifier = widthModifier,
                send = { send(QueryBuilder.UIEvent.NameChanged(it)) }
            )
        }
        if (properties.mode.hasSpinnerFilter(line) && properties.filterItemValues != null) {
            QueryFieldSpinner(
                value = value.filter ?: "",
                send = { send(QueryBuilder.UIEvent.FilterChanged(it)) },
                itemValues = properties.filterItemValues,
                modifier = widthModifier,
                itemLabels = properties.filterItemLabels,
            )
        }
        if (properties.mode.hasRadioButtonFilter(line) && properties.filterItemValues != null) {
            QueryFieldRadioButton(
                value = value.filter ?: "",
                send = { send(QueryBuilder.UIEvent.FilterChanged(it)) },
                itemValues = properties.filterItemValues,
                modifier = widthModifier,
                itemLabels = properties.filterItemLabels,
            )
        }
        if (properties.mode.hasSpinnerSort(line) && properties.sortItemValues != null) {
            QueryFieldSpinner(
                value = value.sort ?: "",
                send = { send(QueryBuilder.UIEvent.SortChanged(it)) },
                itemValues = properties.sortItemValues,
                modifier = widthModifier,
                itemLabels = properties.sortItemLabels,
            )
        }
        if (properties.mode.hasRadioButtonSort(line) && properties.sortItemValues != null) {
            QueryFieldRadioButton(
                value = value.sort ?: "",
                send = { send(QueryBuilder.UIEvent.SortChanged(it)) },
                itemValues = properties.sortItemValues,
                modifier = widthModifier,
                itemLabels = properties.sortItemLabels,
            )
        }
        if (properties.mode.hasOrder(line) && properties.orderItemValues != null) {
            QueryFieldRadioButton(
                value = value.order ?: "",
                send = { send(QueryBuilder.UIEvent.OrderChanged(it)) },
                itemValues = properties.orderItemValues,
                modifier = widthModifier,
                itemIcons = arrayOf(Icons.Default.ArrowUpward, Icons.Default.ArrowDownward),
            )
        }
    }
}

@Composable
fun QueryField (
    value : QueryValue,
    send : (QueryBuilder.UIEvent) -> Unit,
    modifier: Modifier = Modifier,
    properties : QueryProperties,
) {
    if (properties.mode.columns == 0) return
    Column (
        modifier = modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                MaterialTheme.shapes.extraSmall
            )
    ) {
        if (properties.labelId != null) {
            Text(
                text = stringResource(id = properties.labelId),
                modifier = Modifier.padding(4.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        for (column in 0 until properties.mode.columns) {
            QueryFieldInARow(column, value, send, modifier, properties)
        }
    }
}