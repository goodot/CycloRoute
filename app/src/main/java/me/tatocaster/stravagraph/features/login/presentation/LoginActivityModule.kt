package me.tatocaster.stravagraph.features.login.presentation

import dagger.Module
import dagger.Provides

@Module
class LoginActivityModule {
    @Provides
    fun providesView(view: LoginActivity): LoginContract.View = view

    @Provides
    fun providesPresenter(presenter: LoginPresenter): LoginContract.Presenter = presenter
}