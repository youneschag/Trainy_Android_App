package fr.uha.chaguer.android.database


import java.util.Date

interface Timable {

    fun getCreatedDate() : Date?
    fun setCreatedDate(date : Date)

    fun getUpdatedDate() : Date?
    fun setUpdatedDate(date : Date)

}