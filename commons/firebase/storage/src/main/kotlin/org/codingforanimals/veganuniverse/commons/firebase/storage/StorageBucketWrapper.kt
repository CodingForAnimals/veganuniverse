package org.codingforanimals.veganuniverse.commons.firebase.storage

import android.content.Context

internal class StorageBucketWrapper(
    context: Context,
) {
    val storageBucket = context.getString(R.string.firebase_storage_bucket)
}
