package com.example.youssef.upsalestest.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.youssef.upsalestest.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.example.youssef.upsalestest.R.id.map;

/**
 * Created by Youssef on 12/21/2016.
 */

public class ClientInfoFragment extends Fragment implements OnMapReadyCallback {

    // The Client's name
    String mName;
    // The Client's address
    String mAddress;
    // The main map
    MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_info, container, false);

        // Getting the map view and starting the map initialization
        mapView = (MapView) v.findViewById(map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return v;
    }

    // The main initialization
    private void init(){
        // Setting the top Action bar name to the Client name
        TextView title = (TextView)getActivity().findViewById(R.id.title);
        title.setText(getArguments().getString("name"));
        // Getting the address and name from the passed arguments
        mAddress = getArguments().getString("address");
        mName = getArguments().getString("name");
        // Showing the top right back button
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }


    // The main geocoder to get the Lat and Lng coordinates from the address
    public LatLng getLocationFromAddress(){
        Geocoder coder= new Geocoder(getActivity());
        List<Address> address;
        LatLng p1 = null;
        try
        {
            address = coder.getFromLocationName(mAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Setting up the marker with the position and title
        LatLng position = getLocationFromAddress();
        Marker marker = googleMap.addMarker(new MarkerOptions()
               .position(position)
               .title(mName));
        marker.showInfoWindow();
        // Zoom into the location
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 15);
        googleMap.animateCamera(cameraUpdate);
    }
}
