package me.tatocaster.stravagraph.entity

import android.os.Parcel
import android.os.Parcelable

data class StravaRecordedActivity(
        var name: String,
        var distanceKm: Float,
        var elevationMeters: Float,
        var polyLine: String,
        var displayTime: String,
        var startDate: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    constructor() : this("", 0f, 0f, "", "", "")

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(name)
        p0?.writeFloat(distanceKm)
        p0?.writeFloat(elevationMeters)
        p0?.writeString(polyLine)
        p0?.writeString(displayTime)
        p0?.writeString(startDate)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<StravaRecordedActivity> {
        override fun createFromParcel(parcel: Parcel): StravaRecordedActivity {
            return StravaRecordedActivity(parcel)
        }

        override fun newArray(size: Int): Array<StravaRecordedActivity?> {
            return arrayOfNulls(size)
        }
    }
}