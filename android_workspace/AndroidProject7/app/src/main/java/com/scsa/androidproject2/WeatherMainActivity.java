package com.scsa.androidproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherMainActivity extends AppCompatActivity {
    private static final String API_KEY = "e9ae81e2a92a2a547e79050c60eac631";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=Seoul,kr&APPID=e9ae81e2a92a2a547e79050c60eac631";

    private TextView weatherTextView;

    private TextView conditionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

        Button returnButton = findViewById(R.id.returnBtn);
        returnButton.setOnClickListener(v->{
            Intent intent = new Intent(WeatherMainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        weatherTextView = findViewById(R.id.textView);
        conditionTextView = findViewById(R.id.condition);

        FetchWeatherTask weatherTask = new FetchWeatherTask();
        weatherTask.execute();
    }

    private class FetchWeatherTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String weatherJsonStr = null;

            try {
                URL url = new URL(API_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                weatherJsonStr = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return weatherJsonStr;
        }

        @Override
        protected void onPostExecute(String weatherJsonStr) {
            TextView textTextView = findViewById(R.id.text);

            if (weatherJsonStr != null) {
                try {
                    JSONObject weatherJson = new JSONObject(weatherJsonStr);
                    JSONObject mainJson = weatherJson.getJSONObject("main");
                    double temperature = mainJson.getDouble("temp");
                    double n_temp = (Math.round(temperature-273.15)*100)/100.0;
                    String weatherText = n_temp + " °C";
                    String conditionText;
                    switch (weatherJson.getJSONArray("weather").getJSONObject(0).getString("main")) {
                        case "Clear":
                            conditionText = getString(R.string.weather_clear);
                            ImageView backgroundImageView3 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.sunny) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView3);
                            break;
                        case "Clouds":
                            conditionText = getString(R.string.weather_clouds);
                            ImageView backgroundImageView = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.cloud) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView);
                            weatherTextView.setTextColor(Color.WHITE);
                            conditionTextView.setTextColor(Color.WHITE);
                            textTextView.setTextColor(Color.WHITE);
                            break;
                        case "Rain":
                            conditionText = getString(R.string.weather_rain);
                            ImageView backgroundImageView2 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.rainy) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView2);
                            break;
                        case "Drizzle":
                            conditionText = getString(R.string.weather_drizzle);
                            break;
                        case "Thunderstorm":
                            conditionText = getString(R.string.weather_thunderstorm);
                            break;
                        case "Snow":
                            conditionText = getString(R.string.weather_snow);
                            break;
                        case "Mist":
                            conditionText = getString(R.string.weather_mist);
                            break;
                        case "Smoke":
                            conditionText = getString(R.string.weather_smoke);
                            break;
                        case "Haze":
                            conditionText = getString(R.string.weather_haze);
                            break;
                        case "Dust":
                            conditionText = getString(R.string.weather_dust);
                            break;
                        case "Fog":
                            conditionText = getString(R.string.weather_fog);
                            break;
                        case "Sand":
                            conditionText = getString(R.string.weather_sand);
                            break;
                        case "Ash":
                            conditionText = getString(R.string.weather_ash);
                            break;
                        case "Squall":
                            conditionText = getString(R.string.weather_squall);
                            break;
                        case "Tornado":
                            conditionText = getString(R.string.weather_tornado);
                            break;
                        default:
                            conditionText = weatherJson.getJSONArray("weather").getJSONObject(0).getString("main");
                            break;
                    }
                    weatherTextView.setText(weatherText);
                    conditionTextView.setText(conditionText);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
