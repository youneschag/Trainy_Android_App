package fr.uha.chaguer.android.icons.filled

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Suppress("UnusedReceiverParameter", "unused")
val Icons.Filled.Minimum: ImageVector
    get() {
        if (_minimum != null) {
            return _minimum!!
        }
        _minimum = materialIcon(name = "Minimum") {
            materialPath {
                moveTo(12.0F, 21.0F)
                verticalLineTo(19.0F)
                lineTo(10.0F, 19.0F)
                lineTo(8.0F, 17.0F)
                lineTo(9.5F, 15.5F)
                lineTo(11.0F, 17.0F)
                lineTo(11.0F, 7.0F)
                lineTo(13.0F, 7.0F)
                lineTo(13.0F, 17.0F)
                lineTo(14.5F, 15.5F)
                lineTo(16.0F, 17.0F)
                lineTo(14.0F, 19.0F)
                lineTo(16.0F, 19.0F)
                verticalLineTo(21.0F)
                close()
            }
        }
        return _minimum!!
    }
private var _minimum: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconMinimumPreview() {
    Image(imageVector = Icons.Filled.Minimum, contentDescription = null)
}