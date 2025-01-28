package fr.uha.chaguer.trainy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.uha.chaguer.android.ui.app.AppBottomBar
import fr.uha.chaguer.trainy.ui.theme.TrainyTheme
import fr.uha.chaguer.trainy.ui.TrainyAppScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Trainy",
                                        color = Color.White // Titre en blanc
                                    )
                                },
                                colors = TopAppBarDefaults.mediumTopAppBarColors(
                                    containerColor = Color(0xFF673AB7) // Violet pour la barre
                                ),
                                actions = {
                                    IconButton(onClick = { /* On traitera l'action plus tard */ }) {
                                        Icon(
                                            imageVector = Icons.Filled.Settings, // Icône de paramètres
                                            contentDescription = "Paramètres",
                                            tint = Color.White // Couleur blanche pour l'icône
                                        )
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = innerPadding.calculateTopPadding() - 50.dp, // Réduit l'espace en haut
                                    bottom = 0.dp // Supprime l’espace blanc sous la Bottom Bar
                                )
                        ) {
                            TrainyAppScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TrainyTheme {
        Greeting("Android")
    }
}