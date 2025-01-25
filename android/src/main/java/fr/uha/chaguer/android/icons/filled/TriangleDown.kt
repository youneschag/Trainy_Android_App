package fr.uha.chaguer.android.icons.filled

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Suppress("UnusedReceiverParameter", "unused")
val Icons.Filled.TriangleDown: ImageVector
    get() {
        if (_triangleDown != null) {
            return _triangleDown!!
        }
        _triangleDown = materialIcon(name = "TriangleDown") {
            materialPath {
                moveTo(2.0F, 2.0F)
                lineTo(22.0F, 2.0F)
                lineTo(11.0F, 22.0F)
                close()
            }
        }
        return _triangleDown!!
    }
private var _triangleDown: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconTriangleDownPreview() {
    Image(imageVector = Icons.Filled.TriangleDown, contentDescription = null)
}