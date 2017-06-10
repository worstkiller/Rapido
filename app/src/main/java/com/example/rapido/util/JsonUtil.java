package com.example.rapido.util;

import android.content.Context;
import android.text.Html;

import com.example.rapido.R;
import com.example.rapido.model.DirectionsInfo;
import com.example.rapido.model.RouteInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by Khushhal on 6/10/2017.
 */

public class JsonUtil {

    public static ArrayList<RouteInfo> addRoutes(JsonObject jsonObject) {
        JsonArray jsonArray = jsonObject.get("routes").getAsJsonArray();
        ArrayList<RouteInfo> routeInfos = new ArrayList<>();
        RouteInfo routeInfo;
        for (int count = 0; count < jsonArray.size(); count++) {
            JsonObject childObject = jsonArray.get(count).getAsJsonObject();
            routeInfo = new RouteInfo();
            routeInfo.setRouteTitle(childObject.get("summary").getAsString());
            JsonArray legs = childObject.get("legs").getAsJsonArray();
            JsonObject legsObject = legs.get(0).getAsJsonObject();
            routeInfo.setDistance(legsObject.get("distance").getAsJsonObject().get("text").getAsString());
            routeInfo.setTime(legsObject.get("duration").getAsJsonObject().get("text").getAsString());
            routeInfos.add(routeInfo);
        }
        return routeInfos;
    }

    public static ArrayList<DirectionsInfo> addDirections(Context context, JsonObject jsonObject) {
        ArrayList<DirectionsInfo> directionsInfos = new ArrayList<>();
        DirectionsInfo directionsInfo;
        JsonArray legs = jsonObject.get("legs").getAsJsonArray();
        JsonObject legsObject = legs.get(0).getAsJsonObject();
        JsonArray routeInfo = legsObject.get("steps").getAsJsonArray();
        int size = routeInfo.size();
        for (int steps = 0; steps < size; steps++) {
            directionsInfo = new DirectionsInfo();
            JsonObject object = routeInfo.get(steps).getAsJsonObject();
            try {
                StringBuffer distance = new StringBuffer(legsObject.get("duration").getAsJsonObject().get("text").getAsString());
                distance.append("(").append(legsObject.get("distance").getAsJsonObject().get("text").getAsString()).append(")");
                directionsInfo.setDistance(distance.toString());
                directionsInfo.setTitle(URLDecoder.decode(Html.fromHtml(object.get("html_instructions").getAsString()).toString(), "UTF-8"));
                if (directionsInfo.getTitle().contains("Turn right")) {
                    directionsInfo.setIconId(R.drawable.ic_action_right);
                } else if (directionsInfo.getTitle().contains("Turn left")) {
                    directionsInfo.setIconId(R.drawable.ic_action_left);
                } else {
                    directionsInfo.setIconId(R.drawable.ic_action_info);
                }
                directionsInfos.add(directionsInfo);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return directionsInfos;
    }


}
