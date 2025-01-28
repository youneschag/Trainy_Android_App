package fr.uha.chaguer.trainy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.uha.chaguer.android.database.DatabaseTypeConverters
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.model.Progress
import fr.uha.chaguer.trainy.model.RoutineExerciseAssociation
import fr.uha.chaguer.trainy.model.RoutineProgress

@TypeConverters(DatabaseTypeConverters::class)
@Database(
    entities = [
        Exercise::class,
        Routine::class,
        Progress::class,
        RoutineProgress::class,
        RoutineExerciseAssociation::class
    ],
    version = 5,
    exportSchema = false
)

abstract class TrainyDatabase : RoomDatabase() {
    abstract fun exerciseDAO(): ExerciseDao

    abstract fun routineDAO(): RoutineDao

    abstract fun progressDAO(): ProgressDao

    abstract fun routineProgressDAO(): RoutineProgressDao

    companion object {
        private lateinit var instance: TrainyDatabase

        fun create(context: Context): TrainyDatabase {
            instance = Room.databaseBuilder(context, TrainyDatabase::class.java, "trainy.db")
                .fallbackToDestructiveMigration()
                .build()
            return instance
        }

        fun get(): TrainyDatabase {
            return instance
        }
    }
}
