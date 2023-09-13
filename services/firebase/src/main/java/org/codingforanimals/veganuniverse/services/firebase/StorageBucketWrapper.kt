package org.codingforanimals.veganuniverse.services.firebase

import android.content.Context

class StorageBucketWrapper(
    context: Context,
) {
    val storageBucket = context.getString(R.string.firebase_storage_bucket)
}