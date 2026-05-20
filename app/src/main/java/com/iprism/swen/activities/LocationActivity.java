package com.iprism.swen.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.iprism.swen.databinding.ActivityLocationBinding;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private ActivityLocationBinding binding;

    private GoogleMap mMap;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private PlacesClient placesClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    String apikey = "AIzaSyAaf0xB28iKcaFh6Ex4bRFSaQzZtbu8GOg";

    SupportMapFragment mapFragment;
    private View mapView;
    private LatLng latLng;
    String lat = "", lon = "", Statenew = "", cityname = "";
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private final float DEFAULT_ZOOM = 16;
    AVLoadingIndicatorView progress;
    static OkHttpClient client = new OkHttpClient();
    String str_srcaddress;
    ActivityResultLauncher<Intent> launchSomeActivity;

    String tag = "";
    String pincode = "";
    Bundle bundle;
    String tottaladdress = "";
    String area = "", district = "", state = "", country = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bundle = getIntent().getExtras();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(com.iprism.swen.R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        if (getIntent().hasExtra("tag")) {
            tag = getIntent().getStringExtra("tag");
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocationActivity.this);
        Places.initialize(LocationActivity.this, apikey);
        placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();

                            final Place place = Autocomplete.getPlaceFromIntent(data);
                            Log.e("onActivityResult", "Place: " + place.getAddress());
                            LatLng latLng = place.getLatLng();
                            double latitude = latLng.latitude;
                            double longitude = latLng.longitude;
                            System.out.println("latlng" + latLng);
                            System.out.println("latlng1 " + latitude + " " + longitude);
                            lat = String.valueOf(latitude);
                            lon = String.valueOf(longitude);
                            str_srcaddress = getCompleteAddressString(latitude, longitude);

                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {

                                    mMap = googleMap;
                                    // Add a marker in Sydney and move the camera
                                    LatLng sydney = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
//                        mMap.addMarker(new MarkerOptions().position(sydney));

//                        mMap.setOnCameraMoveStartedListener((GoogleMap.OnCameraMoveStartedListener) AddadressMapActivity.this);
                                    //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
                                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                    // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
                                }
                            });
                        }
                    }
                });

        /*btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                intent.putExtra("str_lat1", str_lat1);
                intent.putExtra("str_lang1", str_lang1);
                intent.putExtra("address", loaction_text.getText().toString());
                intent.putExtra("Statenew", Statenew);
                intent.putExtra("cityname", cityname);
                //intent.putExtra("buslist","listing_main");
//               intent.putExtra("str_locality",cityname+" - "+pincode);
                //setResult(Activity.RESULT_OK, intent);
                startActivity(intent);
                finish();
            }
        });*/

        binding.searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!Places.isInitialized()) {
                        Places.initialize(getApplicationContext(), apikey);
                    }
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).setCountry("IN").build(LocationActivity.this);
                    //  startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    launchSomeActivity.launch(intent);
                } catch (Exception e) {
                    // TODO: Handle the error.
                }
            }
        });
        handleBackImg();
        handleConfirmBtn();
    }

    private void handleBackImg() {
        binding.backImg.setOnClickListener(view -> {
            finish();
        });
    }

    private void handleConfirmBtn() {
        binding.confirmBtn.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            intent.putExtra("str_srcaddress", binding.locationTxt.getText().toString());
            intent.putExtra("cityName", cityname);
            intent.putExtra("address", tottaladdress);
            intent.putExtra("area", area);
            intent.putExtra("district", district);
            intent.putExtra("state", Statenew);
            intent.putExtra("country", country);
            intent.putExtra("pincode", pincode);
            //intent.putExtra("buslist","listing_main");
            // intent.putExtra("str_locality",cityname+" - "+pincode);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private String getAddress() {
        return binding.locationTxt.getText().toString().trim();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(LocationActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(LocationActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(LocationActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(LocationActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

/*
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (materialSearchBar.isSuggestionsVisible())
                    materialSearchBar.clearSuggestions();
                if (materialSearchBar.isSearchEnabled())
                    materialSearchBar.disableSearch();
                return false;
            }
        });
*/


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (mMap != null) {
                    mMap.clear();
                }
                // cameraPosition = mMap.getCameraPosition().zoom;

                LatLng midLatLng = mMap.getCameraPosition().target;
                Log.e("onCameraChange: ", "" + mMap.getCameraPosition().target.latitude + "  :  " + mMap.getCameraPosition().target.longitude);
                //   String userlocationadress = getCompleteAddressString(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                //Toast.makeText(MapsActivity.this, getCompleteAddressString(cameraPosition.target.latitude, cameraPosition.target.longitude), Toast.LENGTH_SHORT).show();
                //strAddress = MapUtil.getAddress(midLatLng.longitude, midLatLng.longitude, MapsActivity.this);
                //    strAddress = userlocationadress;

                latLng = midLatLng;
                lat = String.valueOf(latLng.latitude);
                lon = String.valueOf(latLng.longitude);
                str_srcaddress = getCompleteAddressString(latLng.latitude, latLng.longitude);

                Log.e("onCameraChangep;'j: ", "" + midLatLng);


                // txt_Address.setText(userlocationadress);
            }
        });


    }


    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                                lat = String.valueOf(mLastKnownLocation.getLatitude());
                                lon = String.valueOf(mLastKnownLocation.getLongitude());
                                str_srcaddress = getCompleteAddressString(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                                // txt_Address.setText(str_srcaddress);

                            /*    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                if (imm != null)
                                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

                                materialSearchBar.setText(str_destaddress);
*/
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                                        lat = String.valueOf(mLastKnownLocation.getLatitude());
                                        lon = String.valueOf(mLastKnownLocation.getLongitude());
                                        str_srcaddress = getCompleteAddressString(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                                        //    materialSearchBar.setText(str_destaddress);

                                        // txt_Address.setText(str_srcaddress);
/*

                                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                        if (imm != null)
                                            imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
*/

                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(LocationActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String getCompleteAddressString(double latitude, double longitude) {
/*
        GetLocationAsync locationAsync = new GetLocationAsync(latitude,
                longitude, AddadressMapActivity.this, new    GetLocationAsync.AsyncResponse() {
            @Override
            public void onProcessFinished(String fulladdress, String smallAddress, String state, String city, String country, String zipCode, String placeName) {
                txt_Address.setText(fulladdress);
            }
        });
        locationAsync.execute();*/
        String strAdd = "";

        try {
            // Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            //List<Address> addresses= geocoder.getFromLocation(latitude, longitude, 3);

            //  List<Address> addressesnew = MyGeocoder.getFromLocation(latitude,longitude,3);


            String key = "key=AIzaSyAaf0xB28iKcaFh6Ex4bRFSaQzZtbu8GOg";
            String addressmo = String.format(Locale.US,
                    "https://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=false&language="
                            + Locale.getDefault().getCountry(), latitude, longitude) + "&" + key;

            getmyAddress(addressmo, 3);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                area = address.getSubLocality();
                district = address.getLocality();
                state = address.getAdminArea();
                country = address.getCountryName();
                pincode = address.getPostalCode();

                // Do something with the retrieved information
                Log.d("LocationInfo", "Area: " + area + ", District: " + district + ", State: " + state);
            }

            Log.d("newurl", addressmo);

            //  List<Address> addresses = MyGeocoder.getAddress(addressmo,3);


          /*  String key = "key="+getResources().getString(R.string.google_maps_key);
            String sensor = "sensor=true";
            String mode = "mode=driving";
            String addressnew = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude;
            String parameters = addressnew + sensor + key;
*/
            //   Log.d("newadrees", parameters);
           /* if (addresses != null) {

                txt_Address.setText(Variables.addressnew);

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                Address returnedAddress = addresses.get(0);

               // txt_Address.setText(address);

                StringBuilder strReturnedAddress = new StringBuilder("");



                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.e("Current loction address", strReturnedAddress.toString());
            } else {
                Log.e("Current loction address", "No Address returned!");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Current loction address", "Canont get Address!");
        }
        return strAdd;
        //  return null;
    }

    public List<Address> getmyAddress(String url, int maxResult) {
        List<Address> retList = null;

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        Request request = new Request.Builder().url(url)
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json; q=0.5")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseStr = response.body().string();
            JSONObject jsonObject = new JSONObject(responseStr);

            retList = new ArrayList<Address>();


            if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                JSONArray results = jsonObject.getJSONArray("results");
                if (results.length() > 0) {
                    for (int i = 0; i < results.length() && i < maxResult; i++) {
                        JSONObject result = results.getJSONObject(i);
                        Address addr = new Address(Locale.getDefault());

                        JSONArray components = result.getJSONArray("address_components");
                        tottaladdress = result.getString("formatted_address");
                        Log.d("mainaddress", tottaladdress);
                        // Variables.addressnew= tottaladdress;
                        binding.locationTxt.setText(tottaladdress);
                        // mapsnewActivity.getaddress(tottaladdress);
                        String streetNumber = "";
                        String State = "";
                        String sublocalitynew = "";
                        String route = "";
                        for (int a = 0; a < components.length(); a++) {
                            JSONObject component = components.getJSONObject(a);
                            JSONArray types = component.getJSONArray("types");
                            for (int j = 0; j < types.length(); j++) {
                                String type = types.getString(j);
                                if (type.equals("locality")) {
                                    addr.setLocality(component.getString("long_name"));
                                    cityname = component.getString("long_name");
//                                    Log.d("locality", cityname);
                                } else if (type.equals("postal_code")) {
                                    streetNumber = component.getString("long_name");
//                                    pincode = component.getString("long_name");
//                                    Log.d("pincode", pincode);
                                } else if (type.equals("administrative_area_level_1")) {
                                    Statenew = component.getString("long_name");
                                    State = component.getString("short_name");
//                                    Log.d("State", Statenew);
                                } else if (type.equals("sublocality")) {
                                    sublocalitynew = component.getString("long_name");
//                                    sublocality = component.getString("short_name");
//                                    Log.d("sublocality", sublocality);
                                } else if (type.equals("route")) {
                                    route = component.getString("long_name");
                                }
                            }
                        }

                        addr.setAddressLine(0, route + " " + streetNumber);

                        addr.setLatitude(
                                result.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                        addr.setLongitude(
                                result.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                        retList.add(addr);
                    }
                }
            }
        } catch (IOException e) {
            Log.e("TAG", "Error calling Google geocode webservice.", e);
        } catch (JSONException e) {
            Log.e("TAG", "Error parsing Google geocode webservice response.", e);
        }

        return retList;
    }
}