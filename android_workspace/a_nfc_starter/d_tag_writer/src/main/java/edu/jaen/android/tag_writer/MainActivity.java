package edu.jaen.android.tag_writer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.jaen.android.tag_writer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity_SCSA";

	private ActivityMainBinding binding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		binding.button0.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, WriteMainActivity.class)));
		binding.button1.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, WriteWithDialogActivity.class)));
	}
}