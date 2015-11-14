package com.mounacheikhna.snipschallenge

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
public interface AppComponent {

    fun inject(mainActivity: MainActivity)

}