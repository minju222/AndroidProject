package edu.jaen.android.intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity_SCSA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.detail_button).setOnClickListener( v -> {
            Intent intent = new Intent(this, DetailActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.explicit_button).setOnClickListener( v -> {
                Toast.makeText(this,"명시적 인텐트", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, NextActivity.class);
                intent.putExtra("Key", "MainActiviy에서 명시적 인텐트 전달");
                startActivity(intent);
            }
        );

        ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            String returnValue = result.getData().getStringExtra("to_main");

                            Log.d(TAG, "MainActivity로 돌아왔다. " + returnValue);
                            Toast.makeText(MainActivity.this, returnValue, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        findViewById(R.id.explicit2_button).setOnClickListener( v -> {
                Toast.makeText(this,"명시적 인텐트-결과받기", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, NextActivity.class);
                intent.putExtra("Key", "MainActiviy에서 명시적 인텐트 전달, 결과를 넘겨주세요.");
                //결과 돌려받기.
                startActivityResult.launch(intent);
            }
        );

        //암시적 인텐트 목적에 맞는 호출 : 지도보기, 연락처보기, 인터넷, SNS 공유 등등.
        TextView tv0 = findViewById(R.id.button0);
        tv0.setText("Browser 열기");
        tv0.setOnClickListener( v -> {
                //browser로 열기.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/"));
                startActivity(intent);
            }
        );

        TextView tv1 = findViewById(R.id.button1);
        tv1.setText("주소록 1번 보기");
        tv1.setOnClickListener( v -> {
                //ACTION_VIEW : 주소록 1번 보기
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/contacts/1"));
                startActivity(intent);
            }
        );

        TextView tv2 = findViewById(R.id.button2);
        tv2.setText("주소록 등록");
        tv2.setOnClickListener( v -> {
                //ACTION_EDIT : 주소록 등록
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_INSERT);
                intent.setData(Uri.parse("content://com.android.contacts/contacts"));
                startActivity(intent);
            }
        );

        TextView tv3 = findViewById(R.id.button3);
        tv3.setText("주소록 수정");
        tv3.setOnClickListener( v -> {
                //ACTION_EDIT : 주소록 수정
                Intent intent = new Intent(Intent.ACTION_EDIT, Uri.parse("content://com.android.contacts/contacts/1"));
                startActivity(intent);
            }
        );

        TextView tv4 = findViewById(R.id.button4);
        tv4.setText("특정번호 전화 연결");
        tv4.setOnClickListener( v -> {
                //ACTION_EDIT : 주소록 수정
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01011112222"));
                startActivity(intent);
            }
        );

        TextView tv5 = findViewById(R.id.button5);
        tv5.setText("문자 보내기");
        tv5.setOnClickListener( v -> {
                //문자 보내기
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:01099991111"));
                intent.putExtra("sms_body", "SMS text연습...");
                startActivity(intent);
            }
        );

        TextView tv6 = findViewById(R.id.button6);
        tv6.setText("GPS 좌표 열기");
        tv6.setOnClickListener( v -> {
                //ACTION_VIEW : 좌표열기
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:36.1092397,128.4167727"));
                startActivity(intent);
            }
        );

        TextView tv7 = findViewById(R.id.button7);
        tv7.setText("앱 연결 or 마켓이동.");
        tv7.setOnClickListener( v -> {
                String togo = "com.kakao.talk";
                Intent wantToGo = getPackageManager().getLaunchIntentForPackage(togo);
                if( wantToGo == null ){
                    // market이 없으면 ActivityNotFoundException
                    try{
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id="+togo));
                        startActivity(intent);
                    }catch (ActivityNotFoundException exception) {
                        Toast.makeText(this, "실행할 Activity가 없습니다", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    wantToGo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(wantToGo);
                }

            }
        );
    }
}