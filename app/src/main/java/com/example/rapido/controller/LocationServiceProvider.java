package com.example.rapido.controller;

import com.example.rapido.interfaces.LocationProviderListener;
import com.example.rapido.network.ApiServiceNetwork;
import com.example.rapido.util.WebConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.rapido.util.WebConstants.PLACES_TYPE;

/**
 * Created by Khushhal on 6/9/2017.
 */

public class LocationServiceProvider {
    private ApiServiceNetwork apiServiceNetwork;
    private LocationProviderListener providerListener;

    public LocationServiceProvider(LocationProviderListener providerListener) {
        apiServiceNetwork = new ApiServiceNetwork();
        this.providerListener = providerListener;
    }

    public void getLocations(String input) {
        try {
            apiServiceNetwork.getNetworkService(null, WebConstants.PLACES_API)
                    .getLocations(input, PLACES_TYPE, WebConstants.API_KEY)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<JsonObject>() {
                        @Override
                        public void onCompleted() {
                            //completion
                        }

                        @Override
                        public void onError(Throwable e) {
                            //handle the error here
                            providerListener.onLocationNotAvailable();
                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            //location data,parse here and store for later use
                            if (!jsonObject.isJsonNull()) {
                                ArrayList<String> arrayList = new ArrayList<String>();
                                JsonArray jsonArray = jsonObject.get("predictions").getAsJsonArray();
                                int size = jsonArray.size();
                                if (size > 0) {
                                    for (int count = 0; count < size; count++) {
                                        JsonObject object = jsonArray.get(count).getAsJsonObject();
                                        arrayList.add(object.get("description").getAsString());
                                    }
                                    providerListener.onLocationAvailable(arrayList);
                                } else {
                                    providerListener.onLocationAvailable(arrayList);
                                }

                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            providerListener.onLocationNotAvailable();
        }
    }


}