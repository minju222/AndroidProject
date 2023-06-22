package com.scsa.androidproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.Random;

public class MouseMainActivity extends AppCompatActivity {
    private static final String TAG = "MouseMainActivity_SCSA";

    FrameLayout frameLayout;
    FrameLayout.LayoutParams params;
    int count = 0;   //점수 변수

    int catch_count = 0;

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
    int howManyJewel = 5;  //startLevel 5마리. 레벨마다 증가

    TextView scoreTextView;

    ImageButton returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse_main);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        params = new FrameLayout.LayoutParams(1, 1);

        scoreTextView = new TextView(this);
        scoreTextView.setText("Level " + level + "  Score: 0 point");
        scoreTextView.setTextColor(Color.WHITE);
        scoreTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
        returnButton = new ImageButton(this);
        returnButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_cancel_24));


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

        //디스플레이 크기 체크
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        myWidth = metrics.widthPixels;
        myHeight = metrics.heightPixels;
        Log.d(TAG, "My Window " + myWidth + " : " + myHeight);

        if (level == 1) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MouseMainActivity.this);
            dialog.setCancelable(false);
            dialog.setView(R.layout.dialog_layout);
            dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    init(howManyJewel);
                }
            });
            dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mouseTask = new MouseTask();
                    mouseTask.cancel(true);
                    finish();
                }
            });
            dialog.show();
        }else{
            init(howManyJewel);
        }

        returnButton.setOnClickListener(v -> {
            AlertDialog.Builder dialog2 = new AlertDialog.Builder(MouseMainActivity.this);
            dialog2.setCancelable(false);
            dialog2.setMessage("게임을 그만하시겠습니까?");
            dialog2.setPositiveButton("네", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog2.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog2.show();
        });
    }

    public void init(int nums) {
        // 초기화
        count = 0;
        catch_count = 0;
        threadEndFlag = true;
        this.howManyJewel = nums;
        gameSpeed = (int) (gameSpeed * (10 - level) / 9.);

        frameLayout.removeAllViews();

        // 이미지 담을 배열 생성과 이미지 담기
        imageViews = new ImageView[nums];
        for (int i = 0; i < nums; i++) {
            ImageView iv = new ImageView(this);
            if (i < nums/4) {
                iv.setImageResource(R.drawable.diamond);
            } else if (i < nums/4*2){
                iv.setImageResource(R.drawable.bomb);
            } else if (i < nums/4*3){
                iv.setImageResource(R.drawable.steal);
            }else{
                iv.setImageResource(R.drawable.gold);
            }
            frameLayout.addView(iv, params); // 화면에 표시
            imageViews[i] = iv; // 배열에 담기
            iv.setOnClickListener(h); // 이벤트 등록
        }

        frameLayout.addView(scoreTextView);
        FrameLayout.LayoutParams textParams = (FrameLayout.LayoutParams) scoreTextView.getLayoutParams();
        textParams.topMargin = getResources().getDimensionPixelSize(R.dimen.button_margin);
        returnButton.setLayoutParams(textParams);

        frameLayout.addView(returnButton);
        returnButton.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        // 버튼 위치 수정
        FrameLayout.LayoutParams buttonParams = (FrameLayout.LayoutParams) returnButton.getLayoutParams();
        buttonParams.gravity = Gravity.TOP | Gravity.END;
        buttonParams.topMargin = getResources().getDimensionPixelSize(R.dimen.button_margin);
        returnButton.setLayoutParams(buttonParams);


        float playbackSpeed = 1.0f + (level * 0.05f); // 레벨이 증가할수록 음악 재생 속도 0.1씩 속도 증가
        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(playbackSpeed));
        mouseTask = new MouseTask(); // 일정 간격으로 이미지 위치를 바꿀 쓰레드 실행
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
        public void onClick(View v) {
            ImageView iv = (ImageView) v;
            Drawable drawable = iv.getDrawable();

            if (drawable != null) {
                if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.bomb).getConstantState())) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MouseMainActivity.this);
                    dialog.setCancelable(false);
                    dialog.setMessage(count+"점 실패😢\n현재 레벨을 다시 진행하겠습니까?");
                    dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            scoreTextView.setText("Level " + level + "  Score: 0 point");
                            init(howManyJewel);
                        }
                    });
                    dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    dialog.show();
                } else if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.steal).getConstantState())) {
                    count -= 10;
                    soundPool.play(killSound, 1, 1, 0, 0, 1);
                    iv.setVisibility(View.INVISIBLE);

                    scoreTextView.setText("Level " + level + "  Score: " + count + " point");
                    scoreTextView.setTextColor(Color.WHITE);
                    scoreTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);

                } else{
                    if(drawable.getConstantState().equals(getResources().getDrawable(R.drawable.diamond).getConstantState())){
                        count += 20; //다이아 20점
                    }else{
                        count += 10; //골드바 10점
                    }
                    soundPool.play(killSound, 1, 1, 0, 0, 1);
                    iv.setVisibility(View.INVISIBLE);
                    scoreTextView.setText("Level " + level + "  Score: " + count + " point");
                    scoreTextView.setTextColor(Color.WHITE);
                    scoreTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);

                    // 보석이 다 없어졌는지 확인
                    boolean jewelExists = true;
                    for (ImageView img : imageViews) {
                        Drawable imgDrawable = img.getDrawable();
                        if (imgDrawable != null &&
                                imgDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.diamond).getConstantState())) {
                            if (img.getVisibility()==View.VISIBLE){
                                jewelExists = false;
                                break;
                            }
                        } else if (imgDrawable != null &&
                                imgDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.gold).getConstantState())) {
                            if (img.getVisibility()==View.VISIBLE){
                                jewelExists = false;
                                break;
                            }
                        }
                    }
                    if (jewelExists) {
                        threadEndFlag = false;
                        mouseTask.cancel(true);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(MouseMainActivity.this);
                        dialog.setCancelable(false);
                        dialog.setMessage(count+"점으로 성공🎉\n계속하시겠습니까?");
                        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                level++;
                                scoreTextView.setText("Level " + level + "  Score: 0 point");
                                init(++howManyJewel);
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
            }
        }
    };

    // 위치 이동하여 다시 그리기
    public void update() {
        if (!threadEndFlag) return;
        Log.d(TAG, "update:");
        for (ImageView img : imageViews) {
            x = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) +
                    random.nextInt(myWidth - 2 * imgWidth);
            y = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin) +
                    random.nextInt(myHeight - 2 * imgHeight);

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
                    Thread.sleep(gameSpeed);//level 상승 -> gameSpeed값 감소 -> sleep timer감소 ->게임속도 빨라짐
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