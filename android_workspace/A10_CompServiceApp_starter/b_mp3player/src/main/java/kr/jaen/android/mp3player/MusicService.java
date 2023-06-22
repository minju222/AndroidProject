package kr.jaen.android.mp3player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService extends Service {

    private MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "음악 재생 시작!",
                Toast.LENGTH_SHORT).show();

        if (player != null) {
            player.stop();
            player.release();
        }

        player = MediaPlayer.create(this, R.raw.jazzbyrima);
        //음악이 끝나도 계속 노래 다시 튼다고 설정
        player.setLooping(true);
        //노래 실행
        player.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "음악 정지!",
                Toast.LENGTH_SHORT).show();

        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}