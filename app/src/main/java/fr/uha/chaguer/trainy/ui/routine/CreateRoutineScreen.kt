package fr.uha.chaguer.trainy.ui.routine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.ui.StateScreen
import fr.uha.chaguer.android.ui.app.AppMenuEntry
import fr.uha.chaguer.android.ui.app.AppTopBar
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.ui.exercise.OutlinedExercisesFieldWrapper
import java.util.Date

@Destination<RootGraph>
@Composable
fun CreateRoutineScreen(
    vm: RoutineViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    // Obtenir l'état de l'interface utilisateur
    val uiTitleState by vm.uiTitleState.collectAsStateWithLifecycle()
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    // Initialisation de la routine par défaut
    LaunchedEffect(Unit) {
        vm.create(
            Routine(
                routineId = 0,
                name = "",
                frequency = 3,
                objective = "",
                startDay = Date()
            )
        )
        vm.titleBuilder.setScreenNameId(R.string.create_routine)
    }

    Scaffold(
        modifier = Modifier.padding(top = 30.dp),
        topBar = {
            AppTopBar(
                uiTitleState = uiTitleState, // Passer la valeur observée
                menuEntries = listOf(
                    AppMenuEntry.ActionEntry(
                        titleId = R.string.save,
                        icon = Icons.Filled.Save,
                        enabled = { uiTitleState.isSavable ?: false }, // Utiliser la valeur observée ici
                        listener = {
                            vm.save()
                            navigator.popBackStack()
                        }
                    )
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            StateScreen(state = uiState) { content ->
                SuccessRoutineScreen(content, { vm.send(it) })
            }
        }
    }
}