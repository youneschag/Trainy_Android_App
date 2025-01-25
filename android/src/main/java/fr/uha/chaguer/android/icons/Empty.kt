package fr.uha.chaguer.android.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

@Suppress("UnusedReceiverParameter", "unused")
val Icons.Empty: ImageVector
    get() {
        if (_empty != null) {
            return _empty!!
        }
        _empty = materialIcon(name = "Empty") {
            materialPath {
            }
        }
        return _empty!!
    }

private var _empty: ImageVector? = null
