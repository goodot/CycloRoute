package me.tatocaster.stravagraph.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.tatocaster.stravagraph.features.base.BaseActivity
import me.tatocaster.stravagraph.features.base.BaseActivityModule
import me.tatocaster.stravagraph.features.create.presentation.CreateActivity
import me.tatocaster.stravagraph.features.create.presentation.CreateActivityModule
import me.tatocaster.stravagraph.features.home.presentation.HomeActivity
import me.tatocaster.stravagraph.features.home.presentation.HomeActivityModule
import me.tatocaster.stravagraph.features.login.presentation.LoginActivity
import me.tatocaster.stravagraph.features.login.presentation.LoginActivityModule

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [LoginActivityModule::class])
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [HomeActivityModule::class])
    abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector(modules = [CreateActivityModule::class])
    abstract fun bindCreateActivity(): CreateActivity

    @ContributesAndroidInjector(modules = [BaseActivityModule::class])
    abstract fun bindBaseActivity(): BaseActivity
}
