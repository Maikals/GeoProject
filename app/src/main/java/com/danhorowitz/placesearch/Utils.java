package com.danhorowitz.placesearch;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Locale;

/**
 * Created by danielhorowitz on 10/24/15.
 */
public class Utils {
    public static final double EARTH_RADIUS = 6371009;
    public static final String GOOGLE_PLACES_API_BASE_URL = "https://maps.googleapis.com/maps/api/place";

    public static LatLngBounds calculateBounds(Location location) {
        if (location != null) {

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            LatLng sw = computeOffset(latLng, 1609, 225);
            LatLng ne = computeOffset(latLng, 1609, 45);

            return new LatLngBounds(sw, ne);
        } else {
            return new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
        }
    }

    public static LatLng computeOffset(LatLng from, double distance, double heading) {
        distance /= EARTH_RADIUS;
        heading = Math.toRadians(heading);
        // http://williams.best.vwh.net/avform.htm#LL
        double fromLat = Math.toRadians(from.latitude);
        double fromLng = Math.toRadians(from.longitude);
        double cosDistance = Math.cos(distance);
        double sinDistance = Math.sin(distance);
        double sinFromLat = Math.sin(fromLat);
        double cosFromLat = Math.cos(fromLat);
        double sinLat = cosDistance * sinFromLat + sinDistance * cosFromLat * Math.cos(heading);
        double dLng = Math.atan2(
                sinDistance * cosFromLat * Math.sin(heading),
                cosDistance - sinFromLat * sinLat);
        return new LatLng(Math.toDegrees(Math.asin(sinLat)), Math.toDegrees(fromLng + dLng));
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param string
     * @return
     */
    public static boolean isEmptyOrNull(String string) {
        return string == null || string.isEmpty();
    }

    public static void hideKeyBoard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (context.getWindow().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

}
