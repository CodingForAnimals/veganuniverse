@file:OptIn(ExperimentalPermissionsApi::class)

package org.codingforanimals.veganuniverse.commons.ui.permissions

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

private const val TAG = "SinglePermissionRequest"

@Composable
fun RequestPermission(
    permission: String,
    onPermissionGranted: @Composable () -> Unit = {},
    onPermissionDenied: @Composable () -> Unit = {},
) {
    val state = rememberPermissionState(permission)

    when (state.status) {
        is PermissionStatus.Denied -> {
            if (state.status.shouldShowRationale) {
                onPermissionDenied()
            } else {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("Tu ubicación") },
                    text = { Text("Tendrás una mejor experiencia con nuestro mapa si nos compartes tu ubicación.") },
                    confirmButton = {
                        Button(
                            onClick = { state.launchPermissionRequest() },
                            content = {
                                Text("Ver opciones")
                            },
                        )
                    }
                )
            }
        }
        PermissionStatus.Granted -> onPermissionGranted()
    }
}
