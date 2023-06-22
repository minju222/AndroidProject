package com.scsa.andr.memo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.scsa.andr.memo.databinding.ActivityMainBinding;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MemoDTO> memos;
    private MemoAdapter adapter;
    private static final int REQUEST_CODE_ADD_MEMO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.scsa.andr.memo.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        memos = new ArrayList<>();
        adapter = new MemoAdapter(this, memos);
        binding.listView.setAdapter(adapter);

        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("memo", memos.get(position));
            startActivity(intent);
        });

        binding.buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_MEMO);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_MEMO && resultCode == RESULT_OK) {
            if (data != null) {
                MemoDTO memo = (MemoDTO) data.getSerializableExtra("memo");
                memos.add(memo);
                adapter.notifyDataSetChanged();
            }
        }
    }
}







