package com.scsa.workshop3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.scsa.workshop3.databinding.ActivityMemoEditBinding;
import com.scsa.workshop3.databinding.ActivityMemoInfoBinding;

import java.util.ArrayList;

public class MemoInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //화면에 띄울 때 binding대신 객체 생성해서 id에 접근하는 방식
        setContentView(R.layout.activity_memo_info);

        Intent intent = getIntent();
        //position이란 이름으로 넘어온 intent positon 받음
        int position = intent.getIntExtra("position", -1);
        MemoDto dto = (MemoDto) intent.getSerializableExtra("item");

        //EditText에서 title, content, date 부분에 setText로 dto로 가져온 내용을 넣음
        EditText titleEditText = findViewById(R.id.title);
        EditText contentEditText = findViewById(R.id.content);
        EditText dateEditText = findViewById(R.id.date);

        titleEditText.setText(dto.getTitle());
        contentEditText.setText(dto.getContents());
        dateEditText.setText(dto.getDate());

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            MemoDto updatedto = new MemoDto(
                    titleEditText.getText().toString(),
                    contentEditText.getText().toString(),
                    dateEditText.getText().toString());
            // 새 intent 생성해서 result라는 이름으로 updatedto를 MainActivity로 넘기기 위해 intent담음, position도 같이 담아줌
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", updatedto);
            resultIntent.putExtra("position", position);

            // Set the result as OK and pass the intent back to MainActivity
            setResult(Activity.RESULT_OK, resultIntent);
            finish();

        });


//          //binding방식을 사용하는 방법
//        ActivityMemoInfoBinding binding = ActivityMemoInfoBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        Intent fromIntent = getIntent();
//        int position = fromIntent.getIntExtra("position", -1);
//        MemoDto memoDto = (MemoDto) fromIntent.getSerializableExtra("item");
//
//        binding.title.setText(memoDto.getTitle());
//        binding.content.setText(memoDto.getContents());
//        binding.date.setText(memoDto.getDate());
//
//        binding.button.setOnClickListener(view -> {
//            MemoDto dto = new MemoDto(
//                    binding.title.getText().toString(),
//                    binding.content.getText().toString(),
//                    binding.date.getText().toString());
//
//            Intent intent = new Intent();
//            intent.putExtra("position", position);
//            intent.putExtra("result", dto);
//
//            setResult(RESULT_OK, intent);
//            finish();
//        });

    }
}