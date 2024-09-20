package org.codingforanimals.veganuniverse.commons.ui.share

import android.content.Intent

fun getShareIntent(
    textToShare: String,
    title: String? = null,
): Intent {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, textToShare)
        type = "text/plain"
    }
    return Intent.createChooser(intent, title)
}