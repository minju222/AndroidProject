package kr.jaen.storage.file;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import kr.jaen.storage.R;

public class FileOutputActivity extends AppCompatActivity {

    private static final String TAG = "FileOutputActivity_SCSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_output);

        TextView tvMessage = findViewById(R.id.tv_message);
        Button btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(view -> {
            Intent intent = new Intent(this, FileInputActivity.class);
            startActivity(intent);
        });

        // 내부 저장소에 저장
//        File file = new File(getFilesDir(), "data.txt");

        // 외부 저장소에 저장 (SD카드 유무 확인 필요)
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(getExternalFilesDir(null), "data.txt");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            Log.d(TAG, "파일 경로: " + file.getCanonicalPath());

            bw.append("안녕하세요.");
            bw.append("반갑습니다.");
            bw.flush();

            tvMessage.setText("파일이 정상적으로 생성되었습니다.");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "파일 출력 오류!");
            tvMessage.setText("파일 생성 시 오류 발생했습니다.");
        }
    }
}