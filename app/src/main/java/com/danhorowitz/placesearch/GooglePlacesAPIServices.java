package com.danhorowitz.placesearch;

import com.danhorowitz.placesearch.model.PredictionsTO;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface GooglePlacesAPIServices {
    @GET("/autocomplete/json")
    void getPlacesPredictions(@Query("key") String key,@Query("components") String country,@Query("input") String input, Callback<PredictionsTO> callback);

}