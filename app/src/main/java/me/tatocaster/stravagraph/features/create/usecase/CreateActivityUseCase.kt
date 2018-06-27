package me.tatocaster.stravagraph.features.create.usecase

import com.sweetzpot.stravazpot.activity.api.ActivityAPI
import com.sweetzpot.stravazpot.common.api.StravaConfig
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import me.tatocaster.stravagraph.common.UseCase
import me.tatocaster.stravagraph.di.executor.PostExecutionThread
import me.tatocaster.stravagraph.di.executor.ThreadExecutor
import javax.inject.Inject

class CreateActivityUseCase
@Inject
constructor(threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {
    lateinit var code: String
    fun execute(code: String, useCaseObserver: ResourceSubscriber<*>) {
        this.code = code
        super.execute(useCaseObserver)
    }

    override fun buildUseCaseFlowable(): Flowable<*> {
        val config = StravaConfig.withToken(code).debug().build()
        val activityApi = ActivityAPI(config)

        return Flowable.fromCallable {
            activityApi.listMyActivities()
                    .perPage(40)
                    .execute()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}