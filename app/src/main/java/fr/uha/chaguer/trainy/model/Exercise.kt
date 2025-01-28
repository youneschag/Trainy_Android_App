package fr.uha.chaguer.trainy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
        @PrimaryKey(autoGenerate = true)
        val exerciseId: Long,
        val name: String,
        val description: String,
        val duration: Int,
        val repetitions: Int
)