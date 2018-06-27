package me.tatocaster.stravagraph.features.create.presentation

import dagger.Module
import dagger.Provides

@Module
class CreateActivityModule {
    @Provides
    fun providesView(view: CreateActivity): CreateActivityContract.View = view

    @Provides
    fun providesPresenter(presenter: CreateActivityPresenter): CreateActivityContract.Presenter = presenter
}