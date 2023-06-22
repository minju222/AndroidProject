package edu.jaen.android.tag_writer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.jaen.android.tag_writer.databinding.ActivityWriteMainBinding;

public class WriteMainActivity extends AppCompatActivity {

	ActivityWriteMainBinding binding;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityWriteMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
	}

	//버튼 클릭시 입력된 데이터와 태그 타입을 Extras에 담아서 TagWrite전달...
	public void writeText(View view) {
		String data = binding.textEt.getText().toString();
		Intent intent = new Intent(this, TagWriteActivity.class);
		intent.putExtra("data", data);
		intent.putExtra("type", "T");
		startActivity(intent);


	}
	public void writeUrl(View view) {
		String data = binding.urlEt.getText().toString();
		Intent intent = new Intent(this, TagWriteActivity.class);
		intent.putExtra("data", data);
		intent.putExtra("type", "U");
		startActivity(intent);
	}
}
