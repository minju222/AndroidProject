package kr.jaen.android.settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kr.jaen.android.settings.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 화면 밝기 값 가져오기
        refresh();

        // Display 설정으로 이동
        binding.btnDisplaySettings.setOnClickListener(view -> {
            Intent intent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
            startActivity(intent);
        });

        // 앱에서 화면 밝기 값 변경하기
        binding.btnApply.setOnClickListener(view -> {

            // 값 변경하기
            Settings.System.putInt(
                    getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS,
                    Integer.parseInt(String.valueOf(binding.etInputValue.getText()))
            );

            refresh();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
    }

    private void refresh() {
        // 화면 밝기 값 가져오기
        String brightness = Settings.System.getString(
                getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS);

        binding.tvResult.setText("현재 화면 밝기: " + brightness);
        binding.etInputValue.setText(brightness);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        if (Settings.System.canWrite(this)) {
            Toast.makeText(this, "권한 획득된 상태",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "설정 화면에서 권한 설정 바랍니다.",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }

        refresh();
    }
}