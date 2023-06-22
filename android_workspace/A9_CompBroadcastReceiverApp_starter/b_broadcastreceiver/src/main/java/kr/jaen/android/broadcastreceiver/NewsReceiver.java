package kr.jaen.android.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NewsReceiver extends BroadcastReceiver {

    private static final String TAG = "NewsReceiver_SCSA";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: receive 받기: " + intent.getAction());
        Log.d(TAG, "onReceive: extra: " + intent.getStringExtra("content"));

        Toast.makeText(context, "onReceive: receive 받기 성공!!", Toast.LENGTH_SHORT).show();
    }
}