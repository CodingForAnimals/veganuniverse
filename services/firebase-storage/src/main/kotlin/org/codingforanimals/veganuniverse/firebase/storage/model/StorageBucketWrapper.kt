package org.codingforanimals.veganuniverse.firebase.storage.model

import android.content.Context
import org.codingforanimals.veganuniverse.commons.firebase.storage.R

internal class StorageBucketWrapper(
    context: Context,
) {
    val storageBucket = context.getString(R.string.firebase_storage_bucket)
}
