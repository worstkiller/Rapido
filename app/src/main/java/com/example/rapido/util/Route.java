package com.example.rapido.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Khushhal on 6/10/2017.
 */

public class Route {
    private GoogleMap mMap;
    private Context context;
    public Polyline line;
    private List<Polyline> polylines = new ArrayList<Polyline>();
    private final String colorList[] = {"#D32F2F", "#7B1FA2", "#512DA8", "#303F9F", "#00796B", "#00C853", "#388E3C", "#FFA000", "#5D4037"};//red,purple,deep purple,indigo,teal,green,amber,grey,brown
    private Random random;

    public boolean drawRoute(GoogleMap map, Context c, String json) {
        mMap = map;
        context = c;
        random = new Random();
        drawPath(json, false);
        return false;
    }

    public void clearRoute() {
        for (Polyline line1 : polylines) {
            line1.remove();
        }
        polylines.clear();

    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private void drawPath(String result, boolean withSteps) {
        try {
            clearRoute();
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            for (int count = 0; count < routeArray.length(); count++) {
                int color = Color.parseColor(colorList[random.nextInt(colorList.length - 1)]);
                JSONObject routes = routeArray.getJSONObject(count);
                JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                String encodedString = overviewPolylines.getString("points");
                List<LatLng> list = decodePoly(encodedString);
                if (line != null) {
                    line.remove();
                }
                for (int z = 0; z < list.size() - 1; z++) {
                    LatLng src = list.get(z);
                    LatLng dest = list.get(z + 1);
                    line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                            .jointType(2).color(color));
                    polylines.add(line);
                }
                if (withSteps) {
                    JSONArray arrayLegs = routes.getJSONArray("legs");
                    JSONObject legs = arrayLegs.getJSONObject(0);
                    JSONArray stepsArray = legs.getJSONArray("steps");
                    //put initial point
                    for (int i = 0; i < stepsArray.length(); i++) {
                        Step step = new Step(stepsArray.getJSONObject(i));
                        mMap.addMarker(new MarkerOptions()
                                .position(step.location)
                                .title(step.distance)
                                .snippet(step.instructions)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                }
            }
        } catch (JSONException e) {

        }
    }

    /**
     * Class that represent every step of the directions. It store distance, location and instructions
     */
    private class Step {
        public String distance;
        public LatLng location;
        public String instructions;

        Step(JSONObject stepJSON) {
            JSONObject startLocation;
            try {
                distance = stepJSON.getJSONObject("distance").getString("text");
                startLocation = stepJSON.getJSONObject("start_location");
                location = new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng"));
                try {
                    instructions = URLDecoder.decode(Html.fromHtml(stepJSON.getString("html_instructions")).toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }
                ;
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }
}
