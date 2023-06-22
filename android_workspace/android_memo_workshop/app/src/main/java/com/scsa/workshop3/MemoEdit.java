package com.scsa.workshop3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.scsa.workshop3.databinding.ActivityMemoEditBinding;

public class MemoEdit extends AppCompatActivity {

    @Override
    // module레벨에 viewbinding 추가해야함
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 화면에 띄울 때 binding으로 불러오는 방식(객체 접근이 편함)
        ActivityMemoEditBinding binding = ActivityMemoEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //저장 버튼이 눌리면 동작하는 함수
        binding.button.setOnClickListener(view -> {
            //입력한 값들을 dto에 저장해서 intent로 MainActivity로 넘김
            MemoDto dto = new MemoDto(
                    binding.title.getText().toString(),
                    binding.content.getText().toString(),
                    binding.date.getText().toString());

            Intent intent = new Intent();
            intent.putExtra("result", dto);//intent에 result라는 이름으로 내용을 담은 dto를 담음

            setResult(RESULT_OK, intent);//결과(intent)를 mainActivity로 보냄
            finish(); //종료

        });






    }
}