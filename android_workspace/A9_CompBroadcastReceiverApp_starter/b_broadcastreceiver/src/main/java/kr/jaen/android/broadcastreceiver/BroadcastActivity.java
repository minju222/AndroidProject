package kr.jaen.android.broadcastreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class BroadcastActivity extends AppCompatActivity {

    private static final String TAG = "BroadcastActivity_SCSA";

    private BroadcastReceiver screenReceiver;
    private BroadcastReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        registScreenOnOff();
        registBatteryLow();
    }

    private void registScreenOnOff() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);

        screenReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(TAG, "onReceive()::" + action);

                switch (action) {
                    case Intent.ACTION_SCREEN_ON :
                        Log.d(TAG,"ACTION_SCREEN_ON");
                        break;

                    case Intent.ACTION_SCREEN_OFF :
                        Log.d(TAG,"ACTION_SCREEN_OFF");
                        break;
                }
            }
        };

        registerReceiver(screenReceiver, filter);
    }

    // emulator에서 battery 수치 15이하로 조정
    private void registBatteryLow() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_LOW);

        batteryReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(TAG, "onReceive()::" + action);

                switch (action) {
                    case Intent.ACTION_BATTERY_LOW :
                        Log.d(TAG,"ACTION_BATTERY_LOW");
                        break;
                }
            }
        };

        registerReceiver(batteryReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(screenReceiver);
        unregisterReceiver(batteryReceiver);
        super.onDestroy();
    }
}