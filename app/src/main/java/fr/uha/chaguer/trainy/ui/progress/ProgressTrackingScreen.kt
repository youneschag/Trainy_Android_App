package fr.uha.chaguer.trainy.ui.progress

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import fr.uha.chaguer.android.ui.app.AppTopBar
import fr.uha.chaguer.trainy.R

@Destination<RootGraph>
@Composable
fun ProgressTrackingScreen() {
    Scaffold(
        ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Statistiques",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Placeholder for progress data
            Text(
                text = "Exemple : 10% de progression dans la routine 'Cardio'",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}