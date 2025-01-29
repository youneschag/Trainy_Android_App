package fr.uha.chaguer.trainy.ui.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
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
import fr.uha.chaguer.android.ui.field.OutlinedIntFieldWrapper
import fr.uha.chaguer.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.chaguer.trainy.R

@Destination<RootGraph>
@Composable
fun EditExerciseScreen(
    vm: ExerciseViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    exerciseId: Long
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val uiTitleState by vm.uiTitleState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.edit(exerciseId)
        vm.titleBuilder.setScreenNameId(R.string.edit_exercise)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp) // 🔹 Moins d'espace entre les champs
    ) {
        // ✅ Titre avec flèche de retour
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navigator.popBackStack() }) { // 🔙 Flèche de retour
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Retour",
                    tint = Color(0xFF673AB7)
                )
            }
            Text(
                text = "✍ Modifier un exercice",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        StateScreen(state = uiState) { content ->

            FieldWithIcon(
                icon = Icons.Default.FitnessCenter, // 🏋️ Icône pour le nom
                content = {
                    OutlinedTextFieldWrapper(
                        field = content.nameState,
                        onValueChange = { vm.send(ExerciseViewModel.UIEvent.NameChanged(it)) },
                        labelId = R.string.exercise_name,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )

            FieldWithIcon(
                icon = Icons.Default.List, // 📝 Icône pour la description
                content = {
                    OutlinedTextFieldWrapper(
                        field = content.descriptionState,
                        onValueChange = { vm.send(ExerciseViewModel.UIEvent.DescriptionChanged(it)) },
                        labelId = R.string.exercise_description,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )

            FieldWithIcon(
                icon = Icons.Default.Timer, // ⏳ Icône pour la durée
                content = {
                    OutlinedIntFieldWrapper(
                        field = content.durationState,
                        onValueChange = { vm.send(ExerciseViewModel.UIEvent.DurationChanged(it)) },
                        labelId = R.string.exercise_duration,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )

            FieldWithIcon(
                icon = Icons.Default.Repeat, // 🔄 Icône pour les répétitions
                content = {
                    OutlinedIntFieldWrapper(
                        field = content.repetitionsState,
                        onValueChange = { vm.send(ExerciseViewModel.UIEvent.RepetitionsChanged(it)) },
                        labelId = R.string.exercise_repetitions,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }

        // ✅ Bouton "Modifier" bien visible
        Button(
            onClick = {
                vm.send(ExerciseViewModel.UIEvent.SaveChanges)
                navigator.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(50.dp), // 🔹 Augmente la taille du bouton
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5E35B1), // 🔹 Violet plus élégant
                contentColor = Color.White
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

/**
 * ✅ Composable pour afficher un champ de saisie avec une icône alignée
 */
@Composable
fun FieldWithIcon(
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF673AB7),
            modifier = Modifier.padding(end = 8.dp)
        )
        content()
    }
}