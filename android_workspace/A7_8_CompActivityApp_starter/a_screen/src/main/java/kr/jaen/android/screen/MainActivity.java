package kr.jaen.android.screen;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_SCSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        Log.d(TAG, "onCreate: "+i.getAction());

        // 1.
        Button btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NextActivity.class);
            startActivity(intent);
        });

        // 2.
        Button btnNextForResult = findViewById(R.id.btn_next_for_result);
        btnNextForResult.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NextActivity.class);
            intent.putExtra("name", "홍길동");
            // 2-1.
//            startActivityForResult(intent, 1);//onActivityResult와 짝꿍
            // 2-2.
            startActivityLauncher.launch(intent);
        });
    }

//    @Override
    //startActivityForResult와 짝꿍인 코드
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode ==1 ){
//            if (resultCode == Activity.RESULT_OK){
//                String msg = data.getStringExtra("msg");
//                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }

    // 2-2. startActivityForResult와 다른 방식
    private final ActivityResultContracts.StartActivityForResult contract =
            new ActivityResultContracts.StartActivityForResult();

    private final ActivityResultLauncher<Intent> startActivityLauncher =
            registerForActivityResult(contract, result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String msg = data.getStringExtra("msg");
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
            });
}