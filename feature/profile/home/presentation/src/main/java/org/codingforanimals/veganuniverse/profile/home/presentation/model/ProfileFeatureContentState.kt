package org.codingforanimals.veganuniverse.profile.home.presentation.model

sealed class ProfileFeatureContentState<out T : Any> {
    data object Loading : ProfileFeatureContentState<Nothing>()
    data object Error : ProfileFeatureContentState<Nothing>()
    data class Success<out T : Any>(val items: List<T>) : ProfileFeatureContentState<T>()
}