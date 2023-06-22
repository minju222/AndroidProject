package kr.jaen.storage.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import kr.jaen.storage.R;

public class MainActivity extends AppCompatActivity {

    private static final int DEFAULT_VALUE = 0;
    private TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCount = findViewById(R.id.tv_count);

        // SharePreference로 넣으면 data 밑에 xml 파일로 저장됨
        // data 저장해주는 preference를 가져옴
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        // 처음 가져온 count는 DEFAULT_VALUE이므로 0
        int count = pref.getInt("count", DEFAULT_VALUE);

        SharedPreferences.Editor editor = pref.edit();
        //eitor에다가 count 증가해서 넣음(1로 저장됨) -> onCreate 될때마다 증가하면서 저장함
        editor.putInt("count", ++count);
        editor.apply();

        updateTvCount(count);


        findViewById(R.id.btn_add_value).setOnClickListener(v -> {



        });

        findViewById(R.id.btn_init_value).setOnClickListener(v -> {



        });
    }

    private void updateTvCount(int value) {
        tvCount.setText("SharedPreferences Test: " + value);
    }
}