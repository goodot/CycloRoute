package me.tatocaster.stravagraph.features.home.presentation

import com.sweetzpot.stravazpot.activity.model.Activity
import io.reactivex.subscribers.ResourceSubscriber
import me.tatocaster.stravagraph.features.home.usecase.HomeUseCase
import javax.inject.Inject

class HomePresenter @Inject constructor(private var useCase: HomeUseCase,
                                        private var view: HomeContract.View) : HomeContract.Presenter {


    override fun getStravaActivities(token: String) {
        val subscriber = object : ResourceSubscriber<MutableList<Activity>>() {
            override fun onNext(t: MutableList<Activity>) {
                view.showActivitiesList(t)
            }

            override fun onError(t: Throwable) {
                view.showError(t.message!!)
            }

            override fun onComplete() {

            }
        }
        useCase.execute(token, subscriber)
    }

    override fun detach() {
        useCase.clear()
    }

}