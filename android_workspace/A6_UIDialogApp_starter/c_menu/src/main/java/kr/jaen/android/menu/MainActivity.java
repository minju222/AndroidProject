package kr.jaen.android.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_1).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, MenuXMLActivity.class));
        });

        findViewById(R.id.btn_2).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, MenuJavaActivity.class));
        });
    }
}