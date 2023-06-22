package kr.jaen.android.anr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import kr.jaen.android.anr.databinding.ActivityRunOnUiThreadBinding;
import kr.jaen.android.anr.databinding.ActivityThreadHandlerPostBinding;

public class RunOnUiThreadActivity extends AppCompatActivity {
    private static final String TAG = "RunOnUiThreadAct_SCSA";
    private int mainCount = 0;
    private int threadCount = 0;
    private boolean isRunning = true;


    private ActivityRunOnUiThreadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRunOnUiThreadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnIncrease.setOnClickListener(view -> {
            mainCount++;
            binding.tvMain.setText("mainCount=" + mainCount);
            binding.tvThread.setText("threadCount=" + threadCount);
        });

        // Thread 객체 생성
        CountThread countThread = new CountThread();
        countThread.start();
    }


    private class CountThread extends Thread {

        @Override
        public void run() {
            while (isRunning) {
                threadCount++;
                Log.d(TAG, "threadCount: " + threadCount);

                // Main 스레드가 아닌 스레드가 Activity 자원에 접근이 불가능함
                // CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
                //binding.tvThread.setText("threadCount=" + threadCount);

                // MainThread에서 실행
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.tvThread.setText("threadCount=" + threadCount);
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onPause() {
        isRunning = false;
        super.onPause();
    }
}