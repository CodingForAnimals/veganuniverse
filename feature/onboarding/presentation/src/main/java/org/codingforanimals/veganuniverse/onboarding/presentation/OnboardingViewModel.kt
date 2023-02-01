package org.codingforanimals.veganuniverse.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.common.dispatcher.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.datastore.VeganUniverseDataStore

class OnboardingViewModel(
    private val onboardingDataStoreManager: OnboardingDataStoreManager,
) : ViewModel() {

    private val _sideEffect: Channel<SideEffect> = Channel()
    val sideEffect: Flow<SideEffect> = _sideEffect.receiveAsFlow()

    fun onAction(action: Action) {
        when (action) {
            Action.OnUserDismissOnboardingScreen -> {
                viewModelScope.launch {
                    onboardingDataStoreManager.setOnboardingAsDismissed()
                    _sideEffect.send(SideEffect.DismissOnboardingScreen)
                }
            }
        }
    }

    sealed class Action {
        object OnUserDismissOnboardingScreen : Action()
    }

    sealed class SideEffect {
        object DismissOnboardingScreen : SideEffect()
    }
}

interface OnboardingDataStoreManager {
    suspend fun wasOnboardingDismissed(): Boolean
    suspend fun setOnboardingAsDismissed()

    companion object DataStoreKeys {
        const val OnboardingDismissedKey = "IS_ONBOARDING_DISMISSED"
    }
}

class OnboardingDataStoreManagerImpl(
    private val dataStore: VeganUniverseDataStore,
) : OnboardingDataStoreManager {

    override suspend fun setOnboardingAsDismissed() {
        dataStore.saveValue(OnboardingDataStoreManager.OnboardingDismissedKey, true)
    }

    override suspend fun wasOnboardingDismissed(): Boolean =
        dataStore.readValue(OnboardingDataStoreManager.OnboardingDismissedKey) ?: false

}

class ShowOnboardingUseCase(
    private val onboardingDataStoreManager: OnboardingDataStoreManager,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) {
    suspend operator fun invoke(): Boolean =
        withContext(dispatcherProvider.io()) { !onboardingDataStoreManager.wasOnboardingDismissed() }
}

class SetOnboardingAsDismissedUseCase(
    private val onboardingDataStoreManager: OnboardingDataStoreManager,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) {
    suspend operator fun invoke() =
        withContext(dispatcherProvider.io()) { onboardingDataStoreManager.setOnboardingAsDismissed() }
}