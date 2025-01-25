package fr.uha.chaguer.android.icons.outlined

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Suppress("UnusedReceiverParameter")
val Icons.Outlined.NoTranslate: ImageVector
    get() {
        if (_noTranslate != null) {
            return _noTranslate!!
        }
        _noTranslate = materialIcon(name = "NoTranslate") {
            materialPath {
                moveTo(11.9F, 22F)
                lineTo(16.45F, 10F)
                lineTo(18.55F, 10F)
                lineTo(23.1F, 22F)
                lineTo(21F, 22F)
                lineTo(19.925F, 18.95F)
                lineTo(15.075F, 18.95F)
                lineTo(14F, 22F)
                lineTo(11.9F, 22F)

                moveTo(4F, 19F)
                lineTo(2.6F, 17.6F)
                lineTo(7.65F, 12.55F)
                quadTo(6.775F, 11.675F, 6.0625F, 10.55F)
                quadTo(5.35F, 9.425F, 4.75F, 8F)
                lineTo(6.85F, 8F)
                quadTo(7.35F, 8.975F, 7.85F, 9.7F)
                quadTo(8.35F, 10.425F, 9.05F, 11.15F)
                quadTo(9.875F, 10.325F, 10.7625F, 8.8375F)
                quadTo(11.65F, 7.35F, 12.1F, 6F)
                lineTo(1F, 6F)
                lineTo(1F, 4F)
                lineTo(8F, 4F)
                lineTo(8F, 2F)
                lineTo(10F, 2F)
                lineTo(10F, 4F)
                lineTo(17F, 4F)
                lineTo(17F, 6F)
                lineTo(14.1F, 6F)
                quadTo(13.575F, 7.8F, 12.525F, 9.7F)
                quadTo(11.475F, 11.6F, 10.45F, 12.6F)

                lineTo(12.85F, 15.05F)
                lineTo(12.1F, 17.1F)
                lineTo(9.05F, 13.975F)
                lineTo(4F, 19F)

                moveTo(15.7F, 17.2F)
                lineTo(19.3F, 17.2F)
                lineTo(17.5F, 12.1F)
                lineTo(15.7F, 17.2F)

                moveTo(20.55F, 23.35F)
                lineTo(0.65F, 3.45F)
                lineTo(2.05F, 2.05F)
                lineTo(21.95F, 21.95F)
                close()
            }
        }
        return _noTranslate!!
    }
private var _noTranslate: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconNoTranslatePreview() {
    Image(imageVector = Icons.Outlined.NoTranslate, contentDescription = null)
}