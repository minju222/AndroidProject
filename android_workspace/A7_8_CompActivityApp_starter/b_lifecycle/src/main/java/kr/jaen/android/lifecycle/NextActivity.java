package kr.jaen.android.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class NextActivity extends AppCompatActivity {

    private static final String TAG = "NextActivity_SCSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Log.d(TAG, "onCreate() - NextActivity");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause() - NextActivity");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop() - NextActivity");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart() - NextActivity");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() - NextActivity");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() - NextActivity");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy() - NextActivity");
        super.onDestroy();
    }
}