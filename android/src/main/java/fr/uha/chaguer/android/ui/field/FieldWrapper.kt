package fr.uha.chaguer.android.ui.field

data class FieldWrapper<T>(
    val value: T? = null,
    val errorId: Int? = null
)