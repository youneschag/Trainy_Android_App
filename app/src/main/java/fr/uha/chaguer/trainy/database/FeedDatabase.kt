package fr.uha.chaguer.trainy.database

import android.net.Uri
import fr.uha.chaguer.trainy.model.*
import fr.uha.chaguer.trainy.R
import java.util.*

class FeedDatabase(
    private val db: TrainyDatabase
) {

    private suspend fun feedExercises(): LongArray {
        val dao: ExerciseDao = db.exerciseDAO()
        val ids = LongArray(5)
        ids[0] = dao.createExercise(getRandomExercise())
        ids[1] = dao.createExercise(getRandomExercise())
        ids[2] = dao.createExercise(getRandomExercise())
        ids[3] = dao.createExercise(getRandomExercise())
        ids[4] = dao.createExercise(getRandomExercise())
        return ids
    }

    private suspend fun feedRoutines(exerciseIds: LongArray) {
        val dao: RoutineDao = db.routineDAO()
        val routine = getRandomRoutine()
        val routineId = dao.upsert(routine)
        dao.addExerciseToRoutine(RoutineExerciseAssociation(routineId, exerciseIds[0]))
        dao.addExerciseToRoutine(RoutineExerciseAssociation(routineId, exerciseIds[3]))
    }

    @Suppress("unused")
    suspend fun populate(mode: Int) {
        val exerciseIds = feedExercises()
        feedRoutines(exerciseIds)
    }

    fun clear() {
        db.clearAllTables()
    }

    companion object {
        private val rnd: Random = Random()

        private val routineNames: Array<String> = arrayOf(
            "Cardio Blast",
            "Strength Training",
            "Yoga Flow",
            "Full Body Circuit",
            "Pilates Power"
        )

        private val exerciseNames: Array<String> = arrayOf(
            "Push-ups",
            "Squats",
            "Lunges",
            "Burpees",
            "Mountain Climbers",
            "Plank Hold",
            "Jumping Jacks",
            "High Knees",
            "Bicycle Crunches",
            "Leg Raises"
        )

        private val exerciseDescriptions: Array<String> = arrayOf(
            "A basic exercise to strengthen your upper body.",
            "An excellent move to build leg strength.",
            "Targets multiple muscle groups for better mobility.",
            "A high-intensity exercise for cardiovascular health.",
            "Improves agility and stamina.",
            "Focuses on core stability.",
            "A full-body cardio move to warm up.",
            "Boosts lower-body endurance.",
            "A great core workout to enhance flexibility.",
            "Focuses on your abdominal muscles."
        )

        private fun getRandomName(names: Array<String>): String {
            return names[rnd.nextInt(names.size)]
        }

        private fun getRandomBetween(low: Int, high: Int): Int {
            return rnd.nextInt(high - low) + low
        }

        private fun getRandomExercise(): Exercise {
            val name = getRandomName(exerciseNames)
            val description = getRandomName(exerciseDescriptions)
            return Exercise(
                exerciseId = 0,
                name = name,
                description = description,
                duration = getRandomBetween(10, 60),
                repetitions = getRandomBetween(8, 20)
            )
        }

        private fun getRandomRoutine(): Routine {
            return Routine(
                routineId = 0,
                name = getRandomName(routineNames),
                frequency = getRandomBetween(2, 5),
                objective = "General Fitness",
                startDay = Date()
            )
        }
    }
}
