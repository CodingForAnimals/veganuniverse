@file:OptIn(ExperimentalPermissionsApi::class)

package org.codingforanimals.veganuniverse.core.ui.permissions

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun RequestPermission(
    permission: String,
    onPermissionGranted: @Composable () -> Unit = {},
    onPermissionDenied: @Composable () -> Unit = {},
) {
    var secondAttemptFailed by remember { mutableStateOf(false) }
    val permissionState = rememberPermissionState(permission)

    if (!secondAttemptFailed) {
        when (permissionState.status) {
            is PermissionStatus.Granted -> {
                Log.e("RequestPermission.kt", "PermissionStatus.Granted")
                onPermissionGranted()
            }
            is PermissionStatus.Denied -> {
                if (!permissionState.status.shouldShowRationale) {
                    Log.e("RequestPermission.kt", "PermissionStatus.Denied, rationale false")
                    AlertDialog(
                        onDismissRequest = {},
                        title = { Text("Tu ubicación") },
                        text = { Text("No es necesario pero al permitirnos conocer tu ubicación tendrás una mejor experiencia con nuestro mapa") },
                        confirmButton = {
                            Button(
                                onClick = { permissionState.launchPermissionRequest() },
                                content = {
                                    Text("Ver opciones")
                                },
                            )
                        }
                    )
                } else {
                    Log.e("RequestPermission.kt", "PermissionStatus.Denied, rationale true")
                    if (!secondAttemptFailed) {
                        AlertDialog(
                            onDismissRequest = {},
                            title = { Text("No te volveremos a preguntar") },
                            text = { Text("Recuerda que no es necesario, pero si vuelves a denegar el acceso a tu ubicación y luego quieres habilitarla deberás hacerlo a través de la configuración de tu Android") },
                            dismissButton = {
                                Button(onClick = {
                                    secondAttemptFailed = true
                                }) {
                                    Text("No permitir")
                                }
                            },
                            confirmButton = {
                                Button(onClick = {
                                    secondAttemptFailed = true
                                    permissionState.launchPermissionRequest()
                                }) {
                                    Text("Ver opciones")
                                }
                            }
                        )
                    }
//                onPermissionDenied()
                }
            }
        }
    }
}


//@Composable
//fun RequestPermission(
//    permission: String,
//    onPermissionGranted: @Composable () -> Unit,
//    onPermissionRejected: @Composable () -> Unit,
//    deniedMessage: String = "Give this app a permission to proceed. If it doesn't work, then you'll have to do it manually from the settings.",
//    rationaleMessage: String = "To use this app's functionalities, you need to give us the permission.",
//) {
//    val permissionState = rememberPermissionState(permission)
//
//    when (permissionState.status) {
//        is PermissionStatus.Granted -> {
//            onPermissionGranted()
//        }
//        is PermissionStatus.Denied -> {
//            PermissionDeniedContent(
//                deniedMessage = deniedMessage,
//                rationaleMessage = rationaleMessage,
//                shouldShowRationale = permissionState.status.shouldShowRationale,
//                onRequestPermission = { permissionState.launchPermissionRequest() },
//                onPermissionRejected = onPermissionRejected,
//            )
//        }
//    }
//}
//
//
//@Composable
//fun PermissionDeniedContent(
//    deniedMessage: String,
//    rationaleMessage: String,
//    shouldShowRationale: Boolean,
//    onRequestPermission: () -> Unit,
//    onPermissionRejected: @Composable () -> Unit
//) {
//    if (shouldShowRationale) {
//        AlertDialog(
//            onDismissRequest = {},
//            title = {
//                Text(
//                    text = "Permission Request",
//                    style = TextStyle(
//                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//            },
//            text = {
//                Text(rationaleMessage)
//            },
//            confirmButton = {
//                Button(onClick = onRequestPermission) {
//                    Text("Give Permission")
//                }
//            }
//        )
//    } else {
//        Content(text = deniedMessage, onClick = onRequestPermission)
//    }
//}
//
//@Composable
//fun Content(text: String, showButton: Boolean = true, onClick: () -> Unit) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(50.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = text, textAlign = TextAlign.Center)
//        Spacer(modifier = Modifier.height(12.dp))
//        if (showButton) {
//            Button(onClick = onClick) {
//                Text(text = "Request")
//            }
//        }
//    }
//}