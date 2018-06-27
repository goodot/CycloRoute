package me.tatocaster.stravagraph.features.create.presentation

interface CreateActivityContract {
    interface View {
        fun showError(message: String)
    }

    interface Presenter {
        fun detach()
    }
}