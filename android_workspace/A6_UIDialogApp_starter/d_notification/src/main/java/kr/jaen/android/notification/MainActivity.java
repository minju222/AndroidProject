package kr.jaen.android.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

/*
예제 실행 전 꼭 알림 권한 허용 해줘야 함
(앱 아이콘 길게 터치, App info > Permission에서 설정가능)
 */
public class MainActivity extends AppCompatActivity {

    private static final int NOTI_ID = 100;

    private NotificationManager nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Button btnFire = findViewById(R.id.btn_fire);
        btnFire.setOnClickListener(view -> {
            showNotification();
        });
    }

    @Override
    protected void onDestroy() {
        nm.cancel(NOTI_ID);
        super.onDestroy();
    }

    private void showNotification() {

        String channelId = getPackageName() + "-" + getClass().getName();

        // Oreo 부터는 Notification Channel을 만들어야 함
        // 26: Build.VERSION_CODES.O
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    channelId,
                    "Simple Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            nm.createNotificationChannel(serviceChannel);
        }

        String [] REQUIRED_PERMISSIONS = new String []{
                "android.permission.POST_NOTIFICATIONS"
        };
        int REQUEST_CODE_PERMISSIONS = 100;

        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.drawable.priority_high);
        builder.setTicker(null);
        builder.setContentTitle("알림");
        builder.setContentText("알림이 발생했습니다.");
        builder.setAutoCancel(false);//알람을 클릭하면 지워주는 역할
        builder.setContentIntent(PendingIntent.getActivity(this, 0, //pending intent는 누르면 이동할수도 있고, 안누를수도 있는 경우에 주는 인텐트
                new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE));

        // Android 13 (SDK 33) 부터는
        // AndroidManifest.xml에 아래 권한 추가 필요
        // android.permission.POST_NOTIFICATIONS
        nm.notify(NOTI_ID, builder.build());
    }
}