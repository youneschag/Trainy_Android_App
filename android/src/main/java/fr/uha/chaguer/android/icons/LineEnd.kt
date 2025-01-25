package fr.uha.chaguer.android.icons

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Suppress("UnusedReceiverParameter", "unused")
val Icons.TimeEnd: ImageVector
    get() {
        if (_timeEnd != null) {
            return _timeEnd!!
        }
        _timeEnd = materialIcon(name = "LineEnd") {
            materialPath {
                moveTo(15.0F, 20.0F)
                lineTo(15.0F, 18.0F)
                lineTo(18.0F, 18.0F)
                lineTo(18.0F,  6.0F)
                lineTo(15.0F,  6.0F)
                lineTo(15.0F,  4.0F)
                lineTo(20.0F,  4.0F)
                lineTo(20.0F, 20.0F)
                lineTo(15.0F, 20.0F)
                close()
            }
        }
        return _timeEnd!!
    }
private var _timeEnd: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconLineEndPreview() {
    Image(imageVector = Icons.TimeEnd, contentDescription = null)
}