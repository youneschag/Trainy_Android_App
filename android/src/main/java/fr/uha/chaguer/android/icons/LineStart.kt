package fr.uha.chaguer.android.icons

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Suppress("UnusedReceiverParameter", "unused")
val Icons.TimeStart: ImageVector
    get() {
        if (_timeStart != null) {
            return _timeStart!!
        }
        _timeStart = materialIcon(name = "TimeStart") {
            materialPath {
                moveTo( 4.0F, 20.0F)
                lineTo( 4.0F,  4.0F)
                lineTo( 9.0F,  4.0F)
                lineTo( 9.0F,  6.0F)
                lineTo( 6.0F,  6.0F)
                lineTo( 6.0F, 18.0F)
                lineTo( 9.0F, 18.0F)
                lineTo( 9.0F, 20.0F)
                lineTo( 4.0F, 20.0F)
                close()
            }
        }
        return _timeStart!!
    }

private var _timeStart: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconTimeStartPreview() {
    Image(imageVector = Icons.TimeStart, contentDescription = null)
}