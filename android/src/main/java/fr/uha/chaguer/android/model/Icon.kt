package fr.uha.chaguer.android.model

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter

/*
   rules to load the source of an Icon
   it can be :
      an id of a resource file either drawable (SVG) or mipmap (PNG)
      a vectorImage (SVG in Kotlin)
      a bitmap (screenshot)
      an Uri (file from Camera)
   tint can be used to force color or a tint is computed according the selected state
 */
data class IconPicture (
    @DrawableRes
    val id : Int?  = null,
    val vector : ImageVector? = null,
    val bitmap : Bitmap? = null,
    val uri : Uri? = null,
    val tint : Color? = null,
)

/*
   rules to load the rendering
   if selected use the focused if not null or the normal
   if not selected use the normal
 */
data class IconRender (
    val focused : IconPicture? = null,
    val unfocused : IconPicture? = null,
)

@Composable
private fun inferPainter (picture : IconPicture?) : Painter? {
    if (picture == null) return null
    if (picture.vector != null) return rememberVectorPainter (image = picture.vector)
    if (picture.id != null) return painterResource (id = picture.id)
    if (picture.bitmap != null) return rememberAsyncImagePainter (model = picture.bitmap)
    if (picture.uri != null) return rememberAsyncImagePainter (model = picture.uri)
    return null
}

@Suppress("KotlinConstantConditions")
@Composable
private fun inferPainter (selected : Boolean, picture : IconRender) : Painter {
    var painter : Painter? = null
    if (painter == null && selected) painter = inferPainter(picture = picture.focused)
    if (painter == null && selected) painter = inferPainter(picture = picture.unfocused)
    if (painter == null && ! selected) painter = inferPainter(picture = picture.unfocused)
    if (painter == null) painter = rememberVectorPainter (image = Icons.Outlined.ErrorOutline)
    return painter
}

@Composable
private fun inferColor (picture : IconPicture?) : Color? {
    if (picture == null) return null
    if (picture.tint != null) return picture.tint
    return null
}

@Suppress("KotlinConstantConditions")
@Composable
private fun inferColor (selected : Boolean, picture : IconRender) : Color? {
    var color : Color? = null
    if (color == null && selected) color = inferColor(picture = picture.focused)
//    if (color == null && selected) color = MaterialTheme.colorScheme.inverseSurface
    if (color == null && ! selected) color = inferColor(picture = picture.unfocused)
//    if (color == null && ! selected) color = Color.Gray
//    if (color == null) color = Color.Gray
    return color
}

@Composable
fun Icon (selected : Boolean, picture : IconRender, modifier: Modifier = Modifier) {
    val painter : Painter = inferPainter(selected, picture)
    val tint : Color? = inferColor(selected, picture)

    if (tint != null) {
        androidx.compose.material3.Icon(
            painter = painter,
            contentDescription = "",
            modifier = modifier,
            tint = tint,
        )
    } else {
        androidx.compose.material3.Icon(
            painter = painter,
            contentDescription = "",
            modifier = modifier,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }

}