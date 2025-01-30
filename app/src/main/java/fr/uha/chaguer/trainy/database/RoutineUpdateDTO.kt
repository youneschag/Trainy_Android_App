package fr.uha.chaguer.trainy.database

import java.util.Date

sealed class RoutineUpdateDTO {
    data class Name(val routineId: Long, val name: String) : RoutineUpdateDTO()
    data class Frequency(val routineId: Long, val frequency: Int) : RoutineUpdateDTO()
    data class Objectives(val routineId: Long, val objective: String) : RoutineUpdateDTO()
    data class StartDay(val routineId: Long, val startDay: Date) : RoutineUpdateDTO()
}