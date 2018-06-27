package me.tatocaster.stravagraph.features.login.usecase

import com.sweetzpot.stravazpot.authenticaton.api.AuthenticationAPI
import com.sweetzpot.stravazpot.authenticaton.model.AppCredentials
import com.sweetzpot.stravazpot.common.api.AuthenticationConfig
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import me.tatocaster.stravagraph.STRAVA_CLIENT_ID
import me.tatocaster.stravagraph.STRAVA_CLIENT_SECRET
import me.tatocaster.stravagraph.common.UseCase
import me.tatocaster.stravagraph.di.executor.PostExecutionThread
import me.tatocaster.stravagraph.di.executor.ThreadExecutor
import javax.inject.Inject

class LoginUseCase
@Inject
constructor(threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {
    lateinit var code: String
    fun execute(code: String, useCaseObserver: ResourceSubscriber<*>) {
        this.code = code
        super.execute(useCaseObserver)
    }

    override fun buildUseCaseFlowable(): Flowable<*> {
        val config = AuthenticationConfig.create()
                .debug()
                .build()
        val api = AuthenticationAPI(config)

        return Flowable.fromCallable {
            api.getTokenForApp(AppCredentials.with(STRAVA_CLIENT_ID, STRAVA_CLIENT_SECRET))
                    .withCode(code)
                    .execute()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        /*val useCaseObservable = loginRepository.login()
        this.code = ""
        return useCaseObservable*/
    }
}