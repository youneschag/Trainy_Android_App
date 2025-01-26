package fr.uha.chaguer.trainy.ui.routine

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.ui.StateScreen
import fr.uha.chaguer.android.ui.app.AppMenuEntry
import fr.uha.chaguer.android.ui.app.AppTopBar
import fr.uha.chaguer.android.ui.field.OutlinedDateFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedIntFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.ui.exercise.ExerciseViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun EditRoutineScreen(
    vm: RoutineViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    routineId: Long
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val uiTitleState by vm.uiTitleState.collectAsStateWithLifecycle()

    // Charger les données de la routine au début
    LaunchedEffect(routineId) {
        vm.edit(routineId)
        vm.titleBuilder.setScreenNameId(R.string.edit_routine)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                uiTitleState = uiTitleState,
                navigator = navigator,
                menuEntries = emptyList() // Pas de menu, uniquement un bouton en bas
            )
        },
        bottomBar = {
            // Bouton Modifier en bas
            Column (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        vm.send(RoutineViewModel.UIEvent.SaveChanges) // Envoi d'événement pour sauvegarder les modifications
                        navigator.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Modifier")
                    Text(
                        text = "Modifier",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Utilisation de StateScreen pour afficher et modifier les champs
            StateScreen(state = uiState) { content ->
                OutlinedTextFieldWrapper(
                    field =  content.nameState,
                    onValueChange = { vm.send(RoutineViewModel.UIEvent.NameChanged(it)) },
                    labelId = R.string.routine_name,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedDateFieldWrapper(
                    field =  content.dayState,
                    onValueChange = { vm.send(RoutineViewModel.UIEvent.StartDayChanged(it)) },
                    labelId = R.string.routine_start_day,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextFieldWrapper(
                    field =  content.objectiveState,
                    onValueChange = { vm.send(RoutineViewModel.UIEvent.ObjectiveChanged(it)) },
                    labelId = R.string.routine_objective,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedIntFieldWrapper(
                    field =  content.frequencyState,
                    onValueChange = { vm.send(RoutineViewModel.UIEvent.FrequencyChanged(it)) },
                    labelId = R.string.routine_frequency,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}