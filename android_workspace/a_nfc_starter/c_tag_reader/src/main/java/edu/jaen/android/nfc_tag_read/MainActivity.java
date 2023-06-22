package edu.jaen.android.nfc_tag_read;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.jaen.android.nfc_tag_read.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity_SCSA";
	NfcAdapter nAdapter;
	PendingIntent pIntent;
	IntentFilter[] filters;

	private ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		nAdapter = NfcAdapter.getDefaultAdapter(this);

		getNFCData(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// 이후 다른 method에서 getIntent() 호출시 newIntent에서 받은 Intent를 사용하기 위함.
		// set하지 않으면 기존의 intent인 action Main이 나옴.
		setIntent(intent);
		getNFCData(getIntent());
	}

	private void getNFCData(Intent intent) {
		Toast.makeText(this, "수신 액션 : " + getIntent().getAction(), Toast.LENGTH_SHORT).show();
		Log.d(TAG, "getNFCData: "+getIntent().getAction());
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

			if (rawMsgs != null) {






			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume: "+getIntent().getAction());
		Intent i = new Intent(this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_MUTABLE);

		IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

		try {
			filter.addDataType("text/*");
		} catch (IntentFilter.MalformedMimeTypeException e) {
			e.printStackTrace();
			throw new RuntimeException("fail", e);
		}

		filters = new IntentFilter[]{filter, };
		nAdapter.enableForegroundDispatch(this, pIntent, filters, null);

	}

	@Override
	protected void onPause() {
		super.onPause();
		nAdapter.disableForegroundDispatch(this);
	}

}
