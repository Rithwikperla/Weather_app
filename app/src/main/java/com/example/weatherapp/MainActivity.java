package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private RelativeLayout homeRL;
    private ProgressBar loading;
    private TextView citynameview,tempview,conditionview;
    private TextInputEditText cityedit;
    private ImageView searchlogo,iconview,backview,currentlocation;
    private RecyclerView weather;
    private ArrayList<WeatherRVModel> WeatherRVModelArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        homeRL = findViewById(R.id.Home);
        loading = findViewById(R.id.loading);
        citynameview = findViewById(R.id.city);
        tempview = findViewById(R.id.tempview);
        conditionview = findViewById(R.id.tempcondition);
        weather = findViewById(R.id.rvweather);
        cityedit = findViewById(R.id.editcity);
        searchlogo = findViewById(R.id.Ivsearch);
        backview = findViewById(R.id.imageback);
        iconview = findViewById(R.id.imagetemp);
        currentlocation = findViewById(R.id.currentlocation);

        WeatherRVModelArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this,WeatherRVModelArrayList);
        weather.setAdapter(weatherRVAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        searchlogo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String newcity = cityedit.getText().toString();

                if(newcity.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Please enter city name",Toast.LENGTH_SHORT).show();
                }
                else{
                    getWeatherInfo(newcity);
                    citynameview.setText(newcity);
                }
            }
        });
        currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherInfo(cityName);
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        cityName  = getCityName(location.getLongitude(),location.getLatitude());
        getWeatherInfo(cityName);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CODE )
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this,"Please provide the permissions.",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(double longitude, double latitude){
        String cityname = "Not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try{
            List<Address> addresses = gcd.getFromLocation(latitude,longitude,1);
            for(Address adr : addresses){
                if(adr != null)
                { String city = adr.getLocality();
                    if(city != null && !city.equals("")) {
                        cityname=city;
                    }else{
                        Log.d("Tag","City not found");
                        Toast.makeText(this,"User city not found...",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return cityname;
    }
    private void getWeatherInfo(String cityname){
        String url = "https://api.weatherapi.com/v1/forecast.json?key=c5714249f35940a0b02132556222202&q="+cityname+"&days=1&aqi=yes&alerts=yes";
        citynameview.setText(cityname);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                WeatherRVModelArrayList.clear();
                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    tempview.setText(temperature + "??C");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionicon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("https:".concat(conditionicon)).into(iconview);
                    conditionview.setText(condition);
                    if (isDay == 1) {
                        Picasso.get().load(R.drawable.morning).into(backview);
                    } else {
                        Picasso.get().load(R.drawable.night).into(backview);
                    }
                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastArray = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourarray = forecastArray.getJSONArray("hour");

                    for (int k = 0; k < hourarray.length(); k++) {
                        JSONObject hourobj = hourarray.getJSONObject(k);
                        String time = hourobj.getString("time");
                        String temp = hourobj.getString("temp_c");
                        String img = hourobj.getJSONObject("condition").getString("icon");
                        //Toast.makeText(MainActivity.this,img,Toast.LENGTH_SHORT).show();
                        String wind = hourobj.getString("wind_kph");
                        WeatherRVModelArrayList.add(new WeatherRVModel(time, temp, img, wind));
                    }
                    weatherRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Please enter valid city name",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}