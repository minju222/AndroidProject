package edu.jaen.android.task1;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.jaen.android.task1.R;

public class SecondPage extends AppCompatActivity {
	private static final String TAG = "SecondPage_SCSA";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);

		findViewById(R.id.next2).setOnClickListener(v -> {
			Intent i = new Intent("Third3");

			startActivity(i);
		});

		findViewById(R.id.my2).setOnClickListener(v -> {
			Intent i = new Intent("Second2");
//			i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//stack의 top에 있을때만 재사용

			startActivity(i);
		});
		Log.d(TAG, "onCreate: ");
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onResume() {
		ActivityManager m = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.AppTask>  tasks = m.getAppTasks();

		for (ActivityManager.AppTask task : tasks) {
			ActivityManager.RecentTaskInfo info = task.getTaskInfo();
			int cnt = info.numActivities;
			int id = info.id;
			String cName = info.baseActivity.getShortClassName();
			String sName = info.topActivity.getShortClassName();
			Log.d(TAG, "baseActivity:" + cName + ", top:" + sName + ", numActivities:" + cnt + ", id:" + id);
		}
		super.onResume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(TAG, "onNewIntent: ");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy: ");
	}

}