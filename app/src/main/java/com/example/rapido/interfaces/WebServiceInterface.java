package com.example.rapido.interfaces;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by OFFICE on 4/13/2017.
 */

public interface WebServiceInterface {

    @GET("maps/api/place/autocomplete/json?")
    Observable<JsonObject> getLocations(@Query("input") String input, @Query("types") String types, @Query("key") String key);

    @GET("maps/api/directions/json?")
    Observable<JsonObject> getRoutes(@Query("origin") String origin, @Query("destination") String destination, @Query("alternatives") boolean alternatives, @Query("key") String key);

}
