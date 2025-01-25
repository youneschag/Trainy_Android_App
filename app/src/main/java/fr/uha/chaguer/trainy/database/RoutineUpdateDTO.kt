package fr.uha.chaguer.trainy.database

sealed class RoutineUpdateDTO {
    data class Name(val routineId: Long, val name: String) : RoutineUpdateDTO()
    data class Frequency(val routineId: Long, val frequency: Long) : RoutineUpdateDTO()
    data class Objectives(val routineId: Long, val objective: String) : RoutineUpdateDTO()
}