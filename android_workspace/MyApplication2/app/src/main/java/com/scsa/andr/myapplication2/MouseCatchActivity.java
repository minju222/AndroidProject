package com.scsa.andr.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MouseCatchActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity_SCSA";

    FrameLayout frameLayout;
    FrameLayout.LayoutParams params;
    int count = 0;   //잡은 쥐 개수를 저장할 변수
    int gameSpeed = 1000;  // 게임 속도 조절
    static boolean threadEndFlag = true; // 쓰레드 끄기
    MouseTask mouseTask;                // 쓰레드 구현

    int myWidth;  // 내 폰의 너비
    int myHeight; // 내 폰의 높이
    int imgWidth = 150;  //그림 크기
    int imgHeight = 150;//그림 크기
    Random random = new Random();  // 이미지 위치를 랜덤하게 발생시킬 객체

    SoundPool soundPool;   // 소리
    int killSound;    // 소리
    MediaPlayer mediaPlayer;   // 소리

    int x = 200;        //시작위치
    int y = 200;        //시작위치
    ImageView[] imageViews; // 이미지들을 담아 놓을 배열

    int level = 1;      // 게임 레벨
    int howManyMouse = 5;  //startLevel 5마리. 레벨마다 증가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse_catch);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        params = new FrameLayout.LayoutParams(1, 1);

        //디스플레이 크기 체크
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        myWidth = metrics.widthPixels;
        myHeight = metrics.heightPixels;
        Log.d(TAG, "My Window " + myWidth + " : " + myHeight);

        //사운드 셋팅
//        pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(attributes)
                .build();
        killSound = soundPool.load(this, R.raw.mouse_scream, 1);
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setLooping(true);

        init(howManyMouse);

    }

    public void init(int nums) {
        //초기화
        count = 0;
        threadEndFlag = true;
        this.howManyMouse = nums;
        gameSpeed = (int) (gameSpeed * (10 - level) / 10.);

        frameLayout.removeAllViews();

        //이미지 담을 배열 생성과 이미지 담기
        imageViews = new ImageView[nums];
        for (int i = 0; i < nums; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(R.drawable.running_mouse_trans);  // 이미지 소스 설정
            frameLayout.addView(iv, params);  // 화면에 표시
            imageViews[i] = iv;     // 배열에 담기
            iv.setOnClickListener(h);  // 이벤트 등록
        }

        mouseTask = new MouseTask();  //일정 간격으로 이미지 위치를 바꿀 쓰레드 실행
        mouseTask.execute();
    }

    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    protected void onPause() {
        super.onPause();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mouseTask.cancel(true);
        threadEndFlag = false;
    }

    View.OnClickListener h = new View.OnClickListener() {
        public void onClick(View v) {   // 쥐를 잡았을 때
            count++;
            ImageView iv = (ImageView) v;
            soundPool.play(killSound, 1, 1, 0, 0, 1);  // 소리 내기
            iv.setVisibility(View.INVISIBLE);          // 이미지(쥐) 제거

            Toast.makeText(MouseCatchActivity.this, "Die...." + count, Toast.LENGTH_SHORT).show();
            if (count == howManyMouse) {   // 쥐를 다 잡았을때
                threadEndFlag = false;
                mouseTask.cancel(true);

                AlertDialog.Builder dialog = new AlertDialog.Builder(MouseCatchActivity.this);
                dialog.setMessage("계속하시겠습니까?");
                dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        level++;
                        init(++howManyMouse);
                    }
                });
                dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.show();
            }
        }
    };

    // 쥐 위치 이동하여 다시 그리기
    public void update() {
        if (!threadEndFlag) return;
        Log.d(TAG, "update:");
        for (ImageView img : imageViews) {
            x = random.nextInt(myWidth - imgWidth);
            y = random.nextInt(myHeight - imgHeight);

            img.layout(x, y, x + imgWidth, y + imgHeight);
            img.invalidate();
        }

    }

    // 일정 시간 간격으로 쥐를 다시 그리도록 update()를 호출하는 쓰레드
    class MouseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {// 다른 쓰레드
            while (threadEndFlag) {
                //다른 쓰레드에서는 UI를 접근할 수 없으므로
                publishProgress();    //자동으로 onProgressUpdate() 가 호출된다.
                try {
                    Thread.sleep(gameSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            update();
        }
    }

    ;//end MouseTask
}// end MainActivity