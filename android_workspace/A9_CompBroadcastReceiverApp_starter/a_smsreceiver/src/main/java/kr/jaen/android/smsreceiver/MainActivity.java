package kr.jaen.android.smsreceiver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_SCSA";
    private static final int REQUEST_PERMISSIONS = 100;

    private SMSReceiver receiver;

    private final String [] REQUIRED_PERMISSIONS = new String []{
            "android.permission.RECEIVE_SMS"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. 권한이 있는지 확인.
        int permission = ContextCompat.checkSelfPermission(this,REQUIRED_PERMISSIONS[0]);

        // 2. 권한이 없으면 런타임 퍼미션 창 띄우기. 있으면 정상진행.
        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_PERMISSIONS);
        }

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    boolean hasPermission = true;

    // requestPermissions의 call back method(창이 떠야할 때 진행하는 코드)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSIONS){
            //사용자가 권한 허용 누른 경우와 허용 누르지 않은 경우
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else{
                showDialog();
            }
        }

    }

    //권한 사용 동의를 안했을 때 dialog 띄우는 코드
    private void showDialog(){
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("권한확인")
                .setMessage("서비스를 정상적으로 이용하려면, 권한이 필요합니다. 설정화면으로 이동합니다.")
                .setPositiveButton("예", (dialogInterface, i) -> {
                    //권한설정화면으로 이동.
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton("아니오", (dialogInterface, which) -> {
                    Toast.makeText(MainActivity.this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                })
                .create();
        dialog.show();
    }


    // 2. BroadcastReceiver를 registerReceiver()를 이용하여 등록 후 사용하기
    private void regist() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");

        receiver = new SMSReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    //Resume에서 reciver를 받고
    protected void onResume() {
        super.onResume();
        regist();
    }

    @Override
    //Pause되면 receiver 버림(app이 백그라운드로 내려가면 문자 수신 안함)
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}