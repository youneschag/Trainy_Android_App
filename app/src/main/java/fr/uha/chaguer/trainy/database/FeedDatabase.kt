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
        val ids = LongArray(7)
        for (i in ids.indices) {
            ids[i] = dao.createExercise(getRandomExercise())
        }
        return ids
    }

    private suspend fun feedRoutines(exerciseIds: LongArray): LongArray {
        val dao: RoutineDao = db.routineDAO()
        val routineIds = LongArray(3)

        for (i in routineIds.indices) {
            val routine = getRandomRoutine()
            routineIds[i] = dao.createRoutine(routine)
        }

        var exerciseIndex = 0
        for (routineId in routineIds) {
            val firstExerciseId = exerciseIds[exerciseIndex % exerciseIds.size]
            val secondExerciseId = exerciseIds[(exerciseIndex + 1) % exerciseIds.size]

            dao.addExerciseToRoutine(RoutineExerciseAssociation(routineId, firstExerciseId))
            dao.addExerciseToRoutine(RoutineExerciseAssociation(routineId, secondExerciseId))

            exerciseIndex += 2
        }

        return routineIds
    }

    private suspend fun feedProgress(routineIds: LongArray, exerciseIds: LongArray) {
        val dao: RoutineProgressDao = db.routineProgressDAO()

        for (routineId in routineIds) {
            for (exerciseId in exerciseIds) {
                val progress = getRandomProgress(routineId, exerciseId)
                dao.insertOrUpdateProgress(progress)
            }
        }
    }

    @Suppress("unused")
    suspend fun populate(mode: Int) {
        val exerciseIds = feedExercises()
        val routineIds = feedRoutines(exerciseIds)
        feedProgress(routineIds, exerciseIds)
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
            "Pilates Power",
            "Endurance Run",
            "Core Crusher",
            "Leg Day Madness",
            "Upper Body Focus",
            "HIIT Extreme",
            "CrossFit Challenge",
            "Athletic Performance",
            "Mobility & Stretching",
            "Functional Fitness",
            "Weightlifting Basics",
            "Triathlon Training",
            "Combat Conditioning",
            "Marathon Preparation",
            "Boxing Workout",
            "Swimming Endurance"
        )

        private val exerciseNames: Array<String> = arrayOf(
            "Push-ups", "Squats", "Lunges", "Burpees", "Plank Hold",
            "Deadlifts", "Bench Press", "Overhead Press", "Pull-ups", "Dips",
            "Dumbbell Rows", "Leg Press", "Calf Raises", "Bicep Curls", "Tricep Dips",
            "Hammer Curls", "Chest Fly", "Shoulder Shrugs", "Front Squats", "Kettlebell Swings",
            "Mountain Climbers", "Jump Rope", "Box Jumps", "High Knees", "Jumping Jacks",
            "Sprints", "Rowing Machine", "Elliptical Training", "Cycling", "Treadmill Run",
            "Battle Ropes", "Agility Ladder Drills", "Hiking", "Stair Climbing", "Swimming Laps",
            "Downward Dog", "Child’s Pose", "Warrior Pose", "Tree Pose", "Cat-Cow Stretch",
            "Sun Salutation", "Seated Forward Bend", "Cobra Stretch", "Bridge Pose", "Butterfly Stretch",
            "Kettlebell Snatch", "Wall Balls", "Box Step-Ups", "Tire Flips", "Battle Ropes",
            "Sled Push", "Turkish Get-Up", "Sandbag Carries", "Farmers Walk", "Burpee Pull-Ups",
            "Single-Leg Deadlifts", "Resistance Band Squats", "Med Ball Slams", "TRX Rows", "Bear Crawls",
            "Side Plank", "Reverse Lunges", "Glute Bridges", "Banded Side Steps", "Heel Touches",
            "Boxing Jab-Cross", "Kickboxing Roundhouse", "MMA Sprawls", "Speed Bag Drills", "Shadowboxing",
            "Sledgehammer Slams", "Punching Bag Workout", "Jump Knee Strikes", "Duck & Weave Drill", "Defensive Slips"
        )

        private val exerciseDescriptions: Array<String> = arrayOf(
            // 🔥 Musculation & Renforcement
            "Un exercice classique pour renforcer la poitrine, les épaules et les triceps.",
            "Idéal pour renforcer les jambes, les fessiers et améliorer la stabilité du tronc.",
            "Travaille plusieurs groupes musculaires pour améliorer la mobilité et l'équilibre.",
            "Un exercice explosif pour brûler des calories et renforcer le cardio.",
            "Améliore l'agilité, la coordination et l'endurance musculaire.",
            "Cible les abdominaux et améliore la stabilité du tronc.",
            "Un exercice de cardio complet qui sollicite tout le corps.",
            "Renforce le bas du corps et améliore l’endurance musculaire.",
            "Travail intensif des abdominaux, excellent pour la flexibilité du tronc.",
            "Idéal pour travailler les muscles du bas-ventre et améliorer la posture.",
            "Un excellent moyen de brûler des calories rapidement et d'améliorer le cardio.",
            "Exercice dynamique qui développe l'explosivité et la vitesse des jambes.",
            "Améliore la condition physique générale et développe la rapidité.",
            "Idéal pour travailler l’endurance musculaire et améliorer la coordination.",
            "Un excellent exercice pour renforcer le bas du corps tout en travaillant le cardio.",
            "Renforce le système cardiovasculaire tout en sollicitant les jambes.",
            "Améliore la capacité pulmonaire et l'endurance sur de longues distances.",
            "Développe la force et l’endurance musculaire en côte ou en escaliers.",
            "Travaille la vitesse et l’agilité en sprintant sur des distances courtes.",
            "Améliore la condition physique et brûle rapidement des calories.",
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

        private fun getRandomProgress(routineId: Long, exerciseId: Long): RoutineProgress {
            return RoutineProgress(
                id = 0,
                routineId = routineId,
                exerciseId = exerciseId,
                completedRepetitions = getRandomBetween(5, 20),
                completedDuration = getRandomBetween(5, 30),
                date = Date()
            )
        }
    }
}
