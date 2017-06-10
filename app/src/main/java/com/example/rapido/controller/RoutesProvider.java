package com.example.rapido.controller;

import com.example.rapido.interfaces.RoutesListener;
import com.example.rapido.network.ApiServiceNetwork;
import com.example.rapido.util.WebConstants;
import com.google.gson.JsonObject;

import java.io.IOException;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Khushhal on 6/10/2017.
 */

public class RoutesProvider {
    private ApiServiceNetwork apiServiceNetwork;
    private RoutesListener routesListener;

    public RoutesProvider(RoutesListener routesListener) {
        this.routesListener = routesListener;
        apiServiceNetwork = new ApiServiceNetwork();
    }

    public void getRoutes(String source, String destinations) {
        try {
            apiServiceNetwork.getNetworkService(null, WebConstants.PLACES_API)
                    .getRoutes(source, destinations, true, WebConstants.API_KEY)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onCompleted() {
                            //handle completion here
                        }

                        @Override
                        public void onError(Throwable e) {
                            //handle the error here
                            routesListener.onRoutNotAvailable();
                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            //handle the response here
                            if (!jsonObject.isJsonNull()) {
                                routesListener.onRouteAvailable(jsonObject);
                            } else {
                                routesListener.onRoutNotAvailable();
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            routesListener.onRoutNotAvailable();
        }
    }
}
