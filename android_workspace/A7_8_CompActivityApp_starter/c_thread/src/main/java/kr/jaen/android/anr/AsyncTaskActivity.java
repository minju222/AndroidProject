package kr.jaen.android.anr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import kr.jaen.android.anr.databinding.ActivityAsyncTaskBinding;

public class AsyncTaskActivity extends AppCompatActivity {

    private static final String TAG = "AsyncTaskActivity_SCSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAsyncTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MyAsyncTask asyncTask = new MyAsyncTask();
        asyncTask.execute();
    }

    private ActivityAsyncTaskBinding binding;

    /**
     * 첫 번째 param: doInBackground의 parameter
     * 두 번째 param: onProgressUpdate의 parameter
     * 세 번째 param: doInBackground의 return type
     *               == onPostExecute의 parameter
     *               == onCancelled의 parameter
     */
    private class MyAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        // 실행 준비
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute()");
        }

        // 백그라운드 실행 (일반 스레드)
        @Override
        protected Integer doInBackground(Integer... args) {

            for (int i = 0; i < 100; i++) {
                SystemClock.sleep(100);
                publishProgress(i);
            }

            return 0;
        }

        // 진행 상황 호출 시 실행
        @Override
        protected void onProgressUpdate(Integer... progress) {
            binding.textView.setText(progress[0].toString());
        }

        // 실행 후 결과 처리
        @Override
        protected void onPostExecute(Integer result) {
            Log.d(TAG, "onPostExecute()::" + result);
        }

        // 취소
        @Override
        protected void onCancelled(Integer result) {
            Log.d(TAG, "onCancelled()::" + result);
        }
    }


}