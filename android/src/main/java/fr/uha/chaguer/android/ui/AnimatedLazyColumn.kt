package fr.uha.chaguer.android.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

/**
 * An animated LazyListScope extension for items.
 * Adds fade-in animation for items as they become visible during scroll.
 *
 * @param items The list of items to display.
 * @param scrollState The LazyListState to track scrolling.
 * @param key Optional key for identifying items.
 * @param contentType Optional content type for each item.
 * @param itemContent The composable content for each item.
 */
inline fun <T> LazyListScope.animatedItems(
    items: List<T>,
    scrollState: LazyListState,
    noinline key: ((item: T) -> Any)? = null,
    crossinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyItemScope.(item: T, backgroundColor: Color) -> Unit
) = itemsIndexed(
    items = items,
    key = if (key != null) { _: Int, item: T -> key(item) } else null,
    contentType = contentType
) { index, item ->

    // Animatable for alpha
    val animatableAlpha = remember { Animatable(0.7f) }

    // Visibility check based on scroll state
    val isVisible = remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex <= index
        }
    }

    // Animation trigger when the item becomes visible
    LaunchedEffect(isVisible.value) {
        if (isVisible.value) {
            animatableAlpha.animateTo(
                0f, animationSpec = tween(durationMillis = 850, delayMillis = 150)
            )
        }
    }

    // Pass the calculated background color to the item's content
    itemContent(
        item,
        MaterialTheme.colorScheme.onBackground.copy(alpha = animatableAlpha.value)
    )
}