package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import fr.uha.chaguer.trainy.model.Exercise

@Composable
fun RoutineExercise(
    exercise: Exercise,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = exercise.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        supportingContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.FitnessCenter,
                    contentDescription = "Icon representing repetitions"
                )
                Text("${exercise.repetitions ?: 0} repetitions", fontSize = 16.sp)
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = "Icon representing duration"
                )
                Text("${exercise.duration ?: 0} min", fontSize = 16.sp)
            }
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Outlined.FitnessCenter,
                contentDescription = "Exercise icon",
                modifier = Modifier.size(32.dp)
            )
        },
        modifier = modifier
    )
}