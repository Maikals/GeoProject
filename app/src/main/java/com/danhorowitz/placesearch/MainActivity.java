package com.danhorowitz.placesearch;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.danhorowitz.placesearch.model.PredictionsTO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dhorowitz on 10/07/2015.
 */
public class MainActivity extends AppCompatActivity implements GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMapLoadedCallback, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapLongClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    ClearableAutoCompleteTextView autoCompleteTextView;
    ProgressBar progressToolbar;
    Toolbar toolbar;
    Fragment map;
    FrameLayout mapContainer;

    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIsFirstTime = true;
    private Location mSelectedLocation;
    private PlaceAPIAutocompleteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initViews();
        setupToolbar();
        setupGooglePlacesAPI();
        setupGoogleMap();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {
        this.autoCompleteTextView = (ClearableAutoCompleteTextView) findViewById(R.id.auto_complete_text_view);
        this.progressToolbar = (ProgressBar) findViewById(R.id.progress_toolbar);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.map = getSupportFragmentManager().findFragmentById(R.id.map);
        this.mapContainer = (FrameLayout) findViewById(R.id.map_container);

    }

    private void setupAutoCompleteAdapter(Location currentLocation) {
        mAdapter = new PlaceAPIAutocompleteAdapter(this, R.layout.simple_prediction_item,
                mGoogleApiClient, Utils.calculateBounds(currentLocation), null);
        autoCompleteTextView.setAdapter(mAdapter);

        this.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PlaceAPIAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                String placeId = String.valueOf(item.placeId);
                requestPlaceDetailsById(placeId);
            }
        });

        autoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("actionId", "OnKeyListener: " + keyCode + " - " + KeyEvent.KEYCODE_ENTER);
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!Utils.isEmptyOrNull(autoCompleteTextView.getText().toString())) {
                        if (autoCompleteTextView.isPopupShowing()) {
                            autoCompleteTextView.dismissDropDown();
                        }
                        Utils.hideKeyBoard(MainActivity.this);
                        requestPlaceIdByQuery(autoCompleteTextView.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void setupGoogleMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleMap = supportMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(this);
        googleMap.setOnMapLoadedCallback(this);
        googleMap.setOnMapLongClickListener(this);
    }

    private void setupGooglePlacesAPI() {
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).build();
    }

    protected void requestPlaceIdByQuery(String input) {
        String country = "country:" + (Utils.getUserCountry(this) == null ? "es" : Utils.getUserCountry(this));
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Utils.GOOGLE_PLACES_API_BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        GooglePlacesAPIServices googlePlacesAPIServices = restAdapter.create(GooglePlacesAPIServices.class);

        googlePlacesAPIServices.getPlacesPredictions(getString(R.string.server_google_api_key), country, input, new Callback<PredictionsTO>() {
            @Override
            public void success(PredictionsTO predictionsTO, Response response) {
                if (predictionsTO != null && predictionsTO.getPredictions() != null && !predictionsTO.getPredictions().isEmpty()) {
                    requestPlaceDetailsById(predictionsTO.getPredictions().get(0).getPlace_id());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, "An error has occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestPlaceDetailsById(String placeId) {
        progressToolbar.setVisibility(View.VISIBLE);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (mIsFirstTime && mSelectedLocation == null) {
            mIsFirstTime = false;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            if (mAdapter == null) {
                setupAutoCompleteAdapter(location);
            }
        }

    }

    @Override
    public void onMapLoaded() {
        if (mSelectedLocation != null) {
            generateMarker("", new LatLng(mSelectedLocation.getLatitude(), mSelectedLocation.getLongitude()));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            progressToolbar.setVisibility(View.GONE);
            String address = "";
            if (!places.getStatus().isSuccess() || places.getCount() == 0) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                Toast.makeText(MainActivity.this, R.string.no_results_found, Toast.LENGTH_SHORT).show();
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            if (!Utils.isEmptyOrNull(place.getAddress().toString())) {
                address = place.getAddress().toString();
            }

            Log.i(TAG, "Place details received: " + place.getName());
            generateMarker(address, place.getLatLng());
            generateLocationFromLatLng(place.getLatLng());

            places.release();

        }
    };

    private void generateLocationFromLatLng(LatLng latLng) {
        mSelectedLocation = new Location("");
        mSelectedLocation.setLatitude(latLng.latitude);
        mSelectedLocation.setLongitude(latLng.longitude);
    }

    private void generateMarker(String address, LatLng latLng) {
        googleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(address);
        googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        progressToolbar.setVisibility(View.GONE);
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        AddresByLatLngTask task = new AddresByLatLngTask(latLng);
        task.execute();
    }

    public class AddresByLatLngTask extends AsyncTask<Address, Void, Address> {

        private LatLng latLng;

        public AddresByLatLngTask(LatLng latLng) {
            this.latLng = latLng;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressToolbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Address doInBackground(Address... ads) {
            Geocoder geocoder;
            List<Address> addresses;
            Address result = null;
            try {
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if (addresses != null && !addresses.isEmpty()) {
                    result = addresses.get(0);
                }

            } catch (IOException e) {
                this.cancel(true);
            }
            return result;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(MainActivity.this, "An error has ocurred", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Address address) {
            super.onPostExecute(address);
            progressToolbar.setVisibility(View.GONE);
            googleMap.clear();
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).snippet(address.getAddressLine(0)).title(address.getAddressLine(0));
            googleMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }


}
