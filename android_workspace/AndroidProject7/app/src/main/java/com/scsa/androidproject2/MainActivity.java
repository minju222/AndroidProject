package com.scsa.androidproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button alarmButton = findViewById(R.id.todoBtn);
        Button mouseCatchButton = findViewById(R.id.newsBtn);
        Button xmlParserButton = findViewById(R.id.mouseBtn);
        Button weatherButton = findViewById(R.id.weatherBtn);

        alarmButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteListActivity.class);
            startActivity(intent);
        });

        mouseCatchButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewsMainActivity.class);
            startActivity(intent);
        });

        xmlParserButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MouseMainActivity.class);
            startActivity(intent);
        });

        weatherButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WeatherMainActivity.class);
            startActivity(intent);
        });
    }
}