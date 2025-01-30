package fr.uha.chaguer.trainy.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ExerciseListScreenDestination
import com.ramcosta.composedestinations.generated.destinations.RoutineListScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.database.DateUtils.formatDate
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.ui.theme.MontserratFont

@Destination<RootGraph>(start = true)
@Composable
fun GreetingScreen(
    navigator: DestinationsNavigator,
    vm: HomeViewModel = hiltViewModel()
) {
    val exercises by vm.getAllExercises().collectAsStateWithLifecycle(emptyList())
    val routines by vm.getAllRoutines().collectAsStateWithLifecycle(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.welcome_message),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFont
            )
            Text(
                text = stringResource(R.string.app_description),
                fontSize = 16.sp,
                fontFamily = MontserratFont,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            item {
                SectionCard(
                    title = stringResource(R.string.titre_routine),
                    items = routines.take(3),
                    emptyMessage = stringResource(R.string.no_routine),
                    onSeeMore = { navigator.navigate(RoutineListScreenDestination) }
                )
            }

            item {
                Spacer(modifier = Modifier.padding(8.dp))
            }

            item {
                SectionCard(
                    title = stringResource(R.string.titre_exercise),
                    items = exercises.take(3),
                    emptyMessage = stringResource(R.string.no_exercise),
                    onSeeMore = { navigator.navigate(ExerciseListScreenDestination) }
                )
            }
        }
    }
}

@Composable
fun <T> SectionCard(
    title: String,
    items: List<T>,
    emptyMessage: String,
    onSeeMore: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFont,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (items.isEmpty()) {
                Text(
                    text = emptyMessage,
                    fontSize = 16.sp,
                    fontFamily = MontserratFont,
                    color = Color.Gray
                )
            } else {
                items.forEach { item ->
                    SubCard(item = item)
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onSeeMore) {
                    Text(text = stringResource(R.string.see_more))
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun <T> SubCard(item: T) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            when (item) {
                is Exercise -> {
                    Text(
                        text = "🏋️ ${item.name}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MontserratFont,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.description),
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = item.description,
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                            color = Color.Gray,
                            modifier = Modifier.weight(2f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.duration),
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${item.duration} min",
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                            color = Color.Gray,
                            modifier = Modifier.weight(2f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.repetitions),
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${item.repetitions}",
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                            color = Color.Gray,
                            modifier = Modifier.weight(2f)
                        )
                    }
                }

                is Routine -> {
                    Text(
                        text = "🔥 ${item.name}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MontserratFont,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.routine_objective),
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = item.objective,
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                            color = Color.Gray,
                            modifier = Modifier.weight(2f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.routine_frequency),
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${item.frequency} fois/semaine",
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                            color = Color.Gray,
                            modifier = Modifier.weight(2f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.routine_start_day),
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = formatDate(item.startDay),
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                            color = Color.Gray,
                            modifier = Modifier.weight(2f)
                        )
                    }
                }

                else -> {
                    Text(text = item.toString(), fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}