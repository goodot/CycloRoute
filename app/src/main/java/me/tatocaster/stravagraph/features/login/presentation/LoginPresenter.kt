package me.tatocaster.stravagraph.features.login.presentation

import com.sweetzpot.stravazpot.authenticaton.model.LoginResult
import io.reactivex.subscribers.ResourceSubscriber
import me.tatocaster.stravagraph.features.login.usecase.LoginUseCase
import timber.log.Timber
import javax.inject.Inject

class LoginPresenter @Inject constructor(private var useCase: LoginUseCase,
                                         private var view: LoginContract.View) : LoginContract.Presenter {

    override fun onLoginResult(code: String) {
        val subscriber = object : ResourceSubscriber<LoginResult?>() {
            override fun onNext(t: LoginResult?) {
                Timber.d("loginResult %s", t?.token)
                view.loginSuccessful(t)
            }

            override fun onError(t: Throwable) {
                view.showError(t.message!!)
            }

            override fun onComplete() {

            }
        }
        useCase.execute(code, subscriber)
    }

    override fun detach() {
        useCase.clear()
    }

}