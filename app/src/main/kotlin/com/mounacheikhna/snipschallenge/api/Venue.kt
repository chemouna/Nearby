package com.mounacheikhna.snipschallenge.api

import android.os.Parcel
import android.os.Parcelable


public data class Venue
(
    val id: String,
    val name: String,
    var contact: Contact?,
    val location: VenueLocation,
    val canonicalUrl: String?,
    val hours: Hours?,
    val verified: Boolean,
    val rating: Double?,
    val description: String?,
    val price: Price?
) : Parcelable { //Venue needs to be parcelable to be passed (with VenueResult) in intent to VenueActivity

    constructor(source: Parcel) : this(
        source.readString(), source.readString(),
        source.readParcelable<Contact>(Contact::class.java.classLoader),
        source.readParcelable<VenueLocation>(VenueLocation::class.java.classLoader),
        source.readString(),
        source.readParcelable<Hours>(Hours::class.java.classLoader),
        source.readInt() == 1, source.readDouble(), source.readString(),
        source.readParcelable<Price>(Price::class.java.classLoader)
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(id)
        dest?.writeString(name)
        dest?.writeParcelable(contact, flags)
        dest?.writeParcelable(location, flags)
        dest?.writeString(canonicalUrl ?: "")
        dest?.writeParcelable(hours, flags)
        dest?.writeInt(if (verified) 1 else 0)
        dest?.writeDouble(rating ?: 0.0)
        dest?.writeString(description ?: "")
        dest?.writeParcelable(price, flags)
    }

    override fun describeContents() = 0

    companion object {
        public val CREATOR: Parcelable.Creator<Venue> = object : Parcelable.Creator<Venue> {
            override fun createFromParcel(source: Parcel): Venue {
                return Venue(source)
            }

            override fun newArray(size: Int): Array<Venue?> {
                return arrayOfNulls(size)
            }
        }
    }

}

data class Contact(
    val phone: String?,
    val formattedPhone: String?
) : Parcelable {
    constructor(source: Parcel) : this(source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(phone ?: "")
        dest?.writeString(formattedPhone ?: "")
    }

    companion object {
        public val CREATOR: Parcelable.Creator<Contact> = object : Parcelable.Creator<Contact> {
            override fun createFromParcel(source: Parcel): Contact {
                return Contact(source)
            }

            override fun newArray(size: Int): Array<Contact?> {
                return arrayOfNulls(size)
            }
        }
    }

}

data class Hours(
    val status: String,
    val isOpen: Boolean
) : Parcelable {
    constructor(source: Parcel) : this(source.readString(), source.readInt() == 1)

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(status)
        dest?.writeInt(if (isOpen) 1 else 0)
    }

    companion object {
        public val CREATOR: Parcelable.Creator<Hours> = object : Parcelable.Creator<Hours> {
            override fun createFromParcel(source: Parcel): Hours {
                return Hours(source)
            }

            override fun newArray(size: Int): Array<Hours?> {
                return arrayOfNulls(size)
            }
        }
    }
}

data class VenueLocation
(
    val lat: Double? = null,
    val lng: Double? = null,
    val distance: Int? = null,
    val address: String? = null,
    val crossStreet: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
    val country: String? = null,
    var formattedAddress: Array<String>?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readDouble(), source.readDouble(),
        source.readInt(), source.readString(), source.readString(), source.readString(),
        source.readString(), source.readString(), source.readString(),
        arrayOf())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeDouble(lat ?: 0.0)
        dest?.writeDouble(lng ?: 0.0)
        dest?.writeInt(distance ?: 0)
        dest?.writeString(address ?: "")
        dest?.writeString(crossStreet ?: "")
        dest?.writeString(city ?: "")
        dest?.writeString(state ?: "")
        dest?.writeString(postalCode ?: "")
        dest?.writeString(country ?: "")
        dest?.writeStringArray(formattedAddress ?: arrayOf())
    }

    companion object {
        public val CREATOR: Parcelable.Creator<VenueLocation> = object : Parcelable.Creator<VenueLocation> {
            override fun createFromParcel(source: Parcel): VenueLocation {
                return VenueLocation(source)
            }

            override fun newArray(size: Int): Array<VenueLocation?> {
                return arrayOfNulls(size)
            }
        }
    }
}

data class Price(
    val tier: Int,
    val message: String
) : Parcelable {
    constructor(source: Parcel) : this(source.readInt(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(tier)
        dest?.writeString(message)
    }

    companion object {
        public val CREATOR: Parcelable.Creator<Price> = object : Parcelable.Creator<Price> {
            override fun createFromParcel(source: Parcel): Price {
                return Price(source)
            }

            override fun newArray(size: Int): Array<Price?> {
                return arrayOfNulls(size)
            }
        }
    }

}