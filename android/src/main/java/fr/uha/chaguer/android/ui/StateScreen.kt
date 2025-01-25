package fr.uha.chaguer.android.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.uha.chaguer.android.R
import fr.uha.chaguer.android.viewmodel.Result

@Composable
fun LoadingScreen(
    text: String
) {
    Text(text = text, modifier = Modifier.padding(vertical = 16.dp))
    CircularProgressIndicator()
}

@Composable
fun ErrorScreen(
    text: String
) {
    Text(text = text, modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.error)
}

@Composable
fun <S> StateScreen(
    state: Result<S>,
    loadingScreen: @Composable (l: Result.Loading) -> Unit = { LoadingScreen(text = stringResource(id = R.string.loading)) },
    errorScreen: @Composable (e: Result.Error) -> Unit = { ErrorScreen(text = stringResource(id = R.string.error)) },
    successScreen: @Composable (state: S) -> Unit,
) {
    when (state) {
        Result.Loading -> loadingScreen(state as Result.Loading)
        is Result.Error -> errorScreen(state)
        is Result.Success -> successScreen(state.content)
    }
}