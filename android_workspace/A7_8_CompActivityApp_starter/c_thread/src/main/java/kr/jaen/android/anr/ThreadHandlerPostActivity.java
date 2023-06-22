package kr.jaen.android.anr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import kr.jaen.android.anr.databinding.ActivityThreadHandlerBinding;
import kr.jaen.android.anr.databinding.ActivityThreadHandlerPostBinding;

public class ThreadHandlerPostActivity extends AppCompatActivity {

    private static final String TAG = "ThreadHandlerPost_SCSA";
    private int mainCount = 0;
    private int threadCount = 0;
    private boolean isRunning = true;


    private ActivityThreadHandlerPostBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityThreadHandlerPostBinding.inflate(getLayoutInflater());
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


    // handlerMessage 메서드를 구현할 필요가 없고 단지 Handler 객체만 생성함
    private Handler handler = new Handler(Looper.getMainLooper());

    private class CountThread extends Thread {

        @Override
        public void run() {
            while (isRunning) {
                threadCount++;
                Log.d(TAG, "threadCount: " + threadCount);

                // Main 스레드가 아닌 스레드가 Activity 자원에 접근이 불가능함
                // CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
                //binding.tvThread.setText("threadCount=" + threadCount);

                // Runnable 객체를 Handler에게 전달
                handler.post(new Runnable() {
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