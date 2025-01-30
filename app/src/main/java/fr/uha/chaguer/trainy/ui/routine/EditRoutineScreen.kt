package fr.uha.chaguer.trainy.ui.routine

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.ui.StateScreen
import fr.uha.chaguer.trainy.R

@Destination<RootGraph>
@Composable
fun EditRoutineScreen(
    vm: RoutineViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    routineId: Long
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(routineId) {
        vm.edit(routineId)
        vm.titleBuilder.setScreenNameId(R.string.edit_routine)
    }

    StateScreen(state = uiState) { content ->
        SuccessRoutineScreen(
            routine = content,
            send = { vm.send(it) },
            onBackClick = {
                if (content.nameState.value.isNullOrBlank() ||
                    content.objectiveState.value.isNullOrBlank() ||
                    (content.frequencyState.value ?: 0) <= 0
                ) {
                    showErrorDialog = true
                } else {
                    Toast.makeText(context, "Routine modifiée avec succès", Toast.LENGTH_SHORT).show()
                    navigator.popBackStack()
                }
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) { Text("OK") }
            },
            title = { Text("Erreur") },
            text = { Text("Veuillez remplir tous les champs avant d'enregistrer la routine") }
        )
    }
}