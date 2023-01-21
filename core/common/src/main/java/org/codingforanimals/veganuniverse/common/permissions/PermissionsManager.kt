package org.codingforanimals.veganuniverse.common.permissions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.dsl.module

interface PermissionsManager {
    val denied: Flow<List<VeganUniversePermissions>>
}

class PermissionsManagerImpl : PermissionsManager {
    override val denied: Flow<List<VeganUniversePermissions>>
        get() = flow { emit(listOf(VeganUniversePermissions.USER_LOCATION)) }
}

enum class VeganUniversePermissions(vararg permissions: String) {
    USER_LOCATION(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
}

val permissionsManagerModule = module {
    single<PermissionsManager> { PermissionsManagerImpl() }
}