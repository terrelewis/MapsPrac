package com.example.acer.mapsprac;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView myAddress;
    TextView service;
    JSONArray obj = null;
    private static final String TAG = "FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        String tag_json_arry = "json_array_req";

        String url = "http://fabfresh.elasticbeanstalk.com/users/postalcode/";

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, response.toString() + "");
                        if (response != null && response.length() > 6) {

                            obj = new JSONArray();
                            obj = response;


                        } else {
                            Log.e(TAG, "obj is null");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage() + "");

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer hari");

                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(req, tag_json_arry);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        myAddress = (TextView) findViewById(R.id.textView);
        service = (TextView) findViewById(R.id.textView1);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                Log.e("asd", mMap.getCameraPosition().target.toString());

            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                Log.e("asd", String.valueOf(cameraPosition.target.latitude) + "");
                if (cameraPosition.target.latitude != 0 && cameraPosition.target.longitude != 0 &&cameraPosition.target.latitude>-90 && cameraPosition.target.latitude<90 && cameraPosition.target.longitude>-180 &&cameraPosition.target.longitude<180) {

                    try {
                        getMyLocationAddress((cameraPosition.target.latitude), (cameraPosition.target.longitude));
                    }catch(IndexOutOfBoundsException e){Toast.makeText(
                            MapsActivity.this,
                            "No service" ,
                            Toast.LENGTH_LONG).show();}
                }
            }
        });

    }

  public void getMyLocationAddress(double lat, double longi) {

        int flag=0;
        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);
      List<Address> addresses=null;
        try {

            //Place your latitude and longitude
           try {
                addresses = geocoder.getFromLocation(lat, longi, 1);
           }catch(IndexOutOfBoundsException e){Toast.makeText(
                   MapsActivity.this,
                   "No service" ,
                   Toast.LENGTH_LONG).show();}
            if(addresses != null) {

                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }
                myAddress.setText("Loc: " +strAddress.toString());
                String str=strAddress.toString();
                String s=str.substring(str.length()-7, str.length()-1);
                for (int i = 0; i < obj.length(); i++) {
                    JSONObject jsonobject;
                    try {
                        jsonobject = obj.getJSONObject(i);
                        String postal = jsonobject.getString("postalCode");
                        if(s.equals(postal))
                        {
                            service.setText("Service available");
                            flag=1;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                if(flag==0)
                    service.setText("Service unavailable");

            }

            else
                myAddress.setText("No location found..!");

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not get address..!", Toast.LENGTH_LONG).show();
        }
    }


}
