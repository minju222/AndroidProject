package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Button button2main = findViewById(R.id.button2main);
//        //main으로 돌아갈때 main-next-main으로 쌓인상태로 돌아감
//        button2main.setOnClickListener(v->{
//            Intent intent= new Intent(NextActivity.this, MainActivity.class);//버튼을 클릭하면 Next Act에서 MainAct로 이동하겠다는 의미
//            startActivity(intent);//실행
//        });

        //main으로 돌아갈때 main-next에서 next를 종료해서 main이 뜸
        button2main.setOnClickListener(v->{
            finish();//종료
        });






    }

}