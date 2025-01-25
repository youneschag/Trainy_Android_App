package fr.uha.chaguer.android.ui.field

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Suppress("unused")
sealed class FieldPaging(val slow : Int, val fast : Int) {
    data class MinusOnly(val amount : Int = 1) : FieldPaging(amount, 0)
    data class PlusOnly(val amount : Int = 1)  : FieldPaging(amount, 0)
    data class MinusAndPlus(val amount : Int = 1)  : FieldPaging(amount, 0)
    data class LessOnly(val amount : Int = 10) : FieldPaging(0, amount)
    data class MoreOnly(val amount : Int = 10) : FieldPaging(0, amount)
    data class LessAndMore(val amount : Int = 10) : FieldPaging(0, amount)
    open class All(small : Int, big : Int) : FieldPaging(small, big)
    data object AllInDecade : All(1, 10)
    data object AllInWeek : All(1, 7)
}

private fun getImageFor (paging : FieldPaging, forPaging : FieldPaging) : ImageVector? {
    val image =  when {
        forPaging is FieldPaging.LessOnly && paging is FieldPaging.LessAndMore -> Icons.Outlined.KeyboardDoubleArrowLeft
        forPaging is FieldPaging.LessOnly && paging is FieldPaging.LessOnly -> Icons.Outlined.KeyboardDoubleArrowLeft
        forPaging is FieldPaging.LessOnly && paging is FieldPaging.All -> Icons.Outlined.KeyboardDoubleArrowLeft
        forPaging is FieldPaging.MoreOnly && paging is FieldPaging.LessAndMore -> Icons.Outlined.KeyboardDoubleArrowRight
        forPaging is FieldPaging.MoreOnly && paging is FieldPaging.MoreOnly -> Icons.Outlined.KeyboardDoubleArrowRight
        forPaging is FieldPaging.MoreOnly && paging is FieldPaging.All -> Icons.Outlined.KeyboardDoubleArrowRight
        forPaging is FieldPaging.MinusOnly && paging is FieldPaging.MinusAndPlus -> Icons.Outlined.Remove
        forPaging is FieldPaging.MinusOnly && paging is FieldPaging.MinusOnly -> Icons.Outlined.Remove
        forPaging is FieldPaging.MinusOnly && paging is FieldPaging.All -> Icons.Outlined.Remove
        forPaging is FieldPaging.PlusOnly && paging is FieldPaging.MinusAndPlus -> Icons.Outlined.Add
        forPaging is FieldPaging.PlusOnly && paging is FieldPaging.PlusOnly -> Icons.Outlined.Add
        forPaging is FieldPaging.PlusOnly && paging is FieldPaging.All -> Icons.Outlined.Add
        else -> null
    }
    return image
}

private fun getAmountFor (paging : FieldPaging, forPaging : FieldPaging) : Int? {
    val amount =  when {
        forPaging is FieldPaging.LessOnly && paging is FieldPaging.LessAndMore -> - paging.fast
        forPaging is FieldPaging.LessOnly && paging is FieldPaging.LessOnly -> - paging.fast
        forPaging is FieldPaging.LessOnly && paging is FieldPaging.All -> - paging.fast
        forPaging is FieldPaging.MoreOnly && paging is FieldPaging.LessAndMore -> paging.fast
        forPaging is FieldPaging.MoreOnly && paging is FieldPaging.MoreOnly -> paging.fast
        forPaging is FieldPaging.MoreOnly && paging is FieldPaging.All -> paging.fast
        forPaging is FieldPaging.MinusOnly && paging is FieldPaging.MinusAndPlus -> - paging.slow
        forPaging is FieldPaging.MinusOnly && paging is FieldPaging.MinusOnly -> - paging.slow
        forPaging is FieldPaging.MinusOnly && paging is FieldPaging.All -> - paging.slow
        forPaging is FieldPaging.PlusOnly && paging is FieldPaging.MinusAndPlus -> paging.slow
        forPaging is FieldPaging.PlusOnly && paging is FieldPaging.PlusOnly -> paging.slow
        forPaging is FieldPaging.PlusOnly && paging is FieldPaging.All -> paging.slow
        else -> null
    }
    return amount
}

private fun hasPagingIconFor (paging : FieldPaging, forPaging : FieldPaging) : Boolean {
    if (getImageFor(paging, forPaging) == null) return false
    if (getAmountFor(paging, forPaging) == null) return false
    return true
}

private fun hasPagingIcon (paging : FieldPaging?, allPagingFor : Array<FieldPaging>) : Boolean {
    if (paging == null) return false
    val result = allPagingFor.map { hasPagingIconFor (paging, it) }.filter { it }
    if (result.isEmpty()) return false
    return true
}

fun hasPagingIcon (paging : FieldPaging?) : Boolean {
    return hasPagingIcon (
        paging,
        arrayOf(FieldPaging.LessOnly(), FieldPaging.MinusOnly(), FieldPaging.PlusOnly(), FieldPaging.MoreOnly())
    )
}

fun hasLeadingIcons (paging : FieldPaging?) : Boolean {
    return hasPagingIcon (
        paging,
        arrayOf(FieldPaging.LessOnly(), FieldPaging.MinusOnly())
    )
}

fun hasTrailingIcon (paging : FieldPaging?) : Boolean {
    return hasPagingIcon (
        paging,
        arrayOf(FieldPaging.PlusOnly(), FieldPaging.MoreOnly())
    )
}

@Composable
private fun BuildPagingIconFor (paging : FieldPaging, forPaging : FieldPaging, onClick : (Int) -> Unit) {
    val image = getImageFor(paging, forPaging) ?: return
    val amount = getAmountFor(paging, forPaging) ?: return
    Icon(imageVector = image, contentDescription = null, modifier = Modifier.selectable(selected = false, onClick = { onClick(amount) }))
}


@Composable
fun BuildPagingIcons (paging : FieldPaging?, onClick : (Int) -> Unit) {
    if (paging == null) return
    val allPagingFor = arrayOf(FieldPaging.LessOnly(), FieldPaging.MinusOnly(), FieldPaging.PlusOnly(), FieldPaging.MoreOnly())
    Row {
        allPagingFor.forEach { BuildPagingIconFor (paging, it, onClick) }
    }
}

@Composable
fun BuildLeadingIcons (paging : FieldPaging?, onClick : (Int) -> Unit) {
    if (paging == null) return
    val allPagingFor = arrayOf(FieldPaging.LessOnly(), FieldPaging.MinusOnly())
    Row {
        allPagingFor.forEach { BuildPagingIconFor (paging, it, onClick) }
    }
}

@Composable
fun BuildTrailingIcons (paging : FieldPaging?, onClick : (Int) -> Unit) {
    if (paging == null) return
    val allPagingFor = arrayOf(FieldPaging.PlusOnly(), FieldPaging.MoreOnly())
    Row {
        allPagingFor.forEach { BuildPagingIconFor (paging, it, onClick) }
    }
}
