package org.codingforanimals.veganuniverse.ui.icon

import org.codingforanimals.veganuniverse.core.ui.R

object VUImages {
    val ErrorCat = Image.DrawableResourceImage(R.drawable.img_cat_error)
}

sealed class Image {
    data class DrawableResourceImage(val resId: Int) : Image()
}