package com.scsa.andr.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import androidx.appcompat.widget.AppCompatButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatButton alarmButton = findViewById(R.id.alarmBtn);
        AppCompatButton mouseCatchButton = findViewById(R.id.mouseCatchBtn);
        AppCompatButton xmlParserButton = findViewById(R.id.xmlParserBtn);

        alarmButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AlarmMainActivity.class);
            startActivity(intent);
        });

        mouseCatchButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MouseCatchActivity.class);
            startActivity(intent);
        });

        xmlParserButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, XmlPullParserActivity.class);
            startActivity(intent);
        });
    }
}
