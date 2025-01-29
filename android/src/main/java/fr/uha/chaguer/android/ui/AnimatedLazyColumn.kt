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

/*
 dark mode: start 0.5, end 0, color White
 light mode: start 0.5, end 0, color Black
 after I switch to onBackground instead color (alpha is applied so the invert color is better)
 next 0.7 instead 0.5 more expressive
 delay of 150 so the start is enough after the start of scroll so the cell is fully visible
 duration of 750 seems enough
 no way to pass the color in the background color of the modifier ... not able to extract it later
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

        val animatableAlpha = remember { Animatable(.7f) }
        val isVisible = remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex <= index
            }
        }

        LaunchedEffect(isVisible.value) {
            if (isVisible.value) {
                animatableAlpha.animateTo(
                    0f, animationSpec = tween(durationMillis = 850, delayMillis = 150)
                )
            }
        }
        itemContent(
            item,
            MaterialTheme.colorScheme.onBackground.copy(alpha = animatableAlpha.value)
        )
    }
