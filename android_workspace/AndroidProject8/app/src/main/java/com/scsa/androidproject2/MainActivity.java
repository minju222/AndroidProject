package com.scsa.androidproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity_SCSA";
    private NfcAdapter nAdapter;

    PendingIntent pIntent;

    IntentFilter[] filters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nAdapter = NfcAdapter.getDefaultAdapter(this);

        Intent i = new Intent(this, WeatherMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_MUTABLE);
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

        try {
            filter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        filters = new IntentFilter[]{filter,};

        Button alarmButton = findViewById(R.id.todoBtn);
        Button mouseCatchButton = findViewById(R.id.newsBtn);
        Button xmlParserButton = findViewById(R.id.mouseBtn);
        Button weatherButton = findViewById(R.id.weatherBtn);
        Button returnButton = findViewById(R.id.returnBtn);


        alarmButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteListActivity.class);
            startActivity(intent);
        });

        mouseCatchButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewsMainActivity.class);
            startActivity(intent);
        });

        xmlParserButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MouseMainActivity.class);
            startActivity(intent);
        });

        weatherButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WeatherMainActivity.class);
            startActivity(intent);
        });

        returnButton.setOnClickListener(v->{
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setCancelable(false);
            dialog.setMessage("앱을 종료하시겠습니까?");
            dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // NFC 태그 감지 시 인텐트 필터 설정
        Intent intent = getIntent();
        String action = intent.getAction();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            // NFC 태그가 감지되었을 때 실행할 코드 작성
            // 이동할 액티비티를 시작하는 인텐트를 생성하고 startActivity() 메서드를 호출합니다.

            Intent weatherIntent = new Intent(this, WeatherMainActivity.class);
            startActivity(weatherIntent);

            finish();
        }
            // MainActivity를 종료하여 뒤로 가기 버튼을 눌렀을 때 바로 WeatherMainActivity로 돌아가지 않도록 합니다.
        nAdapter.enableForegroundDispatch(this, pIntent, filters, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 포그라운드 디스패치 비활성화
        nAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            parseData(intent);
        }
    }

    private void parseData(Intent intent) {
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (data != null) {
            NdefMessage ndefM = (NdefMessage) data[0];
            NdefRecord ndefR = ndefM.getRecords()[0];

            byte[] byteArr = ndefR.getPayload();

            Log.d(TAG, "parseData: ");

            Intent launchIntent = new Intent(MainActivity.this, WeatherMainActivity.class);
            startActivity(launchIntent);
        }
    }

}