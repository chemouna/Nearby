package com.benoitletondor.squarehack.data


public data class Venue
(
    val id:String,
    val name:String,
    val location:Location,
    val mainCategory:Category?,
    val verified:Boolean,
    val stats:Stats,
    var ratingAvailability:RatingLoadingStatus = RatingLoadingStatus.NOT_LOADED,
    var rating:Double? = null
)


enum class RatingLoadingStatus {
    NOT_LOADED,
    LOADING,
    AVAILABLE,
    NOT_AVAILABLE,
    ERROR
}

public data class Location
(
    val lat:Double? = null,
    val lng:Double? = null,
    val distance:Int? = null,
    val address:String? = null,
    val crossStreet:String? = null,
    val city:String? = null,
    val state:String? = null,
    val postalCode:String? = null,
    val country:String? = null
)

public data class Category
(
    val id:String,
    val name:String,
    val pluralName:String,
    val icon:String
)

public data class Stats
(
    val checkinsCount:Int,
    val usersCount:Int,
    val tipCount:Int
)