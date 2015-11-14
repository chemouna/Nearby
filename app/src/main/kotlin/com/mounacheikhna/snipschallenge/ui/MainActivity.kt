package com.mounacheikhna.snipschallenge.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.bindView
import com.mounacheikhna.snipschallenge.FoursquareApp
import com.mounacheikhna.snipschallenge.R

class MainActivity: AppCompatActivity() {

    val toolbar: Toolbar by bindView(R.id.toolbar)
    val venuesView: NearbyVenuesView by bindView(R.id.venues_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        FoursquareApp.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
    }

    private fun setupToolbar() {
        delegate.setSupportActionBar(toolbar)
        setTitle(R.string.app_name)
    }


}