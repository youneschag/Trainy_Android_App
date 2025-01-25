package fr.uha.chaguer.android.icons.filled

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Suppress("UnusedReceiverParameter", "unused")
val Icons.Filled.TriangleRight: ImageVector
    get() {
        if (_triangleRight != null) {
            return _triangleRight!!
        }
        _triangleRight = materialIcon(name = "TriangleRight") {
            materialPath {
                moveTo(2.0F, 2.0F)
                lineTo(22.0F, 11.0F)
                lineTo(2.0F, 22.0F)
                close()
            }
        }
        return _triangleRight!!
    }
private var _triangleRight: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconTriangleRightPreview() {
    Image(imageVector = Icons.Filled.TriangleRight, contentDescription = null)
}