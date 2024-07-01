package org.codingforanimals.veganuniverse.create.home.persentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class CreateHomeViewModel(
    flowOnCurrentUser: FlowOnCurrentUser,
) : ViewModel() {

    val user = flowOnCurrentUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    sealed class Action {
        data object OnCreatePlaceClick : Action()
        data object OnCreateRecipeClick : Action()
        data object OnCreateProductClick : Action()
    }
}