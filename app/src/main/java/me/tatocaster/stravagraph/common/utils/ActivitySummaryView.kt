package me.tatocaster.stravagraph.common.utils

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.view_activity_summary.view.*
import me.tatocaster.stravagraph.R
import me.tatocaster.stravagraph.entity.StravaRecordedActivity

class ActivitySummaryView : ConstraintLayout {
    lateinit var v: View

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        v = inflater.inflate(R.layout.view_activity_summary, this)
    }

    fun setSummary(activity: StravaRecordedActivity) {
        v.title.text = activity.name
        v.activityElevation.text = "${activity.elevationMeters}m"
        v.distance.text = "${"%.2f".format(activity.distanceKm)}km"
    }

}