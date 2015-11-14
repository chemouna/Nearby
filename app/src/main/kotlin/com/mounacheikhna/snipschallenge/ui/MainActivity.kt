package com.mounacheikhna.snipschallenge.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import butterknife.bindView
import com.mounacheikhna.snipschallenge.FoursquareApp
import com.mounacheikhna.snipschallenge.R

class MainActivity: AppCompatActivity() {

    val content: ViewGroup by bindView(R.id.main_content)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        layoutInflater.inflate(R.layout.activity_main, null)

        FoursquareApp.appComponent.inject(this)


        //TODO: refactor this to put in NearbyVenues instead ?
        layoutInflater.inflate(R.layout.nearby_venues_view, content)
    }

}