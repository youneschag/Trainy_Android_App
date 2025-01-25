package fr.uha.chaguer.android.icons.outlined

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Suppress("UnusedReceiverParameter", "unused")
val Icons.Outlined.Width: ImageVector
    get() {
        if (_width != null) {
            return _width!!
        }
        _width = materialIcon(name = "Width") {
            materialPath {
                moveTo( 7.0F, 16.0F)
                lineTo( 3.0F, 12.0F)
                lineTo( 7.0F,  8.0F)
                lineTo( 8.425F, 9.4F)
                lineTo( 6.825F, 11.0F)
                lineTo(17.175F, 11.0F)
                lineTo(15.6F,  9.4F)
                lineTo(17.0F,  8.0F)
                lineTo(21.0F, 12.0F)
                lineTo(17.0F, 16.0F)
                lineTo(15.6F, 14.6F)
                lineTo(17.175F, 13.0F)
                lineTo( 6.825F, 13.0F)
                lineTo( 8.4F, 14.6F)
                lineTo( 7.0F, 16.0F)
                close()
            }
        }
        return _width!!
    }
private var _width: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconWidthPreview() {
    Image(imageVector = Icons.Outlined.Width, contentDescription = null)
}