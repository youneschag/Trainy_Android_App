package fr.uha.chaguer.trainy.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.GreetingScreenDestination
import com.ramcosta.composedestinations.generated.destinations.RoutineListScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ExerciseListScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ProgressTrackingScreenDestination
import fr.uha.chaguer.android.model.IconPicture
import fr.uha.chaguer.android.model.IconRender
import fr.uha.chaguer.android.ui.app.AppBottomBar
import fr.uha.chaguer.android.ui.app.BottomBarDestination
import fr.uha.chaguer.trainy.R

@Composable
fun TrainyAppScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            AppBottomBar(
                navController,
                NavGraphs.root,
                bottomNavigations
            )
        }
    ) { innerPadding ->
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            modifier = Modifier.padding(innerPadding),
            navController = navController,
        )
    }
}

private val bottomNavigations = arrayOf<BottomBarDestination>(
    BottomBarDestination(
        direction = GreetingScreenDestination,
        icon = IconRender(
            focused = IconPicture(vector = Icons.Filled.Home),
            unfocused = IconPicture(vector = Icons.Outlined.Home)
        ),
        labelId = R.string.home
    ),
    BottomBarDestination(
        direction = ExerciseListScreenDestination,
        icon = IconRender(
            focused = IconPicture(vector = Icons.Filled.FitnessCenter),
            unfocused = IconPicture(vector = Icons.Outlined.FitnessCenter)
        ),
        labelId = R.string.exercises
    ),
    BottomBarDestination(
        direction = RoutineListScreenDestination,
        icon = IconRender(
            focused = IconPicture(vector = Icons.Filled.List),
            unfocused = IconPicture(vector = Icons.Outlined.List)
        ),
        labelId = R.string.routines
    ),
    BottomBarDestination(
        direction = ProgressTrackingScreenDestination,
        icon = IconRender(
            focused = IconPicture(vector = Icons.Filled.ShowChart),
            unfocused = IconPicture(vector = Icons.Outlined.ShowChart)
        ),
        labelId = R.string.progress
    ),
)
