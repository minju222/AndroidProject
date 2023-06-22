package com.scsa.uitestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scsa.uitestapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        // 1. gradle module에   viewBinding{enabled = true} 추가한 경우 추가하는 코드
        // 2. inflate : xml을 java로 바꾸는 것 / getLayoutInflater(): mainactivity에 해당하는 xml을 가져오는 것
        // 3. setContentView(R.layout.activity_main) -> setContentView(binding.getRoot());로 변경
        // 4. findViewById 필요 없어짐 ->  binding.button 이런 식으로 변경
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        Button button = findViewById(R.id.button);
//        EditText editText = findViewById(R.id.edittext);

//        button.setOnClickListener(v -> {
//            Toast.makeText(this, editText.getText(), Toast.LENGTH_SHORT).show();
//        });

        binding.button.setOnClickListener(v -> {
            Toast.makeText(this, binding.edittext, Toast.LENGTH_SHORT).show();
        });

    }
}