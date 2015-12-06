package com.mounacheikhna.nearby.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.bindView
import com.mounacheikhna.nearby.R

/**
 * Main venues activity.
 */
class MainActivity: AppCompatActivity() {

    val toolbar: Toolbar by bindView(R.id.toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
    }

    private fun setupToolbar() {
        delegate.setSupportActionBar(toolbar)
        setTitle(R.string.app_name)
    }

}