package fr.uha.chaguer.trainy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.uha.chaguer.android.database.DatabaseTypeConverters
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.model.RoutineExerciseAssociation

@TypeConverters(DatabaseTypeConverters::class)
@Database(
    entities = [
        Exercise::class,
        Routine::class,
        RoutineExerciseAssociation::class
    ],
    version = 1,
    exportSchema = false
)

abstract class TrainyDatabase : RoomDatabase() {
    abstract fun exerciseDAO(): ExerciseDao

    abstract fun routineDAO(): RoutineDao

    companion object {
        private lateinit var instance: TrainyDatabase

        fun create(context: Context): TrainyDatabase {
            instance = Room.databaseBuilder(context, TrainyDatabase::class.java, "trainy.db").build()
            return instance
        }

        fun get(): TrainyDatabase {
            return instance
        }
    }
}
