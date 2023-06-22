package kr.jaen.android.broadcastreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_SCSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnBroadcast = findViewById(R.id.btn_broadcast);
        btnBroadcast.setOnClickListener(view -> {
            Intent intent = new Intent("edu.jaen.android.news.funny");
            intent.putExtra("content", "티끌은 모아도 티끌이다.");
            
            // permission 사용자 대상으로 Broadcast 발송
            sendBroadcast(intent, "edu.jaen.android.news.funny.PRIVATE");
            Log.d(TAG, "onCreate()::발송 완료");
        });

        Button btnBroadcast2 = findViewById(R.id.btn_broadcast2);
        btnBroadcast2.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, BroadcastActivity.class);
            startActivity(intent);
        });
    }
}