package com.example.myapplicationapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
    GoogleMap.OnMapClickListener {
    GoogleMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);


        CameraUpdate camUpd1 = CameraUpdateFactory
                        .newLatLngZoom(new LatLng(40.690713693371926,
                                        -74.0456805754362),
                                        15);
        mapa.moveCamera(camUpd1);

        LatLng madrid = new LatLng(40.690713693371926, -74.0456805754362);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(madrid)
                .zoom(19)
                .bearing(45) //noreste arriba
                .tilt(70) //punto de vista de la cámara 70 grados
                .build();
        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);
        mapa.animateCamera(camUpd3);
        mapa.setOnMapClickListener(this);


    }
    PolylineOptions lineas = new
            PolylineOptions();
    @Override
    public void onMapClick(@NonNull LatLng latLng) {

        MarkerOptions marcador = new MarkerOptions();

        marcador.position(latLng);
        marcador.title("Punto");
        mapa.addMarker(marcador);

        lineas.add(latLng);
        if(lineas.getPoints().size()==6){
            lineas.add(lineas.getPoints().toArray(new LatLng[0]));
            lineas.width(8);
            lineas.color(Color.RED);
            mapa.addPolyline(lineas);
            lineas.getPoints().clear();
            
        }





    }


}