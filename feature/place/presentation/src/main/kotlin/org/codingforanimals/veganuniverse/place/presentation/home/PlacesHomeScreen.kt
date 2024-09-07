@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.place.presentation.home

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.ui.topbar.HomeScreenTopAppBar
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.home.PlacesHomeViewModel.Action
import org.codingforanimals.veganuniverse.place.presentation.home.PlacesHomeViewModel.FilterState
import org.codingforanimals.veganuniverse.place.presentation.home.PlacesHomeViewModel.PlacesState
import org.codingforanimals.veganuniverse.place.presentation.home.PlacesHomeViewModel.SideEffect
import org.codingforanimals.veganuniverse.place.presentation.home.PlacesHomeViewModel.UiState
import org.codingforanimals.veganuniverse.place.presentation.home.PlacesHomeViewModel.UserLocationState
import org.codingforanimals.veganuniverse.place.presentation.home.bottomsheet.ErrorSheet
import org.codingforanimals.veganuniverse.place.presentation.home.bottomsheet.LoadingSheet
import org.codingforanimals.veganuniverse.place.presentation.home.composables.PlacesMap
import org.codingforanimals.veganuniverse.place.presentation.home.bottomsheet.SuccessSheet
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.CancellationException

@Composable
internal fun PlacesHomeScreen(
    navigateUp: () -> Unit,
    navigateToPlaceDetails: (String) -> Unit,
    viewModel: PlacesHomeViewModel = koinViewModel(),
) {
    BackHandler(onBack = { viewModel.onAction(Action.HandleBackGesture) })

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false,
        )
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val placesState by viewModel.placesState.collectAsStateWithLifecycle()
    val filtersState by viewModel.filtersState.collectAsStateWithLifecycle()
    val userLocationState by viewModel.userLocationState.collectAsStateWithLifecycle()

    PlacesHomeScreen(
        uiState = uiState,
        placesState = placesState,
        filtersState = filtersState,
        userLocationState = userLocationState,
        onAction = viewModel::onAction,
        scaffoldState = scaffoldState,
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        cameraPositionState = uiState.cameraPositionState,
        scaffoldState = scaffoldState,
        onSettingsScreenDismissed = { viewModel.onAction(Action.OnSettingsScreenDismissed) },
        onEnableLocationActivityResult = { viewModel.onEnableLocationResult(it) },
        navigateToPlaceDetails = navigateToPlaceDetails,
        onLocationFromOverlaySelected = { viewModel.onAction(Action.OnLocationFromOverlaySelected(it)) },
    )

    HandleNavigationEffects(
        navigationEffects = viewModel.navigationEffects,
        navigateUp = navigateUp,
    )
}

@Composable
private fun PlacesHomeScreen(
    uiState: UiState,
    placesState: PlacesState,
    filtersState: FilterState,
    userLocationState: UserLocationState,
    scaffoldState: BottomSheetScaffoldState,
    onAction: (Action) -> Unit,
) {
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { state -> SnackbarHost(hostState = state) },
        topBar = {
            HomeScreenTopAppBar(
                title = stringResource(R.string.places_home_title),
                onBackClick = { onAction(Action.OnBackClick) },
            )
        },
        sheetPeekHeight = 300.dp,
        sheetContent = {
            when (placesState) {
                is PlacesState.Success -> SuccessSheet(
                    placesState = placesState,
                    filterState = filtersState,
                    selectedPlace = uiState.selectedPlace,
                    isFocused = uiState.isFocused,
                    onAction = onAction,
                )

                PlacesState.Error -> ErrorSheet()
                PlacesState.Loading -> LoadingSheet()
            }
        },
        content = {
            PlacesMap(
                placesState = placesState,
                uiState = uiState,
                userLocationState = userLocationState,
                onAction = onAction,
            )
        },
    )
}

@Composable
private fun HandleNavigationEffects(
    navigationEffects: Flow<PlacesHomeViewModel.NavigationEffect>,
    navigateUp: () -> Unit,
) {
    LaunchedEffect(Unit) {
        navigationEffects.onEach { effect ->
            when (effect) {
                PlacesHomeViewModel.NavigationEffect.NavigateUp -> navigateUp()
            }
        }.collect()
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    cameraPositionState: CameraPositionState,
    scaffoldState: BottomSheetScaffoldState,
    onSettingsScreenDismissed: () -> Unit,
    onEnableLocationActivityResult: (Int) -> Unit,
    navigateToPlaceDetails: (String) -> Unit,
    onLocationFromOverlaySelected: (ActivityResult) -> Unit,
) {

    val enableLocationActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { onEnableLocationActivityResult(it.resultCode) },
    )

    val selectLocationOverlayLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = onLocationFromOverlaySelected,
    )

    val appSettingsActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { onSettingsScreenDismissed() },
    )

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sideEffects.onEach { effect ->
            when (effect) {
                is SideEffect.NavigateToPlaceDetails -> navigateToPlaceDetails(effect.id)
                is SideEffect.ZoomInLocation -> {
                    try {
                        cameraPositionState.animate(effect.cameraUpdate, effect.duration)
                        delay(500)
                        effect.animationSuccessCallback?.invoke()
                    } catch (e: CancellationException) {
                        Log.d(TAG, e.stackTraceToString())
                        delay(500)
                        effect.animationErrorCallback?.invoke()
                    }
                }

                is SideEffect.ShowSnackbar -> launch {
                    val res = scaffoldState.snackbarHostState.showSnackbar(
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
                        Log.d(TAG, e.stackTraceToString())
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

private const val TAG = "PlacesHomeScreen"
