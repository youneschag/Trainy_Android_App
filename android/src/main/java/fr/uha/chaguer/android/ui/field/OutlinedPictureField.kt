package fr.uha.chaguer.android.ui.field

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import fr.uha.chaguer.android.R
import fr.uha.chaguer.android.ui.app.AppTitle
import java.lang.reflect.Field
import kotlin.reflect.KClass

@Suppress("ArrayInDataClass")
data class PictureFieldConfig (
    val resourceDir : Array<KClass<*>>? = null,
    val resourceFilter : (String) -> Boolean = { true },
    val resourceUriProvider : ((Int) -> Uri)? = null,
    val galleryFilter : String? = null,
    val newImageUriProvider : (() -> Uri)? = null,
)

class TakePictureWithUriContract : ActivityResultContract<Uri, Pair<Boolean, Uri>>() {

    private lateinit var imageUri: Uri

    @CallSuper
    override fun createIntent(context: Context, input: Uri): Intent {
        imageUri = input
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, input)
    }

    override fun getSynchronousResult(
        context: Context,
        input: Uri
    ): SynchronousResult<Pair<Boolean, Uri>>? = null

    @Suppress("AutoBoxing")
    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Boolean, Uri> {
        return (resultCode == Activity.RESULT_OK) to imageUri
    }
}

private fun getAllResourceFields (
    from : KClass<*>,
    filter : (String) -> Boolean = { true }
) : List<Field> {
    return from.java.fields.filterNot { it.name.contains("launcher") }.filter { filter(it.name) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawablePicker (
    from : Array<KClass<*>>,
    filter : (String) -> Boolean,
    onSelected: (Int?) -> Unit,
    @StringRes titleId : Int,
) {
    val entries = from.flatMap { getAllResourceFields(it, filter) }
    Dialog(onDismissRequest = { onSelected(null) }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { AppTitle(screenTitleId = titleId) },
                )
            }
        ) { innerPadding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(entries) { entry ->
                    val id : Int? = try { entry.getInt(Int) } catch (e : Exception) { null }
                    if (id != null) {
                        IconButton(
                            onClick = { onSelected(id) }
                        ) {
                            Icon(painterResource(id = id), contentDescription = null, tint = Color.Unspecified)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun InternalPictureContent(
    value : Uri?,
    onValueChange: (Uri?) -> Unit,
    config : PictureFieldConfig,
    modifier : Modifier = Modifier,
) {
    val showDrawablePicker = remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDrawablePicker.value ) {
        if (config.resourceDir != null && config.resourceUriProvider != null) {
            DrawablePicker(
                config.resourceDir,
                config.resourceFilter,
                onSelected = { id -> showDrawablePicker.value = false
                    if (id != null) onValueChange(config.resourceUriProvider.let { it(id) })
                },
                titleId = R.string.title_drawable_picker,
            )
        } else {
            showDrawablePicker.value = false
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                context.grantUriPermission(context.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION )
            }
            onValueChange(uri)
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = TakePictureWithUriContract(),
        onResult = { success -> if (success.first) onValueChange(success.second) }
    )

    Row(modifier = modifier) {
        AsyncImage(
            model = value,
            modifier = Modifier.size(128.dp),
            contentDescription = "Selected image",
            error = rememberVectorPainter(Icons.Outlined.Error),
            placeholder = rememberVectorPainter(Icons.Outlined.Casino),
            contentScale = ContentScale.Fit
        )
        Column {
            Button(onClick = { onValueChange(null) }) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete picture")
            }
            Row (horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (config.galleryFilter != null) {
                    Button(onClick = { imagePicker.launch(arrayOf(config.galleryFilter)) }) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoLibrary,
                            contentDescription = "picture gallery"
                        )
                    }
                }
                if (config.resourceDir != null && config.resourceUriProvider != null) {
                    Button(onClick = { showDrawablePicker.value = true }) {
                        Icon(
                            imageVector = Icons.Outlined.ImageSearch,
                            contentDescription = "image gallery"
                        )
                    }
                }
                if (config.newImageUriProvider != null) {
                    Button(
                        onClick = {
                            cameraLauncher.launch(config.newImageUriProvider.let { it() })
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Camera,
                            contentDescription = "take picture"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OutlinedPictureField(
    value : Uri?,
    onValueChange: (Uri?) -> Unit,
    modifier : Modifier = Modifier,
    config : PictureFieldConfig,
    @StringRes labelId: Int? = null,
    @StringRes errorId : Int? = null,
    @StringRes hintId : Int? = null,
) {
    OutlinedDecorator (
        modifier = modifier,
        labelId = labelId,
        errorId = errorId,
        hintId = hintId,
    ) {
        InternalPictureContent(
            value = value,
            onValueChange = onValueChange,
            config = config,
            modifier = modifier,
        )
    }
}

@Composable
fun OutlinedPictureFieldWrapper(
    field : FieldWrapper<Uri>,
    onValueChange: (Uri?) -> Unit,
    config : PictureFieldConfig,
    modifier : Modifier = Modifier,
    @StringRes labelId: Int? = null,
    @StringRes hintId : Int? = null,
) {
    OutlinedPictureField(
        value = field.value,
        onValueChange = onValueChange,
        config = config,
        modifier = modifier,
        labelId = labelId,
        errorId = field.errorId,
        hintId = hintId,
    )
}
