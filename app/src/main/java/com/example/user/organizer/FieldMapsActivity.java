package com.example.user.organizer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class FieldMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    double myLatitude;              //широта местоположения онлайн
    double myLongitude;             //долгота местоположения онлайн
    boolean isEnabledGPS;           //статус включенного GPS оборудывания
    String strStatusGPS;            //вывод статуса включенного GPS оборудывания
    boolean isEnabledNet;           //статус включенного GSM оборудывания
    String strStatusNet;            //вывод статуса включенного GSM оборудывания
    Spinner spMapField;             //спинер выбора поля для отображения маркера на карте
    Spinner spMapCity;              //спинер выбора города для отображения маркера на карте
    TextView tvGeoLat;              //TextView для вывода широты при создании поля
    TextView tvGeoLong;             //TextView для вывода долготы при создании поля
    LinearLayout llStatus0;         //LinearLayout для вывода элементов при просмотре полей
    LinearLayout llStatus1;         //LinearLayout для вывода элементов при создании поля
//    LatLng myLatLng;                //данные о онлайн местоположении
    List<String> spListField;       //коллекция для спинера выбора поля
    List<String> spListCity;        //коллекция для спинера выбора города
    int mapStatus;                  //статус для работы с картой(0-просмотр полей, 1-создание нового поля)
    Marker selectMarker;

//    ActionBar actionBar;                //стрелка НАЗАД
    Location location;              //переменая для хранения данных о локации
    boolean connection;                // статус подключения к сети

    private GoogleMap mMap;
    List<Field> fieldList = new ArrayList<>();      //коллекция для хранения данных о полях
    Context context;
    DBUtilities dbUtilities;
    DBLocalUtilities dbLocalUtilities;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        //добавляем actionBar (стрелка сверху слева)
//        actionBar = getSupportActionBar();
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        dbUtilities = new DBUtilities(context);
        dbLocalUtilities = new DBLocalUtilities(getApplicationContext());
        dbLocalUtilities.open();
        spListField = new ArrayList<>();
        spListCity = new ArrayList<>();
        spMapCity = (Spinner) findViewById(R.id.spMapCity);
        spMapField = (Spinner) findViewById(R.id.spMapField);
        tvGeoLat = (TextView) findViewById(R.id.tvGeoLat);
        tvGeoLong = (TextView) findViewById(R.id.tvGeoLong);
        llStatus0 = (LinearLayout) findViewById(R.id.llStatus0);
        llStatus1 = (LinearLayout) findViewById(R.id.llStatus1);
        mapStatus = getIntent().getIntExtra("mapStatus", 0);
        connection = getIntent().getBooleanExtra("connection", false);

        //mapStatus - цель вызваной активности
        //(для просмотра полей статус - 0 или для выбора точки расположения поля)
        if(mapStatus == 0) llStatus1.setVisibility(View.GONE);
        else llStatus0.setVisibility(View.GONE);

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
        if(connection) fieldList = dbUtilities.getListField("");
        else fieldList = dbLocalUtilities.getFieldList();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }//onCreate

//    //обработчик actionBar (стрелка сверху слева)
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }//switch
//    }//onOptionsItemSelected

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

        if(mapStatus == 1) {
            //создание начального пустого маркера
            selectMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(1, 1)).title("Новое поле"));
        }//if

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
        }//if
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                myLatitude = latLng.latitude;
                myLongitude = latLng.longitude;
                tvGeoLat.setText(String.valueOf(latLng.latitude));
                tvGeoLong.setText(String.valueOf(latLng.longitude));
                if(mapStatus == 1) {
                    selectMarker.remove();
                    selectMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Новое поле"));
                }//if
            }//onMapClick
        });//setOnMapClickListener
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
            if(mapStatus == 1) {
//                myLatitude = location.getLatitude();
//                myLongitude = location.getLongitude();
//                tvGeoLat.setText(String.valueOf(myLatitude));
//                tvGeoLong.setText(String.valueOf(myLongitude));
//                myLatLng = new LatLng(myLatitude, myLongitude);
//                mMap.addMarker(new MarkerOptions().position(myLatLng).title("мое положение"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
//                mMap.setMinZoomPreference(10.0f);
//                mMap.setMaxZoomPreference(20.0f);
            }//if
        }//onLocationChanged

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

    public void onClick(View view) {
        // создать для передачи результатов
        Intent intent = new Intent(this, CreateFieldActivity.class);

        // пакуем при помощи
        intent.putExtra(CreateFieldActivity.PAR_LAT, myLatitude);
        intent.putExtra(CreateFieldActivity.PAR_LONG, myLongitude);

        // собственно передача параметров
        setResult(RESULT_OK, intent);
        onBackPressed();
    }//onClick
}//FieldMapsActivity
