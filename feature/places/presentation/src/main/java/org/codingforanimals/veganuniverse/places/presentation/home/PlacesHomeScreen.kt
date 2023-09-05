@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.places.presentation.home

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.google.maps.android.compose.CameraPositionState
import java.util.concurrent.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.core.ui.lifecycle.LifecycleObserver
import org.codingforanimals.veganuniverse.places.presentation.home.PlacesHomeViewModel.Action
import org.codingforanimals.veganuniverse.places.presentation.home.PlacesHomeViewModel.SideEffect
import org.codingforanimals.veganuniverse.places.presentation.home.bottomsheet.ErrorSheet
import org.codingforanimals.veganuniverse.places.presentation.home.bottomsheet.LoadingSheet
import org.codingforanimals.veganuniverse.places.presentation.home.bottomsheet.PlacesHomeScreenContent
import org.codingforanimals.veganuniverse.places.presentation.home.bottomsheet.SuccessSheet
import org.codingforanimals.veganuniverse.places.presentation.home.state.PlacesState
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PlacesHomeScreen(
    navigateUp: () -> Unit,
    snackbarHostState: SnackbarHostState,
    navigateToPlaceDetails: (String) -> Unit,
    viewModel: PlacesHomeViewModel = koinViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val uiState = viewModel.uiState

    BackHandler(onBack = { viewModel.onAction(Action.OnBackClick) })

    val enableLocationActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { activityResult -> viewModel.onEnableLocationResult(activityResult.resultCode) }
    )

    val selectLocationOverlayLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { viewModel.onAction(Action.OnLocationFromOverlaySelected(it)) },
    )

    val appSettingsActivityLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { viewModel.onAction(Action.OnSettingsScreenDismissed) })

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = uiState.initialSheetValue,
            skipHiddenState = false,
        )
    )

    LifecycleObserver(
        lifecycleOwner = lifecycleOwner,
        onStop = { viewModel.saveState(scaffoldState.bottomSheetState.currentValue) },
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        cameraPositionState = uiState.cameraPositionState,
        snackbarHostState = snackbarHostState,
        appSettingsActivityLauncher = appSettingsActivityLauncher,
        enableLocationActivityLauncher = enableLocationActivityLauncher,
        scaffoldState = scaffoldState,
        navigateToPlaceDetails = navigateToPlaceDetails,
        selectLocationOverlayLauncher = selectLocationOverlayLauncher,
        navigateUp = navigateUp,
    )

    PlacesHomeScreen(
        uiState = uiState,
        onMapLoaded = { viewModel.onMapLoaded() },
        onAction = viewModel::onAction,
        scaffoldState = scaffoldState,
    )
}

@Composable
private fun PlacesHomeScreen(
    uiState: PlacesHomeViewModel.UiState,
    scaffoldState: BottomSheetScaffoldState,
    onMapLoaded: () -> Unit,
    onAction: (Action) -> Unit,
) {
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { state -> SnackbarHost(hostState = state) },
        sheetPeekHeight = 300.dp,
//        sheetSwipeEnabled = !uiState.isFocused,
        sheetContent = {
            when (val placesState = uiState.placesState) {
                is PlacesState.Success -> SuccessSheet(
                    placesState = placesState,
                    filterState = uiState.filterState,
                    selectedPlace = uiState.selectedPlace,
                    isFocused = uiState.isFocused,
                    onAction = onAction,
                )

                PlacesState.Error -> ErrorSheet()
                PlacesState.Loading -> LoadingSheet()
            }
        },
        content = {
            PlacesHomeScreenContent(
                uiState = uiState,
                onMapLoaded = onMapLoaded,
                onAction = onAction,
            )
        },
    )
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    cameraPositionState: CameraPositionState,
    scaffoldState: BottomSheetScaffoldState,
    snackbarHostState: SnackbarHostState,
    appSettingsActivityLauncher: ActivityResultLauncher<Intent>,
    enableLocationActivityLauncher: ActivityResultLauncher<IntentSenderRequest>,
    navigateToPlaceDetails: (String) -> Unit,
    selectLocationOverlayLauncher: ActivityResultLauncher<Intent>,
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sideEffects.onEach { effect ->
            when (effect) {
                SideEffect.NavigateUp -> navigateUp()
                is SideEffect.NavigateToPlaceDetails -> navigateToPlaceDetails(effect.id)
                is SideEffect.ZoomInLocation -> {
                    try {
                        cameraPositionState.animate(effect.cameraUpdate, effect.duration)
                        effect.animationSuccessCallback?.invoke()
                    } catch (e: CancellationException) {
                        Log.d("PlacesHomeScreen.kt", e.stackTraceToString())
                        effect.animationErrorCallback?.invoke()
                    }
                }

                is SideEffect.ShowSnackbar -> launch {
                    val res = snackbarHostState.showSnackbar(
                        message = effect.snackbar.text,
                        duration = SnackbarDuration.Long,
                        actionLabel = effect.snackbar.actionLabel
                    )
                    when (res) {
                        SnackbarResult.Dismissed -> Unit
                        SnackbarResult.ActionPerformed -> {
                            appSettingsActivityLauncher.launch(Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", context.packageName, null)
                            })
                        }

                    }
                }

                SideEffect.PartiallyExpand -> {
                    try {
                        scaffoldState.bottomSheetState.partialExpand()
                    } catch (e: Throwable) {
                        Log.d("PlacesHomeScreen.kt", e.stackTraceToString())
                    }
                }

                is SideEffect.LocationServiceEnableRequest -> {
                    enableLocationActivityLauncher.launch(effect.intent)
                }

                is SideEffect.OpenLocationOnlyAutocompleteOverlay -> {
                    selectLocationOverlayLauncher.launch(effect.intent)
                }
            }
        }.collect()
    }
}
