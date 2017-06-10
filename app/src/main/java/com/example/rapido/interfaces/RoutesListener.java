package com.example.rapido.interfaces;

import com.google.gson.JsonObject;

/**
 * Created by Khushhal on 6/10/2017.
 */

public interface RoutesListener {
    void onRouteAvailable(JsonObject jsonObject);

    void onRoutNotAvailable();
}
