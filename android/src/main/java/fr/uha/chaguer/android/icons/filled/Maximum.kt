package fr.uha.chaguer.android.icons.filled

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Suppress("UnusedReceiverParameter", "unused")
val Icons.Filled.Maximum: ImageVector
    get() {
        if (_maximum != null) {
            return _maximum!!
        }
        _maximum = materialIcon(name = "Maximum") {
            materialPath {
                moveTo(12.0F, 5.0F)
                verticalLineTo(7.0F)
                lineTo(14.0F, 7.0F)
                lineTo(16.0F, 9.0F)
                lineTo(14.5F, 10.5F)
                lineTo(13.0F, 9.0F)
                lineTo(13.0F, 19.0F)
                lineTo(11.0F, 19.0F)
                lineTo(11.0F, 9.0F)
                lineTo(9.5F, 10.5F)
                lineTo(8.0F, 9.0F)
                lineTo(10.0F, 7.0F)
                lineTo(8.0F, 7.0F)
                verticalLineTo(5.0F)
                close()
            }
        }
        return _maximum!!
    }
private var _maximum: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconMaximumPreview() {
    Image(imageVector = Icons.Filled.Maximum, contentDescription = null)
}