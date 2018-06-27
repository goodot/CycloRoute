package me.tatocaster.stravagraph.features.login.presentation

import com.sweetzpot.stravazpot.authenticaton.model.LoginResult

interface LoginContract {
    interface View {
        fun showError(message: String)

        fun loginSuccessful(loginResult: LoginResult?)
    }

    interface Presenter {
        fun onLoginResult(code: String)
        fun detach()
    }
}