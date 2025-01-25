package fr.uha.chaguer.android.icons

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Suppress("UnusedReceiverParameter", "unused")
val Icons.VerticalBar: ImageVector
    get() {
        if (_verticalBar != null) {
            return _verticalBar!!
        }
        _verticalBar = materialIcon(name = "TimeStart") {
            materialPath {
                moveTo( 0.0F, 0.0F)
                lineTo( 0.0F,  23.0F)
                lineTo( 3.0F,  23.0F)
                lineTo( 3.0F,  0.0F)
                close()
            }
        }
        return _verticalBar!!
    }

private var _verticalBar: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconTimeStartPreview() {
    Image(imageVector = Icons.VerticalBar, contentDescription = null)
}