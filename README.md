
A Simple Foursquare client written in kotlin
====================================

###Demo
[![Alt text for your video](https://i.ytimg.com/vi/VDtn8DSFXNM/0.jpg)](https://youtu.be/VDtn8DSFXNM)

(click to view on youtube)

#Features :
* Display list of venues for a location.
* Observe location changes and update venues list whenever a new location is detected.
* Display a message informing the user that the app is going to fetch new venues because he/she had moved, with an action "Cancel" to cancel the update of venues.
* Display of a venue item with its image, name, ratings and price.
* Display a venue in a detailed view when a venue item is clicked.
* Animating the display of detail view from the list of venues.
* Support for Marshmallow permissions (Asking for each needed permission).

#Implementation :
* Implemented entirely in Kotlin.
* Heavy use of Reactive paradigms (with RxJava, Retrofit and RxBindings). 
* Unit tests (with mocking of foursquare service).
* Gradle dependencies organised in a maintainable way (dependencies.gradle).
* Using an MVP structure.
* Dependency injection with Dagger2.
* Taking advantage of android build types to use a debug build for features such as logging that we don't want on release builds.
* Testing: using Mocks with retrofit

#Focus on:

##Minimize mutability
  - Use val most of the time for immutability and minimise the use of var.

##Idiomatic use of kotlin
  - Use extensions
  - Use apply
  - Use of kotlin higher order functions to have a LINQ-style code

##Rx
  - Don't break the chain.


License
-------

    Copyright (C) 2016 Mouna Cheikhna

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


