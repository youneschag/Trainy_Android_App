package fr.uha.chaguer.android.ui.app

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.utils.isRouteOnBackStackAsState
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator
import fr.uha.chaguer.android.model.Icon
import fr.uha.chaguer.android.model.IconRender

class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: IconRender,
    @StringRes val labelId: Int
)

@Composable
fun AppBottomBar(
    navController: NavHostController,
    root: NavGraphSpec,
    destinations : Array<BottomBarDestination>,
    saveLastScreen : Boolean = false,
) {
    val navigator = navController.rememberDestinationsNavigator()
    NavigationBar {
        destinations.forEach { destination ->
            val isOnBackStack = navController.isRouteOnBackStackAsState(destination.direction).value
            NavigationBarItem(
                selected = isOnBackStack,
                onClick = {
                    if (isOnBackStack) {
                        navigator.popBackStack(destination.direction, false)
                        return@NavigationBarItem
                    }

                    navigator.navigate(destination.direction) {
                        popUpTo(root) {
                            if (saveLastScreen) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        if (saveLastScreen) {
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        selected = isOnBackStack,
                        picture = destination.icon,
                    )
                },
                label = {
                    AnimatedVisibility(visible = isOnBackStack) {
                        Text(
                            text = stringResource(id = destination.labelId),
                        )
                    }
                },
            )
        }
    }
}
