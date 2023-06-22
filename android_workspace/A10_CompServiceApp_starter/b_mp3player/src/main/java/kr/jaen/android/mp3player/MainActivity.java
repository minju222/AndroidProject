package kr.jaen.android.mp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import kr.jaen.android.mp3player.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //manifest 파일의 service android 부분 true로 변경해야함
//        Intent intent = new Intent(this, MusicService.class);
//        Intent intent = new Intent(this, MusicForegroundService.class);
//        intent.setClass(this, MusicService.class);

        //위와 같음
        ComponentName name = new ComponentName(
                "kr.jaen.android.mp3player",
                "kr.jaen.android.mp3player.MusicForegroundService"
        );
        Intent intent = new Intent();
        intent.setComponent(name);

        binding.btnStartMusic.setOnClickListener( v -> {
            startService(intent);
        });

        binding.btnStopMusic.setOnClickListener( v -> {
            stopService(intent);
        });
    }
}