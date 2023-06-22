package edu.jaen.android.tag_writer;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.jaen.android.tag_writer.databinding.ActivityTagWriteBinding;

public class TagWriteActivity extends AppCompatActivity {
	private static final String TAG = "TagWriteActivity_SCSA";

	private NfcAdapter nfcAdapter;
	private PendingIntent pIntent;
	private IntentFilter[] filters;

	private String tagType;
	private String tagData;

	ActivityTagWriteBinding binding;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityTagWriteBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			finish();
		}

		//넘어온 데이터를 변수에 저장한다.
		tagType = getIntent().getStringExtra("type").toString();
		tagData = getIntent().getStringExtra("data").toString();

		Toast.makeText(this, "type : " + tagType + ", data : " + tagData, Toast.LENGTH_SHORT).show();

		//태그 정보가 포함된 인텐트를 처리할 액티비티 지정
		Intent intent = new Intent(this, TagWriteActivity.class);
		//SingleTop설정
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

		IntentFilter tagFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		filters = new IntentFilter[]{tagFilter,};
	}

	@Override
	public void onResume() {
		super.onResume();
		nfcAdapter.enableForegroundDispatch(this, pIntent, filters, null);
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//태그에 데이터를 write 작업을 수행해야 한다..
		String action = intent.getAction();
		if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED) ||
				action.equals(NfcAdapter.ACTION_TECH_DISCOVERED) ||
				action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
			//Log.d(TAG, "ACTION_NDEF_DISCOVERED...")

			//1. 태그 detect.... Tag 객체
			Tag detectTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			//writeTag 함수 호출
			writeTag(makeNdefMsg(tagType, tagData), detectTag);

		}
	}

	@Override
	public void onPause() {
		super.onPause();
		nfcAdapter.disableForegroundDispatch(this);
	}

	//T, "SCSA"  / U, "https://www.naver.com" /  AAR
	private NdefMessage makeNdefMsg(String type, String data) {
		NdefRecord ndefR = null;
		NdefRecord ndefR1 = null;

		if (type.equals("T")) {
			//TextRecord
			ndefR = NdefRecord.createTextRecord("en", data);

		} else if (type.equals("U")) {
			ndefR = NdefRecord.createUri(data);

		} else {
			Log.d(TAG, "makeNdefMsg: 알지 못하는 type.");
			//모르는 형태....
		}

		return new NdefMessage(new NdefRecord[]{ndefR, });

	}

	//NFC tag 에 데이터를 write 코드...
	private void writeTag(NdefMessage msg, Tag tag) {
		//Ndef 객체를 얻는다 : Ndef.get(tag)
		Ndef ndef = Ndef.get(tag);
		int msgSize = msg.toByteArray().length;
		try {
			if (ndef != null) {

				//ndef 객체를 이용해서 connect
				ndef.connect();
				//tag가 write모드를 지원하는지 여부 체크
				if (!ndef.isWritable()) {
					Toast.makeText(this, "Write를 지원하지 않습니다..", Toast.LENGTH_SHORT).show();
					return;
				}
				if (ndef.getMaxSize() < msgSize) {
					Toast.makeText(this, "Write할 데이터가 태그 크기보다 큽니다..", Toast.LENGTH_SHORT).show();
					return;
				}

				//ndef객체의 writeNdefMessage(msg) 태그에 write 한다...
				ndef.writeNdefMessage(msg);
				Toast.makeText(this, "태그에 데이터를 write 하였습니다..", Toast.LENGTH_SHORT).show();

			}
		} catch (Exception e) {
		}
	}

}