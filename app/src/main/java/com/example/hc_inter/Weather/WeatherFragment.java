package com.example.hc_inter.Weather;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import com.example.hc_inter.R;
import com.example.hc_inter.databinding.FragmentWeatherBinding;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class WeatherFragment extends Fragment {


    private FragmentWeatherBinding binding;

    private ImageButton infobutton;

    private TextView textView;
    
    private ImageButton homebutton;

    private ImageView imageView;


    private static final String API_KEY = "ed1203099aa39360cd60dc3f61d5ea11";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";


    private FusedLocationProviderClient fusedLocationClient;
    private TextView textViewWeather;
    private TextView textViewHumidity;
    private TextView textViewWind;

    private TextView texttom;

    private ImageButton back;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());


        infobutton = root.findViewById(R.id.imageButton10);
        textView = root.findViewById(R.id.textView27);
        homebutton = root.findViewById(R.id.imageButton19);
        textViewWeather = root.findViewById(R.id.textView28);
        textViewHumidity = root.findViewById(R.id.textView29);
        textViewWind = root.findViewById(R.id.textView30);
        imageView = root.findViewById(R.id.imageView2);
        texttom = root.findViewById(R.id.textView32);
        back = root.findViewById(R.id.imageButton20);


        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleInfoTextView();
            }
        });

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_weather_to_nav_home);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_weather_to_nav_home);
            }
        });


        fetchLocationAndWeather();
        return root;
    }

    private void fetchLocationAndWeather() {
        //Makes sure that the persmission have been accepted
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            return;
        }

        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location location = task.getResult();
                    fetchWeather(location.getLatitude(), location.getLongitude());
                    fetchForecast(location.getLatitude(), location.getLongitude());
                } else {
                    Toast.makeText(requireContext(), "Δεν καταφέραμε να βρούμε την τοποθεσία σας", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void toggleInfoTextView() {
        //Info button
        if (textView.getVisibility() == View.GONE) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }


    private void fetchWeather(double latitude, double longitude) {
        //Catches the current weather info and the updates the textview for each attributes
        //Also changes the weather picture depending on the temperature
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = weatherService.getCurrentWeather(latitude, longitude, API_KEY, "metric");


        String fullUrl = BASE_URL + "weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY + "&units=metric";
        Log.d("WeatherAPI", "Full URL: " + fullUrl); // Log the full URL


        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    textViewWeather.setText("Θερμοκρασία: " + weatherResponse.getMain().getTemp() + "°C");
                    textViewHumidity.setText("Υγρασία: " + weatherResponse.getMain().getHumidity() + "%");
                    textViewWind.setText("Αέρας: " + weatherResponse.getWind().getSpeed() + " (*)");

                    if (weatherResponse.getMain().getTemp() > 22) {
                        imageView.setImageResource(R.drawable.sunny);
                    } else {
                        imageView.setImageResource(R.drawable.cloudy);
                    }

                } else {
                    Toast.makeText(requireContext(), "Δεν καταφέραμε να βρούμε πληροφορίες για τον καιρό", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Κάτι πήγε στραβά. Παρακαλώ προσπαθήστε αργότερα", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchForecast(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<ForecastResponse> call = weatherService.getForecastWeather(latitude, longitude, API_KEY, "metric");

        call.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ForecastResponse.WeatherForecast> forecasts = response.body().getList();

                    if (forecasts.size() > 0) {
                        String tomorrowDate = LocalDate.now().plusDays(1).toString();

                        for (ForecastResponse.WeatherForecast forecast : forecasts) {
                            if (forecast.getDtTxt().contains(tomorrowDate)) {
                                texttom.setText("Θερμοκρασία αύριο: " + forecast.getMain().getTemp() + "°C");
                                break;
                            }
                        }
                    }
                } else {
                    Log.e("WeatherAPI", "Response code: " + response.code() + ", Response message: " + response.message());
                    Toast.makeText(requireContext(), "Δεν καταφέραμε να βρούμε πληροφορίες για τον καιρό", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.e("WeatherAPI", "Error: " + t.getMessage(), t);
                Toast.makeText(requireContext(), "Κάτι πήγε στραβά. Παρακαλώ προσπαθήστε αργότερα", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface WeatherService {
        @GET("weather")
        Call<WeatherResponse> getCurrentWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String apiKey, @Query("units") String units);

        @GET("forecast")
        Call<ForecastResponse> getForecastWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String apiKey, @Query("units") String units);
    }
    public static class WeatherResponse {
        private Main main;
        private Wind wind;

        public Main getMain() {
            return main;
        }

        public Wind getWind() {
            return wind;
        }

        public static class Main {
            private float temp;
            private int humidity;

            public float getTemp() {
                return temp;
            }

            public int getHumidity() {
                return humidity;
            }
        }

        public static class Wind {
            private float speed;

            public float getSpeed() {
                return speed;
            }
        }
    }

    public static class ForecastResponse {
        private List<WeatherForecast> list;

        public List<WeatherForecast> getList() {
            return list;
        }

        public static class WeatherForecast {
            private Main main;
            private String dt_txt;

            public Main getMain() {
                return main;
            }



            public String getDtTxt() {
                return dt_txt;
            }
        }

        public static class Main {
            private float temp;

            public float getTemp() {
                return temp;
            }

        }
    }
}
