package com.mounacheikhna.snipschallenge.ui

import android.os.Parcel
import android.os.Parcelable
import com.mounacheikhna.snipschallenge.api.Venue

public data class VenueResult(
    val photoUrl: String?,
    val bestPhotoUrl: String?,//for a better quality
    val venue: Venue
) : Parcelable { //VenueResult needs to be parcelable to be passed in intent to VenueActivity

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readParcelable<Venue>(Venue::class.java.classLoader))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(photoUrl)
        dest?.writeString(bestPhotoUrl)
        dest?.writeParcelable(venue, flags)
    }

    companion object {
        public val CREATOR: Parcelable.Creator<VenueResult> = object : Parcelable.Creator<VenueResult> {
            override fun createFromParcel(source: Parcel): VenueResult {
                return VenueResult(source)
            }

            override fun newArray(size: Int): Array<VenueResult?> {
                return arrayOfNulls(size)
            }
        }
    }

}
