package fr.uha.chaguer.trainy.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.ui.app.AppTopBar
import fr.uha.chaguer.android.ui.app.UITitleState
import fr.uha.chaguer.trainy.R

@Destination<RootGraph>
@Composable
fun SettingsScreen (
    vm : SettingsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    Scaffold (
        topBar = { AppTopBar(UITitleState(screenNameId = R.string.settings)) },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Button(
                onClick = { vm.onClear() }
            ) {
                Text("Clear")
            }
            Button(
                onClick = { vm.onFill() }
            ) {
                Text("Fill")
            }
        }
    }
}