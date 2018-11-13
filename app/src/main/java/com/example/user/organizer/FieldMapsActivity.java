package com.example.user.organizer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class FieldMapsActivity extends FragmentActivity implements OnMapReadyCallback {
//    double myLatitude;
//    double myLongitude;
    boolean isEnabledGPS;
    String strStatusGPS;
    boolean isEnabledNet;
    String strStatusNet;
    Spinner spMapField;
    Spinner spMapCity;
//    LatLng myLatLng;
    List<String> spListField;
    List<String> spListCity;

    Location location;

    private GoogleMap mMap;
    List<Field> fieldList = new ArrayList<>(); //коллекция полей
    Context context;
    DBUtilities dbUtilities;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        dbUtilities = new DBUtilities(context);
        spListField = new ArrayList<>();
        spListCity = new ArrayList<>();
        spMapCity = (Spinner) findViewById(R.id.spMapCity);
        spMapField = (Spinner) findViewById(R.id.spMapField);

        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.getStrListTableFromDB("cities", "name");
        spMapCity.setAdapter(buildSpinnerStr(spListCity));

        spMapCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //заполнить spListField данные для отображения в Spinner
                spListField = dbUtilities.getSomeFieldsfromDB(spMapCity.getItemAtPosition(position).toString());
                spMapField.setAdapter(buildSpinnerStr(spListField));
            }//onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }//onNothingSelected
        });//setOnItemSelectedListener

        spMapField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //получаем id выбранного поля
                String idField = dbUtilities.getIdByValue(
                        "fields",
                        "name",
                        spMapField.getItemAtPosition(position).toString()
                );//idField
                //получаем информацию по выбранному полу
                Field field = dbUtilities.getListField(idField).get(0);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Double.parseDouble(field.geo_lat), Double.parseDouble(field.geo_long))
                        , 12.0f
                        )//newLatLngZoom
                );//moveCamera
            }//onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }//onNothingSelected
        });//setOnItemSelectedListener

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //получаем коллекцию полей
        fieldList = dbUtilities.getListField("");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }//onCreate

    //строим Spinner
    private ArrayAdapter buildSpinnerStr(List<String> list) {

        ArrayAdapter<String> spinnerAdapter;

        //создание адаптера для спинера
        spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                list
        );

        // назначение адапетра для списка
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return spinnerAdapter;
    }//buildCitySpinner

    //заполняем карту маркерами
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Выставляем маркеры
        LatLng latLng;
        for (Field field : fieldList) {
            latLng = new LatLng(Double.parseDouble(field.geo_lat), Double.parseDouble(field.geo_long));
            mMap.addMarker(new MarkerOptions().position(latLng).title(field.name));
        }//foreach

        //кнопка для посика моей геопозиции
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

    }//onMapReady

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 1, locationListener);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000, 1,
                locationListener);
        checkEnabled();
    }//onResume

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }//onPause

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
//            myLatitude = location.getLatitude();
//            myLongitude = location.getLongitude();
//            myLatLng = new LatLng(myLatitude, myLongitude);
//            mMap.addMarker(new MarkerOptions().position(myLatLng).title("мое положение"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
//            mMap.setMinZoomPreference(10.0f);
//            mMap.setMaxZoomPreference(20.0f);
        }

        @Override
        public void onProviderDisabled(String provider) { checkEnabled(); }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            if (ActivityCompat.checkSelfPermission(getBaseContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getBaseContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }//

            location = locationManager.getLastKnownLocation(provider);
        }//onProviderEnabled

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                strStatusGPS = String.valueOf(status);
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                strStatusNet = String.valueOf(status);
            }//if-else
        }//onStatusChanged
    };//locationListener

    private void checkEnabled() {
        isEnabledGPS = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isEnabledNet = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }//checkEnabled

}//FieldMapsActivity
