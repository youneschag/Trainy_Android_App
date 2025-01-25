package fr.uha.chaguer.android.viewmodel

import androidx.compose.runtime.Immutable
import fr.uha.chaguer.android.ui.field.FieldWrapper

@Immutable
sealed interface Result<out T> {
    data class Success<T>(val content: T) : Result<T>
    data class Error(val exception: Throwable? = null, val message: String? = null) : Result<Nothing>
    data object Loading : Result<Nothing>
}

/**
 * Compare si les champs dans les ViewModels "Exercice" ou "Routine" ont été modifiés
 */
fun genericIsModified(fields: Any, initials: List<Any>): Boolean? {
    val fieldValues = fields::class.members
        .filter { it.name.endsWith("State") }
        .associate { it.name.replace("State", "") to (it.call(fields) as FieldWrapper<*>).value }

    val computed: MutableMap<String, Boolean?> = fieldValues.entries.associateTo(mutableMapOf()) { it.key to null }

    initials.forEach { initial ->
        val initialValues = initial::class.members
            .filter { fieldValues.contains(it.name) }
            .associate { it.name to it.call(initial) }

        fieldValues.forEach { (key, value) ->
            if (initialValues.containsKey(key)) {
                computed[key] = (value == initialValues[key])
            }
        }
    }

    return when {
        computed.any { it.value == null } -> null
        computed.any { it.value == false } -> true
        else -> false
    }
}

/**
 * Vérifie s'il y a une erreur dans les champs des ViewModels "Exercice" ou "Routine"
 */
fun genericHasError(fields: Any): Boolean {
    val fieldValues = fields::class.members
        .filter { it.name.endsWith("State") }
        .associate { it.name.replace("State", "") to (it.call(fields) as FieldWrapper<*>).errorId }

    return fieldValues.any { it.value != null }
}

/**
 * Vérifie les modifications pour des champs encapsulés dans un `Result`
 */
fun genericIsModified(fields: Result<Any>, vararg initials: Result<Any>): Boolean? {
    if (fields !is Result.Success) return null
    if (initials.any { it !is Result.Success }) return null

    val initialSuccess = initials.map { (it as Result.Success).content }
    return genericIsModified(fields.content, initialSuccess)
}

/**
 * Vérifie les erreurs dans un `Result`
 */
fun genericHasError(fields: Result<Any>): Boolean? {
    if (fields !is Result.Success) return null
    return genericHasError(fields.content)
}