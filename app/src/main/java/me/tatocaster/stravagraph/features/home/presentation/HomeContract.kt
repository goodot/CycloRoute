package me.tatocaster.stravagraph.features.home.presentation

import com.sweetzpot.stravazpot.activity.model.Activity

interface HomeContract {
    interface View {
        fun showError(message: String)

        fun showActivitiesList(list: MutableList<Activity>)
    }

    interface Presenter {
        fun getStravaActivities(token: String)
        fun detach()
    }
}