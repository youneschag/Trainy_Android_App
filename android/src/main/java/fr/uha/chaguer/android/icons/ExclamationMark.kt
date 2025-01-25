package fr.uha.chaguer.android.icons

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Suppress("unused", "UnusedReceiverParameter")
val Icons.ExclamationMark: ImageVector
    get() {
        if (_exclamationMark != null) {
            return _exclamationMark!!
        }
        _exclamationMark = materialIcon(name = "ExclamationMark") {
            materialPath {
                moveTo(11.0F, 14.0F)
                lineTo(11.0F,  5.0F)
                lineTo(13.0F,  5.0F)
                lineTo(13.0F, 14.0F)
                lineTo(11.0F, 14.0F)

                moveTo(11.0F, 19.0F)
                lineTo(11.0F, 17.0F)
                lineTo(13.0F, 17.0F)
                lineTo(13.0F, 19.0F)
                lineTo(11.0F, 19.0F)
                close()
            }
        }
        return _exclamationMark!!
    }
private var _exclamationMark: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconExclamationMarkPreview() {
    Image(imageVector = Icons.ExclamationMark, contentDescription = null)
}
