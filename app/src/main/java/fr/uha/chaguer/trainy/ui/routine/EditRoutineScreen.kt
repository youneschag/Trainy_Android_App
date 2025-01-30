package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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

@Destination<RootGraph>
@Composable
fun EditRoutineScreen(
    vm: RoutineViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    routineId: Long
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val uiTitleState by vm.uiTitleState.collectAsStateWithLifecycle()

    LaunchedEffect(routineId) {
        vm.edit(routineId)
        vm.titleBuilder.setScreenNameId(R.string.edit_routine)
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