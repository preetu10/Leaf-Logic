package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirstapp.Modals.WeatherData;
import com.example.myfirstapp.Modals.main;
import com.example.myfirstapp.Modals.weather;
import com.example.myfirstapp.databinding.ActivityWeatherBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    ActivityWeatherBinding binding;

    ConstraintLayout constraintLayout;
    private EditText getCityName;
    Toolbar toolbar;
    SessionManager sessionManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(getApplicationContext());
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getCityName=(EditText)findViewById(R.id.get_city);
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        String currentDate= format.format(new Date());
        binding.date.setText(currentDate);

        fetchWeather("Chittagong");
        binding.submitCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = getCityName.getText().toString().trim();
                hideKeyBoard();
                if(cityName.isEmpty()){
                    Toast.makeText(WeatherActivity.this,"City Name is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
                fetchWeather(cityName);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.dashboard_item){
            Intent intent = new Intent(WeatherActivity.this,DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if(id==R.id.logout_item){
            if(sessionManager.isLoggedIn()) {
                FirebaseAuth.getInstance().signOut();
                sessionManager.setLoggedIn(false);
                Intent intent = new Intent(WeatherActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
        return true;
    }

    void hideKeyBoard(){
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(binding.constraintId.getApplicationWindowToken(),0);
    }
    void fetchWeather(String cityName){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceApi interfaceApi=retrofit.create(InterfaceApi.class);
        Call<WeatherData> call =interfaceApi.getData(cityName,"ce2fc21c1eb368cdd393471e31eb33eb","metric");

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if(response.isSuccessful()){
                    WeatherData weatherData=response.body();

                    main to= weatherData.getMain();

                    binding.temperature.setText(String.valueOf(to.getTemp())+"\u2103");
                    binding.maxTemperature.setText(String.valueOf(to.getTemp_max())+"\u2103");
                    binding.minTemperature.setText(String.valueOf(to.getTemp_min())+"\u2103");
                    binding.humidity.setText(String.valueOf(to.getHumidity())+"%");
                    binding.pressure.setText(String.valueOf(to.getPressure()));
                    binding.feelsLike.setText(String.valueOf(to.getFeels_like())+"\u2103");
                    binding.city.setText(weatherData.getName());

                    List<weather>description=weatherData.getWeather();
                    for(weather data :description){
                        binding.description.setText(data.getDescription());
                    }

                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Toast.makeText(WeatherActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
}