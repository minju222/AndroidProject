package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_SCSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "created..", Toast.LENGTH_SHORT).show(); //현위치 this == MainActivity

        Log.d(TAG, "onCreate: "); //"MainActivity_scsa"로 TAG 잡힘
        Log.e(TAG, "onCreate: ");
        Log.i(TAG, "onCreate: ");
        Log.w(TAG, "onCreate: ");

        Button button = findViewById(R.id.hello_button);//java에서 xml 지칭할때는 R로 부름
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: "); //클릭 할떄마다 이 function이 불림
//                Toast.makeText(MainActivity.this, "CLICKED..", Toast.LENGTH_SHORT).show(); //Context는 toast를 띄우는 위치, text가 띄울 메세지/ 여기서 this는 MainActivity가 아님-> new로 만듬 onClickListener가  this임
//                Snackbar.make(v, "clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });

        //위랑 같은 건데 표현 방식이 다른 버전
        button.setOnClickListener(v -> {
                Log.d(TAG, "onClick: "); //클릭 할떄마다 이 function이 불림
                Toast.makeText(MainActivity.this, "CLICKED..", Toast.LENGTH_SHORT).show(); //Context는 toast를 띄우는 위치, text가 띄울 메세지/ 여기서 this는 MainActivity가 아님-> new로 만듬 onClickListener가  this임
                Snackbar.make(v, "clicked", Snackbar.LENGTH_SHORT).show();

                Intent intent= new Intent(MainActivity.this, NextActivity.class);//버튼을 클릭하면 MainAct에서 Next Act로 이동하겠다는 의미
                startActivity(intent);//intent실행
        });


    }
}






