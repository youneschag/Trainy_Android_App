package fr.uha.chaguer.android.model

import android.content.Context
import androidx.annotation.StringRes

data class DynamicText (
    @StringRes
    val labelId : Int? = null,
    var label : String? = null,
) {
    fun isTranslatable () : Boolean {
        return labelId != null
    }

    fun getText (context : Context) : String {
        return when {
            label == null && labelId == null -> ""
            label != null && labelId == null -> label!!
            label == null && labelId != null -> context.resources.getString(labelId)
            label != null && labelId != null -> "both"
            else -> "strange"
        }
    }

}