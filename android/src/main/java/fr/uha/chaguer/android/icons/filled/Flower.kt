package fr.uha.chaguer.android.icons.filled

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Suppress("UnusedReceiverParameter", "unused")
val Icons.Filled.Flower: ImageVector
    get() {
        if (_flower != null) {
            return _flower!!
        }
        _flower = materialIcon(name = "Flower") {
            materialPath {
                moveTo	(	11F,	22F)
                quadTo	(	8.5F,	22F,	6.75F,	20.25F)
                quadTo	(	5F,	18.5F,	5F,	16F)
                lineTo	(	5F,	14F)
                quadTo	(	6.775F,	13.975F,	8.35F,	14.725F)
                quadTo	(	9.925F,	15.475F,	11F,	16.75F)
                lineTo	(	11F,	12.925F)
                quadTo	(	8.85F,	12.575F,	7.425F,	10.9125F)
                quadTo	(	6F,	9.25F,	6F,	7F)
                lineTo	(	6F,	3.6F)
                quadTo	(	6F,	2.95F,	6.575F,	2.6875F)
                quadTo	(	7.15F,	2.425F,	7.65F,	2.85F)
                lineTo	(	9.5F,	4.45F)
                lineTo	(	11.225F,	2.35F)
                quadTo	(	11.525F,	2F,	12F,	2F)
                quadTo	(	12.475F,	2F,	12.775F,	2.35F)
                lineTo	(	14.5F,	4.45F)
                lineTo	(	16.35F,	2.85F)
                quadTo	(	16.85F,	2.425F,	17.425F,	2.6875F)
                quadTo	(	18F,	2.95F,	18F,	3.6F)
                lineTo	(	18F,	7F)
                quadTo	(	18F,	9.25F,	16.575F,	10.9125F)
                quadTo	(	15.15F,	12.575F,	13F,	12.925F)
                lineTo	(	13F,	16.75F)
                quadTo	(	14.075F,	15.475F,	15.65F,	14.725F)
                quadTo	(	17.225F,	13.975F,	19F,	14F)
                lineTo	(	19F,	16F)
                quadTo	(	19F,	18.5F,	17.25F,	20.25F)
                quadTo	(	15.5F,	22F,	13F,	22F)
                lineTo	(	11F,	22F)
                close()
            }
        }
        return _flower!!
    }
private var _flower: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconFlowerPreview() {
    Image(imageVector = Icons.Filled.Flower, contentDescription = null)
}