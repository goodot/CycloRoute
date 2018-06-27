package me.tatocaster.stravagraph.features.home.presentation

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sweetzpot.stravazpot.activity.model.Activity
import kotlinx.android.synthetic.main.item_activity.view.*
import me.tatocaster.stravagraph.R
import me.tatocaster.stravagraph.entity.StravaRecordedActivity
import org.joda.time.Period
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder
import java.text.SimpleDateFormat
import java.util.*


class StravaActivitiesAdapter(private val context: Context,
                              private val onClick: (StravaRecordedActivity) -> Unit) : RecyclerView.Adapter<StravaActivitiesAdapter.ActivityHolder>() {
    private var activityList: List<Activity> = arrayListOf()
    override fun getItemCount(): Int = activityList.size
    private val periodFormatter: PeriodFormatter = PeriodFormatterBuilder()
            .appendHours()
            .appendSeparator(":")
            .appendMinutes()
            .printZeroAlways()
            .minimumPrintedDigits(2)
            .appendSeparator(":")
            .appendSeconds()
            .minimumPrintedDigits(2)
            .toFormatter()
    private val dateFormatter = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        return ActivityHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
        val item = convertToStravaActivity(activityList[position])
        holder.render(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    private fun convertToStravaActivity(activity: Activity): StravaRecordedActivity {
        val stravaRecordedActivity = StravaRecordedActivity()

        val period = Period(activity.movingTime.seconds * 1000L)
        stravaRecordedActivity.displayTime = periodFormatter.print(period)
        stravaRecordedActivity.name = activity.name
        stravaRecordedActivity.distanceKm = activity.distance.meters / 1000
        stravaRecordedActivity.elevationMeters = activity.totalElevationGain.meters
        stravaRecordedActivity.polyLine = activity.map.summaryPolyline
        stravaRecordedActivity.startDate = dateFormatter.format(activity.startDate)
        return stravaRecordedActivity
    }

    fun setActivities(activities: List<Activity>) {
        activityList = activities
        notifyDataSetChanged()
    }

    inner class ActivityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun render(activity: StravaRecordedActivity) {
            itemView.title.text = activity.name
            itemView.date.text = activity.startDate
            itemView.distance.text = "${"%.2f".format(activity.distanceKm)}km"
            itemView.time.text = activity.displayTime
            itemView.activityElevation.text = "${activity.elevationMeters}m"
        }
    }
}
