package kr.jaen.android.anr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import kr.jaen.android.anr.databinding.ActivityThreadBinding;
import kr.jaen.android.anr.databinding.ActivityThreadHandlerBinding;

public class ThreadHandlerActivity extends AppCompatActivity {

    private static final String TAG = "ThreadHandlerAct_SCSA";
    private int mainCount = 0;
    private int threadCount = 0;
    private boolean isRunning = true;


    private ActivityThreadHandlerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityThreadHandlerBinding.inflate(getLayoutInflater());
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



    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    binding.tvThread.setText("threadCount=" + threadCount);
                    break;

                case 1:
                    binding.tvThread.setText("threadCount=" + msg.obj);
                    break;
            }
        }
    };

    private class CountThread extends Thread {

        @Override
        public void run() {
            while (isRunning) {
                threadCount++;
                Log.d(TAG, "threadCount: " + threadCount);

                // Main 스레드가 아닌 스레드가 Activity 자원에 접근이 불가능함
                // CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
                //binding.tvThread.setText("threadCount=" + threadCount);

                // 1.
                handler.sendEmptyMessage(0);

                // 2.
//                Message msg = handler.obtainMessage(1, new Integer(threadCount));
//                handler.sendMessage(msg);

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