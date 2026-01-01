package co.kluvaka.cmp.features.auth.ui.profile

sealed interface ProfileOperation {
    sealed interface Actions : ProfileOperation {
        data object Logout : Actions
        data object LoadProfile : Actions
    }
}

