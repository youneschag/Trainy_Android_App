package fr.uha.chaguer.trainy.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.chaguer.trainy.database.FeedDatabase
import fr.uha.chaguer.trainy.database.TrainyDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val database: TrainyDatabase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    fun onClear () = viewModelScope.launch {
        withContext(dispatcher) {
            FeedDatabase(database).clear()
        }
    }

    fun onFill () = viewModelScope.launch {
        withContext(dispatcher) {
            FeedDatabase(database).populate(0)
        }
    }

}