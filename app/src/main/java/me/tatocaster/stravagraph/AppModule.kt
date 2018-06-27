package me.tatocaster.stravagraph

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import me.tatocaster.stravagraph.di.executor.JobExecutor
import me.tatocaster.stravagraph.di.executor.PostExecutionThread
import me.tatocaster.stravagraph.di.executor.ThreadExecutor
import me.tatocaster.stravagraph.di.executor.UIThread

/*
@Module
class AppModule(application: Application) {

    private var appContext: Context = application.baseContext

    @Provides
    @Singleton
    fun provideApplication(): Context = appContext

    @Provides
    @Singleton
    fun provideAppResources(): Resources = appContext.resources

    @Provides
    @Singleton
    fun provideLocale(): Locale = Locale.getDefault()

    @Provides
    @Singleton
    internal fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor = jobExecutor

    @Provides
    @Singleton
    internal fun providePostExecutionThread(uiThread: UIThread): PostExecutionThread = uiThread

}*/

@Module
abstract class AppModule {

    @Binds
    abstract fun provideContext(application: Application): Context

    @Binds
    abstract fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor

    @Binds
    abstract fun providePostExecutionThread(uiThread: UIThread): PostExecutionThread

}