package edu.jaen.android.intent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        String value = getIntent().getStringExtra("Key");

        TextView text = findViewById(R.id.textview);
        text.setText("전달받은 데이터 : " + value);

        findViewById(R.id.clickMe).setOnClickListener( v -> {
                Intent intent = new Intent();
                intent.putExtra("to_main", "잘 받았습니다.");

                // 결과전달
                setResult(Activity.RESULT_OK, intent);

                //현재 Activity 종료
                finish();
            }
        );
    }
}