package edu.jaen.android.tag_recognition;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.jaen.android.tag_recognition.databinding.ActivityMainBinding;

/**
 * activity의 default launchMode : standard이므로 onCreate가 계속 호출된다.
 * 호출시마다 Activity를 재생성
 */
public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity_SCSA";

	private ActivityMainBinding binding;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		String action = getIntent().getAction();
		binding.textview.setText(action);

		Toast.makeText(this, "onCreate"+ action, Toast.LENGTH_SHORT).show();
		Log.d(TAG, "onCreate: "+action);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
	}
}
