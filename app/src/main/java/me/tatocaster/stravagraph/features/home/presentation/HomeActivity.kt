package me.tatocaster.stravagraph.features.home.presentation

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.sweetzpot.stravazpot.activity.model.Activity
import kotlinx.android.synthetic.main.activity_home.*
import me.tatocaster.stravagraph.R
import me.tatocaster.stravagraph.STRAVA_ACCESS_TOKEN_KEY
import me.tatocaster.stravagraph.common.utils.PreferenceHelper
import me.tatocaster.stravagraph.common.utils.PreferenceHelper.get
import me.tatocaster.stravagraph.common.utils.SeparatorItemDecoration
import me.tatocaster.stravagraph.common.utils.showErrorAlert
import me.tatocaster.stravagraph.features.base.BaseActivity
import me.tatocaster.stravagraph.features.create.presentation.CreateActivity
import javax.inject.Inject


class HomeActivity : BaseActivity(), HomeContract.View {

    @Inject
    lateinit var presenter: HomeContract.Presenter

    private lateinit var adapter: StravaActivitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        presenter.getStravaActivities(PreferenceHelper.defaultPrefs(this)[STRAVA_ACCESS_TOKEN_KEY]!!)


        adapter = StravaActivitiesAdapter(this) {
            val bundle = Bundle().apply {
                putParcelable("strava_activity", it)
            }
            val intent = Intent(this, CreateActivity::class.java).apply {
                putExtras(bundle)
            }
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)
        activitiesRV.addItemDecoration(SeparatorItemDecoration(this, ContextCompat.getColor(this, R.color.colorPrimaryDark), 1f))
        activitiesRV.layoutManager = layoutManager
        activitiesRV.setHasFixedSize(true)
        activitiesRV.adapter = adapter

    }

    override fun showError(message: String) {
        showErrorAlert(this, "", message)
    }

    override fun showActivitiesList(list: MutableList<Activity>) {
        adapter.setActivities(list)
    }
}