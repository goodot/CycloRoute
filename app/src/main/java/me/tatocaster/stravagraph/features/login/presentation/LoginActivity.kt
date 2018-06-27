package me.tatocaster.stravagraph.features.login.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.sweetzpot.stravazpot.authenticaton.api.AccessScope
import com.sweetzpot.stravazpot.authenticaton.api.ApprovalPrompt
import com.sweetzpot.stravazpot.authenticaton.api.StravaLogin
import com.sweetzpot.stravazpot.authenticaton.model.LoginResult
import com.sweetzpot.stravazpot.authenticaton.ui.StravaLoginActivity
import com.sweetzpot.stravazpot.authenticaton.ui.StravaLoginButton
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_login.*
import me.tatocaster.stravagraph.*
import me.tatocaster.stravagraph.common.utils.PreferenceHelper
import me.tatocaster.stravagraph.common.utils.PreferenceHelper.get
import me.tatocaster.stravagraph.common.utils.PreferenceHelper.set
import me.tatocaster.stravagraph.common.utils.showErrorAlert
import me.tatocaster.stravagraph.features.base.BaseActivity
import me.tatocaster.stravagraph.features.home.presentation.HomeActivity
import timber.log.Timber
import javax.inject.Inject


class LoginActivity : BaseActivity(), LoginContract.View {
    private lateinit var prefs: SharedPreferences
    @Inject
    lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = loginButton as StravaLoginButton

        loginButton.setOnClickListener {
            stravaLogin()
        }

        prefs = PreferenceHelper.defaultPrefs(this)

        if (prefs[STRAVA_ACCESS_TOKEN_KEY, ""] != "") {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        askPermissions()

    }

    private fun askPermissions() {
        RxPermissions(this)
                .requestEach(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET)
                .subscribe { permission -> Timber.d("call() called with: permission = %s", permission) }
    }

    private fun stravaLogin() {
        val intent = StravaLogin.withContext(this)
                .withClientID(STRAVA_CLIENT_ID)
                .withRedirectURI(STRAVA_REDIRECT_URL)
                .withApprovalPrompt(ApprovalPrompt.AUTO)
                .withAccessScope(AccessScope.VIEW_PRIVATE)
                .makeIntent()
        startActivityForResult(intent, STRAVA_LOGIN_REQUEST_CODE)
    }


    override fun showError(message: String) {
        prefs[STRAVA_ACCESS_TOKEN_KEY] = ""
        showErrorAlert(this, "", message)
    }


    override fun loginSuccessful(loginResult: LoginResult?) {
        prefs[STRAVA_ACCESS_TOKEN_KEY] = loginResult?.token.toString()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == STRAVA_LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val code = data.getStringExtra(StravaLoginActivity.RESULT_CODE)

            Timber.d("Strava code %s", code)

            if (prefs[STRAVA_ACCESS_TOKEN_KEY, ""] == "") // TODO , shesacvlelia es, sheidzleba nebismier dros gaxdes sachiro amis gamodzaxeba
                presenter.onLoginResult(code)
        }
    }
}
