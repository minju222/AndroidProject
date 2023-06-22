package com.scsa.androidproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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
    private static final String TAG = "WeatherMainActivity_SCSA";

    private static final String API_KEY = "e9ae81e2a92a2a547e79050c60eac631";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=Seoul,kr&APPID=e9ae81e2a92a2a547e79050c60eac631";

    private TextView weatherTextView;

    private TextView conditionTextView;

//    private NfcAdapter nAdapter;
//
//    PendingIntent pIntent;
//
//    IntentFilter[] filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

//        nAdapter = NfcAdapter.getDefaultAdapter(this);
//
//        // NFC 관리자 및 어댑터 설정
//        NfcManager nfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);
//        nAdapter = nfcManager.getDefaultAdapter();
//        Intent i = new Intent(this, MainActivity.class);
//        pIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_MUTABLE);
//        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//
//        try {
//            filter.addDataType("*/*");
//        } catch (IntentFilter.MalformedMimeTypeException e) {
//            e.printStackTrace();
//        }
//        filters = new IntentFilter[]{filter,};

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
                                    .load(R.drawable.haze) // 구름이 있는 배경 GIF 파일의 리소스 ID
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
                            ImageView backgroundImageView4 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.rainy) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView4);
                            break;
                        case "Thunderstorm":
                            conditionText = getString(R.string.weather_thunderstorm);
                            ImageView backgroundImageView10 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.thunder) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView10);
                            weatherTextView.setTextColor(Color.WHITE);
                            conditionTextView.setTextColor(Color.WHITE);
                            textTextView.setTextColor(Color.WHITE);
                            break;
                        case "Snow":
                            conditionText = getString(R.string.weather_snow);
                            break;
                        case "Mist":
                            conditionText = getString(R.string.weather_mist);
                            ImageView backgroundImageView6 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.rainy) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView6);
                            break;
                        case "Smoke":
                            conditionText = getString(R.string.weather_smoke);
                            ImageView backgroundImageView7 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.rainy) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView7);
                            break;
                        case "Haze":
                            conditionText = getString(R.string.weather_haze);
                            ImageView backgroundImageView5 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.haze) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView5);
                            weatherTextView.setTextColor(Color.WHITE);
                            conditionTextView.setTextColor(Color.WHITE);
                            textTextView.setTextColor(Color.WHITE);
                            break;
                        case "Dust":
                            conditionText = getString(R.string.weather_dust);
                            ImageView backgroundImageView14 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.haze) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView14);
                            weatherTextView.setTextColor(Color.WHITE);
                            conditionTextView.setTextColor(Color.WHITE);
                            textTextView.setTextColor(Color.WHITE);
                            break;
                        case "Fog":
                            conditionText = getString(R.string.weather_fog);
                            ImageView backgroundImageView8 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.rainy) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView8);
                            weatherTextView.setTextColor(Color.WHITE);
                            conditionTextView.setTextColor(Color.WHITE);
                            textTextView.setTextColor(Color.WHITE);
                            break;
                        case "Sand":
                            conditionText = getString(R.string.weather_sand);
                            ImageView backgroundImageView15 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.haze) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView15);
                            weatherTextView.setTextColor(Color.WHITE);
                            conditionTextView.setTextColor(Color.WHITE);
                            textTextView.setTextColor(Color.WHITE);
                            break;
                        case "Ash":
                            conditionText = getString(R.string.weather_ash);
                            ImageView backgroundImageView16 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.haze) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView16);
                            weatherTextView.setTextColor(Color.WHITE);
                            conditionTextView.setTextColor(Color.WHITE);
                            textTextView.setTextColor(Color.WHITE);
                            break;
                        case "Squall":
                            conditionText = getString(R.string.weather_squall);
                            ImageView backgroundImageView12 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.thunder) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView12);
                            weatherTextView.setTextColor(Color.WHITE);
                            conditionTextView.setTextColor(Color.WHITE);
                            textTextView.setTextColor(Color.WHITE);
                            break;
                        case "Tornado":
                            conditionText = getString(R.string.weather_tornado);
                            ImageView backgroundImageView11 = findViewById(R.id.backgroundImageView);
                            Glide.with(WeatherMainActivity.this)
                                    .load(R.drawable.thunder) // 구름이 있는 배경 GIF 파일의 리소스 ID
                                    .into(backgroundImageView11);
                            weatherTextView.setTextColor(Color.WHITE);
                            conditionTextView.setTextColor(Color.WHITE);
                            textTextView.setTextColor(Color.WHITE);
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
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // NFC 태그 감지 시 인텐트 필터 설정
//        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        try {
//            filter.addDataType("*/*");
//        } catch (IntentFilter.MalformedMimeTypeException e) {
//            e.printStackTrace();
//        }
//        IntentFilter[] filters = new IntentFilter[]{filter};
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//        if (nAdapter != null) {
//            nAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        // 포그라운드 디스패치 비활성화
//        if (nAdapter != null) {
//            nAdapter.disableForegroundDispatch(this);
//        }
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        // NFC 태그 감지 시 호출됩니다.
//        Log.d(TAG, "onNewIntent: "+intent.getAction());
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
//            // NFC 태그 감지 시 WeatherMainActivity로 이동합니다.
//            Intent weatherIntent = new Intent(this, WeatherMainActivity.class);
//            startActivity(weatherIntent);
//            finish();
//        }
//    }

}
