package kr.jaen.android.lifecycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
// 처음 실행할 때 onCreate > onStart > onResume(포커스)
// 앱 켰다가 홈화면으로 가면 onPause > onStop
// 다시 목록에서 선택하면 onRestart > onStart > onResume
// Main -> Next -> 뒤로가기 키를 누르면 -> Main은 onRestart, onStart, onResume / Next는 onStop onDestroy까지 불림(다시 불릴 일이 없어서)
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_SCSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            String msg = savedInstanceState.getString("test");
            Toast.makeText(this, "onCreate::" + msg, Toast.LENGTH_SHORT).show();
        }

        Button btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NextActivity.class);
            startActivity(intent);
        });

        Log.d(TAG, "onCreate()");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

      //비정상적으로 종료되면..
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        Log.d(TAG, "onSaveInstanceState()");
//        super.onSaveInstanceState(outState);
//
//        outState.putString("test", "현재 상태를 저장");
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        Log.d(TAG, "onRestoreInstanceState()");
//        super.onRestoreInstanceState(savedInstanceState);
//
//        if (savedInstanceState != null) {
//            String msg = savedInstanceState.getString("test");
//            Toast.makeText(this, "onRestore::" + msg, Toast.LENGTH_SHORT).show();
//        }
//    }
}