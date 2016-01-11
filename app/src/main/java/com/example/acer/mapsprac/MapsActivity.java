package com.example.acer.mapsprac;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView myAddress;
    TextView service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        myAddress = (TextView)findViewById(R.id.textView);
        service=(TextView)findViewById(R.id.textView1);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);


        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //Log.e("TAG", googleMap.getCameraPosition().target.toString());

                Toast.makeText(
                        MapsActivity.this,
                        "Lat " + mMap.getCameraPosition().target.latitude + " "
                                + "Long " + mMap.getCameraPosition().target.longitude,
                        Toast.LENGTH_LONG).show();

            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                Log.e("asd", String.valueOf(cameraPosition.target.latitude) + "");
                if (cameraPosition.target.latitude != 0 && cameraPosition.target.longitude != 0) {
                    getMyLocationAddress((cameraPosition.target.latitude), (cameraPosition.target.longitude));

                }
            }
        });

    }





    public void getMyLocationAddress(double lat, double longi) {

        int flag=0;
        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);
        JSONArray obj=null;
        try {
            obj = new JSONArray("[ {\n" +
                    "    \"postalCode\": 560034,\n" +
                    "    \"Locality\": \"Kormangala\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"postalCode\": 560030,\n" +
                    "    \"Locality\": \"Koramangala\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"postalCode\": 560095,\n" +
                    "    \"Locality\": \"Koramangala\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"postalCode\": 560068,\n" +
                    "    \"Locality\": \"Koramangala\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"postalCode\": 560078,\n" +
                    "    \"Locality\": \"JP Nagar\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"postalCode\": 560041,\n" +
                    "    \"Locality\": \"Jayanagar\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"postalCode\": 560076,\n" +
                    "    \"Locality\": \"BTM\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"postalCode\": 560029,\n" +
                    "    \"Locality\": \"BTM 1\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"postalCode\": 560070,\n" +
                    "    \"Locality\": \"Jayanagar west\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"postalCode\": 560069,\n" +
                    "    \"Locality\": \"Jayanagar east\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"postalCode\": 560011,\n" +
                    "    \"Locality\": \"Jayanagar lil block\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"postalCode\": 560047,\n" +
                    "    \"Locality\": \"koramangala\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"postalCode\": 560102,\n" +
                    "    \"Locality\": \"HSR\"\n" +
                    "  }]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            //Place your latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(lat,longi, 1);

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
                    JSONObject jsonobject = null;
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
