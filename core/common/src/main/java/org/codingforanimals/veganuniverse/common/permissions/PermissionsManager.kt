package org.codingforanimals.veganuniverse.common.permissions

import kotlinx.coroutines.flow.Flow

interface PermissionsManager {
    val granted: Flow<List<VeganUniversePermissions>>
}

enum class VeganUniversePermissions(vararg permissions: String) {
    USER_LOCATION(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
}