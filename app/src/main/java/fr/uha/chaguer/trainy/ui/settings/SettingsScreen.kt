package fr.uha.chaguer.trainy.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.ui.theme.MontserratFont

@Destination<RootGraph>
@Composable
fun SettingsScreen (
    vm : SettingsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.settings),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFont
            )
            Divider(
                color = Color(0xFF673AB7),
                thickness = 2.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
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