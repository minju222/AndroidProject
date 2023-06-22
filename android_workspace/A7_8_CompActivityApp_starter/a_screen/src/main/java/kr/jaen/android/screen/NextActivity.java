package kr.jaen.android.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        String name = getIntent().getStringExtra("name");
        if (name != null) {
            Toast.makeText(this, name, Toast.LENGTH_SHORT)
                    .show();
        }

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("msg", "다시 호출되기");

            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}