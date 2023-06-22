package kr.jaen.storage.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import kr.jaen.storage.databinding.ActivityMainSqliteBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_SCSA";

    private ActivityMainSqliteBinding binding;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainSqliteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //mydb로 데이터베이스 만들어짐
        dbHelper = new DBHelper(this, "mydb.db", null, 1);
        //db 오픈
        dbHelper.open();

        refresh();

        binding.btnInsert.setOnClickListener(view -> {
            String message = String.valueOf(binding.etMessage.getText());
            dbHelper.insert(message);
            refresh();

            Toast.makeText(this, "추가되었습니다.",
                    Toast.LENGTH_SHORT).show();
        });

        binding.btnSelect.setOnClickListener(view -> {
            String id = String.valueOf(binding.etId.getText());
            String result = dbHelper.select(id);
            binding.tvResult.setText(result);
        });

        binding.btnUpdate.setOnClickListener(view -> {
            String id = String.valueOf(binding.etId.getText());
            String message = String.valueOf(binding.etMessage.getText());
            //id와 메세지가 있을 때만 수정
            if(!id.isEmpty() && !message.isEmpty()){
                dbHelper.update(id, message);
                refresh();
                Toast.makeText(this, "수정되었습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnDelete.setOnClickListener(view -> {
            String id = String.valueOf(binding.etId.getText());
            //id가 있는 경우에만 삭제
            if(!id.isEmpty()){
                dbHelper.delete(id);
                //목록 갱신
                refresh();

                Toast.makeText(this, "삭제되었습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //목록을 갱신할때마다 부르는 function
    private void refresh() {
        String result = dbHelper.selectAll();
        binding.tvResult.setText(result);
    }
}