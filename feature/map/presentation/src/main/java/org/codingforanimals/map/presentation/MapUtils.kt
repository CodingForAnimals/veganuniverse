package org.codingforanimals.map.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

private const val TAG = "MapUtils"

@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.locationFlow() = callbackFlow {
    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            try {
                result.lastLocation?.let { trySend(it); close() }
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
            }
        }
    }
    requestLocationUpdates(
        createLocationRequest(),
        callback,
        Looper.getMainLooper()
    ).addOnFailureListener {
        close(it)
    }
    awaitClose {
        removeLocationUpdates(callback)
    }
}

private fun createLocationRequest(): LocationRequest {
    return LocationRequest.create().apply {
        priority = Priority.PRIORITY_HIGH_ACCURACY
    }
}

fun checkIfPermissionGranted(context: Context, permission: String): Boolean =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

fun shouldShowPermissionRationale(context: Context, permission: String): Boolean {
    val activity = context as? Activity?
    if (activity == null) {
        Log.e(TAG, "Activity is null")
    }
    return ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)
}

@Composable
fun PermissionDialog(
    context: Context,
    permission: String,
    permissionRationale: String,
    permissionAction: (PermissionAction) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val isPermissionGranted = checkIfPermissionGranted(context, permission)

    if (isPermissionGranted) {
        permissionAction(PermissionAction.Granted)
        return
    }

    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            permissionAction(PermissionAction.Granted)
        } else {
            permissionAction(PermissionAction.Denied)
        }
    }

    val showPermissionRationale = shouldShowPermissionRationale(context, permission)

    if (showPermissionRationale) {
        LaunchedEffect(showPermissionRationale) {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = permissionRationale,
                actionLabel = "Dar acceso",
                duration = SnackbarDuration.Long,
            )

            when (snackbarResult) {
                SnackbarResult.Dismissed -> {
                    permissionAction(PermissionAction.Denied)
                }
                SnackbarResult.ActionPerformed -> {
                    permissionsLauncher.launch(permission)
                }
            }
        }
    } else {
        SideEffect {
            permissionsLauncher.launch(permission)
        }
    }
}

sealed class PermissionAction {
    object Granted : PermissionAction()
    object Denied : PermissionAction()
}