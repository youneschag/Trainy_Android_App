package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.ui.StateScreen
import fr.uha.chaguer.android.ui.app.AppTopBar
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Routine
import java.util.Date

@Destination<RootGraph>
@Composable
fun CreateRoutineScreen(
    vm: RoutineViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val uiTitleState by vm.uiTitleState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.create(
            Routine(0, "Nouvelle routine", 3, "Améliorer la forme", Date())
        )
        vm.titleBuilder.setScreenNameId(R.string.create_routine)
    }

    Scaffold(
        topBar = {
            AppTopBar(uiTitleState = uiTitleState, navigator)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            StateScreen(state = uiState) { content ->
                SuccessRoutineScreen(
                    routine = content,
                    send = { vm.send(it) }
                )
            }
        }
    }

}