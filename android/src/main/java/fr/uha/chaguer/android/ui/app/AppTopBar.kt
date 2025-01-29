package fr.uha.chaguer.android.ui.app

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.chaguer.android.flow.combine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.TreeMap

data class UITitleState (
    val appNameId : Int? = null,
    val screenNameId : Int? = null,
    val documentName : String? = null,
    val isModified : Boolean? = null,
    val isSavable : Boolean? = null,
    val menuEntries : List<AppMenuEntry> = emptyList()
)

class UITitleBuilder {

    private val _appNameId: MutableStateFlow<Int?> = MutableStateFlow(null)

    fun setAppNameId (sid : Int ?) {
        _appNameId.value = sid
    }

    private val _screenNameId: MutableStateFlow<Int?> = MutableStateFlow(null)

    fun setScreenNameId (sid : Int ?) {
        _screenNameId.value = sid
    }

    private val _defaultDocumentName: MutableStateFlow<String?> = MutableStateFlow(null)

    fun setDefaultDocumentName (dn : String ?) {
        _defaultDocumentName.value = dn
    }

    private val _documentName: MutableStateFlow<String?> = MutableStateFlow(null)

    fun setDocumentName (dn : String ?) {
        _documentName.value = dn
    }

    private val _isModified: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    fun setModified (m : Boolean?) {
        _isModified.value = m
    }

    private val _hasError: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    fun setError (e : Boolean?) {
        _hasError.value = e
    }

    private val _menuEntries : MutableStateFlow<TreeMap<String, AppMenuEntry>> = MutableStateFlow(TreeMap())

    fun contributeToMenuEntries (newContributions : List<AppMenuEntry>) {
        val newEntries = TreeMap<String, AppMenuEntry>()
        _menuEntries.value.forEach { newEntries[it.key] = it.value }
        newContributions.forEach { newEntries[it.key] = it }
        _menuEntries.value = newEntries
    }

    fun unContributeToMenuEntries (newContributions : List<String>) {
        val newEntries = TreeMap<String, AppMenuEntry>()
        _menuEntries.value.forEach { newEntries[it.key] = it.value }
        newContributions.forEach { newEntries.remove(it) }
        _menuEntries.value = newEntries
    }

    val uiTitleState : Flow<UITitleState> = combine (
        _appNameId, _screenNameId, _defaultDocumentName, _documentName,
        _isModified, _hasError,
        _menuEntries
    ) { a, s, d, n, i, e, m ->
        val name = when {
            n == null -> d
            n.isEmpty() -> d
            n.isBlank() -> d
            else -> n
        }
        val savable = when {
            e == null -> false
            i == null -> false
            else -> !e && i
        }
        UITitleState(a, s, name, i, savable, m.values.toList())
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    uiTitleState: UITitleState,
    navigator: DestinationsNavigator? = null,
    menuEntries: List<AppMenuEntry> = emptyList()
) {
    TopAppBar(
        title = { AppTitle(
            appNameId = uiTitleState.appNameId,
            screenTitleId = uiTitleState.screenNameId,
            documentName = uiTitleState.documentName,
            isModified = uiTitleState.isModified ?: false,
        ) },
        navigationIcon = { if (navigator != null) AppUp(onClick = { navigator.popBackStack() }) },
        actions = { AppMenu(menuEntries + uiTitleState.menuEntries) },
        windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
    )
}

