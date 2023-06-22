package kr.jaen.storage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.jaen.storage.file.FileInputActivity;
import kr.jaen.storage.file.FileOutputActivity;
import kr.jaen.storage.sharedpreferences.MainActivity;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.button_0).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
        findViewById(R.id.button_1).setOnClickListener(v -> {
            startActivity(new Intent(this, FileInputActivity.class));
        });
        findViewById(R.id.button_2).setOnClickListener(v -> {
            startActivity(new Intent(this, FileOutputActivity.class));
        });
        findViewById(R.id.button_3).setOnClickListener(v -> {
            startActivity(new Intent(this, kr.jaen.storage.sqlite.MainActivity.class));
        });
    }
}
