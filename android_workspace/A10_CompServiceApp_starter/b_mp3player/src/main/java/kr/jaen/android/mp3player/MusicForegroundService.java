package kr.jaen.android.mp3player;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MusicForegroundService extends Service {

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
        startForegroundService();
        player = MediaPlayer.create(this, R.raw.song);
        player.setLooping(true);
        player.start();
        return super.onStartCommand(intent, flags, startId);
    }

    //ForegroundService 여야 백그라운드로 내려가도 프로그램이 꺼지지 않음(==음악이 계속 재생됨)
    private void startForegroundService() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "notification_channel";
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_LOW  //소리없이 알림만.
            );
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setSmallIcon(R.drawable.ic_outline_play_arrow_24)
                .setContentTitle("MyMusic Foreground")
                .setContentText("music play.....")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        startForeground(101, builder.build()); // noti영역 보이고 foreground서비스 시작.
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