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
 * a Generic way to compare the fields used in a ViewModel to the initial values
 *
 * Param fields is expected to be a UIState composed by several FieldWrapper<*>
 *     like in the first working example :
 *     data class UIState(
 *         val args: Array<FieldWrapper<out Any>>,
 *     ) {
 *         val nameState : FieldWrapper<String> = args[0] as FieldWrapper<String>
 *         val typeState : FieldWrapper<VegetableType> = args[1] as FieldWrapper<VegetableType>
 *         val pictureState : FieldWrapper<Uri> = args[2] as FieldWrapper<Uri>
 *         val bestPlantingMonthState : FieldWrapper<Month> = args[3] as FieldWrapper<Month>
 *         val growthDays : FieldWrapper<Int> = args[4] as FieldWrapper<Int>
 *     }
 *
 * Param vararg initials is expected to be a table with all values initially gathered from the repository
 *     like in the first working example :
 *     data class Vegetable (
 *         @PrimaryKey(autoGenerate = true)
 *         var vid: Long = 0,
 *         var name: String,
 *         var type: VegetableType = VegetableType.OTHER,
 *         var picture : Uri? = null,
 *         var bestPlantingMonth : Month = Month(Month.UNKNOWN),
 *         var growthDays : Int = 0,
 *         override var createdDate : Date? = null,
 *         override var updatedDate : Date? = null,
 *     )
 *
 * Comparison is made in several steps :
 *      1.1) extraction of the relevant fields in the fields parameter (all have to ends with State)
 *      1.2) in the same time gather the value of the underlying FileWrapper
 *      2) create a map (computed) with all name entered with a value to null (this null means 'not yet computed')
 *      3) iterate through the array of initials
 *      3.1) select only those with the same stripped name as in fields
 *      3.2) compute the value of the selected property
 *      3.3) replace in computed those entries where we have a result of the comparison
 *      at this point computed contains the results :
 *      4.1) either one entry has a null value (means where not computed) so the final result is null : not yet computed
 *      4.2) either one entry is false (means one entry has a different value) so the result is true : Modified
 *      4.3) all entries are true (means all entries have the same value) the result is false : not Modified
 */
fun genericIsModified(fields: Any, initials: List<Any>): Boolean? {

    val fieldValues = fields::class.members.filter { it.name.endsWith("State") }.associate { it.name.replace("State", "") to (it.call(fields) as FieldWrapper<*>).value }

    val computed : MutableMap<String, Boolean?> = mutableMapOf()
    fieldValues.entries.associateTo(computed) { it.key to null  }

    initials.forEach { initial ->
        val initialValues = initial::class.members.filter {
            fieldValues.contains(it.name)
        }.associate {
            it.name to it.call(initial)
        }
        fieldValues.entries.forEach { if (initialValues.containsKey(it.key)) computed[it.key] = (it.value == initialValues[it.key])  }
    }

    if (computed.any { it.value == null }) return null
    if (computed.any { it.value == false }) return true
    return false
}

/**
 * a Generic way to check if one of the fields used in a ViewModel is in error
 *
 * Param fields is expected to be a UIState composed by several FieldWrapper<*>
 *     like in the first working example :
 *     data class UIState(
 *         val args: Array<FieldWrapper<out Any>>,
 *     ) {
 *         val nameState : FieldWrapper<String> = args[0] as FieldWrapper<String>
 *         val typeState : FieldWrapper<VegetableType> = args[1] as FieldWrapper<VegetableType>
 *         val pictureState : FieldWrapper<Uri> = args[2] as FieldWrapper<Uri>
 *         val bestPlantingMonthState : FieldWrapper<Month> = args[3] as FieldWrapper<Month>
 *         val growthDays : FieldWrapper<Int> = args[4] as FieldWrapper<Int>
 *     }
 *
 * Comparison is made in several steps :
 *      1.1) extraction of the relevant fields in the fields parameter (all have to ends with State)
 *      1.2) in the same time gather the errorId of the underlying FileWrapper
 *      at this point fieldValues contains the results :
 *      4.1) either one entry is different from a null value there is an error
 *      4.2) all entries are null (means all entries have no error)
 */
fun genericHasError (fields : Any) : Boolean {

    val fieldValues = fields::class.members.filter { it.name.endsWith("State") }.associate { it.name.replace("State", "") to (it.call(fields) as FieldWrapper<*>).errorId }

    if (fieldValues.any { it.value != null }) return true
    return false
}

/**
 * Main function entry to check equality between two Results,
 * one contains the current values stored in FieldWrappers
 * others contains the initial values (can be scattered in several record)
 *
 * the key is a name in fields ends with "State" and you have a property with stripped name in initials
 * we compare values from fields to values in initial
 *    if one in left result is null (unable to compare)
 *    if one is false the result is true
 *    if all are true the result is false
 */
fun genericIsModified (fields : Result<Any>, vararg initials : Result<Any> ) : Boolean? {
    if (fields !is Result.Success) return null
    initials.forEach { if (it !is Result.Success) return null }
    val initialSuccess = initials.map { (it as Result.Success).content }
    return genericIsModified(fields.content, initialSuccess )
}

/**
 * Main function entry to check if there is an error in one field of FieldWrappers
 *
 * the key is a name in fields ends with "State"
 *    if one is not null the result is true : error somewhere
 *    if all are null the result is false : no error
 */
fun genericHasError(fields: Result<Any>): Boolean? {
    if (fields !is Result.Success) return null
    return genericHasError(fields.content)
}