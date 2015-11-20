
Snips Challenge : Foursquare client
====================================

#Implemented features : 
* Display list of venues for a location.
* Observe location changes and update venues list whenever a new location is detected.
* Display a message informing the user that the app is going to fetch new venues because he/she had moved, with an action "Cancel" to cancel the update of venues.
* Display of a venue item with its image, name, ratings and price.
* Display a venue in a detailed view when a venue item is clicked.
* Animating the display of detail view from the list of venues.
* Support for Marshmallow permissions (Asking for each needed permission).

#Technical additions :
* Implemented entirely in Kotlin.
* Heavy use of Reactive paradigms (with RxJava, Retrofit and RxBindings). 
* Unit tests (with mocking of foursquare service).
* Gradle dependencies organised in a maintanable way (dependencies.gradle).
* Using an MVP structure for the app.
* Dependency injection with Dagger2.
* Taking advantage of android build types to use a debug build for features such as logging that we don't want on release builds.

#Design additions :
* Following Material design guidelines.
* Use of animations and shared elements to provide smooth transition between Venues and venue screen.

#Challenges 
* Using kotlin for the first time.
* Time management (between Job and this fun project).





