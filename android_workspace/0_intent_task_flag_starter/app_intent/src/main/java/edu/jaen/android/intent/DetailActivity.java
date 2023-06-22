package edu.jaen.android.intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity_SCSA";

    private int value = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tv = findViewById(R.id.textView);
        tv.setText(String.valueOf(value));

        findViewById(R.id.plus).setOnClickListener( v -> {
            value ++;
            tv.setText(String.valueOf(value));
        });

        findViewById(R.id.minus).setOnClickListener( v -> {
            value --;
            tv.setText(String.valueOf(value));
        });

        Log.d(TAG, "onCreate: ");
    }

}