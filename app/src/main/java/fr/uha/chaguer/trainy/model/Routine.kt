package fr.uha.chaguer.trainy.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    val routineId: Long,
    val name: String,
    val frequency: Int,
    val objective: String,
    val startDay: Date
)