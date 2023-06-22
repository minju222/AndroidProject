package com.scsa.andr.memo;

// DetailActivity.java

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView titleView = findViewById(R.id.title);
        TextView contentView = findViewById(R.id.content);
        TextView dateView = findViewById(R.id.date);
        Button backButton = findViewById(R.id.button_back);

        MemoDTO memo = (MemoDTO) getIntent().getSerializableExtra("memo");

        titleView.setText(memo.getTitle());
        contentView.setText(memo.getContent());
        dateView.setText(memo.getDate());

        backButton.setOnClickListener(v -> {
            finish();
        });
    }
}

