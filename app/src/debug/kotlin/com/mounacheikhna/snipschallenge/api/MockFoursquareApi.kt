package com.mounacheikhna.snipschallenge.api

import android.content.SharedPreferences
import retrofit.Response
import retrofit.Result
import retrofit.http.Query
import rx.Observable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

//not sure yet this the right place -> maybe move it to test package
//@Singleton
class MockFoursquareApi {
    //@Inject
    /*internal constructor(private val preferences: SharedPreferences) : FoursquareApi {
    private val responses = LinkedHashMap<Class<out Enum<*>>, Enum<*>>()

    init {

        // Initialize mock responses.
        loadResponse(MockRepositoriesResponse::class.java, MockRepositoriesResponse.SUCCESS)
    }

    *//**
     * Initializes the current response for `responseClass` from `SharedPreferences`, or
     * uses `defaultValue` if a response was not found.
     *//*
    private fun <T : Enum<T>> loadResponse(responseClass: Class<T>, defaultValue: T) {
        responses.put(responseClass, EnumPreferences.getEnumValue(preferences, responseClass, //
            responseClass.canonicalName, defaultValue))
    }

    fun <T : Enum<T>> getResponse(responseClass: Class<T>): T {
        return responseClass.cast(responses[responseClass])
    }

    fun <T : Enum<T>> setResponse(responseClass: Class<T>, value: T) {
        responses.put(responseClass, value)
        EnumPreferences.saveEnumValue(preferences, responseClass.canonicalName, value)
    }

    fun repositories(@Query("q") query: SearchQuery,
                     @Query("sort") sort: Sort,
                     @Query("order") order: Order): Observable<Result<RepositoriesResponse>> {
        var response = getResponse(MockRepositoriesResponse::class.java).response

        if (response.items != null) {
            // Don't modify the original list when sorting.
            val items = ArrayList(response.items)
            SortUtil.sort(items, sort, order)
            response = RepositoriesResponse(items)
        }

        return Observable.just(
            Result.response<RepositoriesResponse>(Response.success<RepositoriesResponse>(response)))
    }*/
}

