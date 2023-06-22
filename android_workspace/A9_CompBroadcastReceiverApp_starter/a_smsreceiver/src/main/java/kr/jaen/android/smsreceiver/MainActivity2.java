package kr.jaen.android.smsreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "MainActivity2_SCSA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Intent intent = getIntent();
        String sms = intent.getStringExtra("sms");
        Log.d(TAG, "onCreate: "+sms);
    }
}