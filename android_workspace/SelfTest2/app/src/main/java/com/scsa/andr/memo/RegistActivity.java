package com.scsa.andr.memo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import android.app.Activity;

public class RegistActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        EditText titleInput = findViewById(R.id.input_title);
        EditText contentInput = findViewById(R.id.input_content);
        EditText dateInput = findViewById(R.id.input_date);
        Button saveButton = findViewById(R.id.button_save);

        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String content = contentInput.getText().toString();
            String date = dateInput.getText().toString();

            MemoDTO memo = new MemoDTO(title, content, date);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("memo", memo);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}

