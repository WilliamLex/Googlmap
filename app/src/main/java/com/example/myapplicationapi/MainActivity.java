package com.example.myapplicationapi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplicationapi.WebService.Asynchtask;
import com.example.myapplicationapi.WebService.WebService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, Asynchtask {
    GoogleMap mapa;

    Double distance = 0.00;
    List<LatLng> Posiciones;
    PolylineOptions lineas;
    AlertDialog.Builder builder;
    Integer count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Posiciones = new ArrayList<>();
        lineas =  new PolylineOptions();
        lineas.width(8);
        lineas.color(Color.RED);
        builder = new AlertDialog.Builder(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        LatLng madrid = new LatLng(40.416215, -3.672086);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(madrid)
                .zoom(19)
                .bearing(45)
                .tilt(70) //punto de vista de la cámara 70 grados
                .build();
        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.animateCamera(camUpd3);
        mapa.setOnMapClickListener(this);


    }

    @Override
    public void onMapClick(LatLng latLng) {
        LatLng punto = new LatLng(latLng.latitude, latLng.longitude);
        mapa.addMarker(new MarkerOptions().position(punto)
                .title("Marker"));
        lineas.add(latLng);


        if (lineas.getPoints().size()==6){
            lineas.add(lineas.getPoints().get(0));
            //recorrer las lineas y verlas en la consola
            for(int i=1; i<lineas.getPoints().size();i++){


                if (i==lineas.getPoints().size()) {
                    Enviar_WebService(lineas.getPoints().get(0).latitude+","+lineas.getPoints().get(0).longitude,
                            lineas.getPoints().get(i).latitude+","+lineas.getPoints().get(i).longitude);
                }
                Enviar_WebService(lineas.getPoints().get(i).latitude+","+lineas.getPoints().get(i).longitude,
                        lineas.getPoints().get(i-1).latitude+","+lineas.getPoints().get(i-1).longitude);

            }
            mapa.addPolyline(lineas);
            lineas.getPoints().clear();
        }

    }
    public void Enviar_WebService(String param1, String param2){
        Log.i("Coordenadas2","envio");
        String link = "https://maps.googleapis.com/maps/api/distancematrix/json?destinations="+
                param1+"&origins="+
                param2+"&units=meters&key=AIzaSyDMmRXHBYOjJyXZruXemR11tl7uiJ2T_Q8";
        Map<String, String> datos = new HashMap<String, String>();
        WebService ws= new WebService(link,
                datos, MainActivity.this, MainActivity.this);
        ws.execute("GET");

        Log.i("Coordenadas2",link);
    }
    @Override
    public void processFinish(String result) throws JSONException {
        JSONObject jObject = new JSONObject(result);
        JSONArray jResult = jObject.getJSONArray("rows");
        JSONObject respuesta = jResult.getJSONObject(0);

        JSONArray row = respuesta.getJSONArray("elements");
        for (int i = 0; i < row.length(); i++){
            JSONObject jdistance = row.getJSONObject(i);
            JSONObject valor = jdistance.getJSONObject("distance");
            distance += Double.parseDouble(valor.getString("value"));
        }
        Log.i("DISTANCIA", "La distancia actual es"+distance.toString());
        count++;
        if(count==6){
            count=0;
            String messge = distance.toString() + " metros";
            builder.setTitle("Distancia calculada")
                    .setMessage(messge)
                    .setCancelable(true).show();
            builder.show();
        }
    }
}

