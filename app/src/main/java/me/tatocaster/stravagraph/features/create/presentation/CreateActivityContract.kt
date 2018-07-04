package me.tatocaster.stravagraph.features.create.presentation

import java.io.File

interface CreateActivityContract {
    interface View {
        fun showError(message: String)

        fun onGetImageFile(file: File)
    }

    interface Presenter {
        fun detach()

        fun onChooseImageResult(imageName: String)
    }
}