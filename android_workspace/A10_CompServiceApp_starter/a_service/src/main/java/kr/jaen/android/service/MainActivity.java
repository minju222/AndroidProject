package kr.jaen.android.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
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

        Button btnStart = findViewById(R.id.btn_start);
        Button btnStop = findViewById(R.id.btn_stop);

        //intent 생성 방법 세가지
        //Intent intent = new Intent(this, MyService.class);
        //Intent intent = new Intent();
        //intent.setClass(this, MyService.class);

        //위의 세개랑 같은 방법
        ComponentName name = new ComponentName(
                "kr.jaen.android.service",
                "kr.jaen.android.service.MusicService"); //MusicService실행하는 경우
        Intent intent = new Intent();
        intent.setComponent(name);

        //startService 버튼 누르면 intent인 MyService.class 실행(onCreate, onStartcommand 실행됨)
        btnStart.setOnClickListener(view -> {
            startService(intent);
        });

        //stopService 버튼 누르면 intent인 MyService.class 실행 종료(onDestroy 실행됨)
        btnStop.setOnClickListener(view -> {
            stopService(intent);
        });
    }


}