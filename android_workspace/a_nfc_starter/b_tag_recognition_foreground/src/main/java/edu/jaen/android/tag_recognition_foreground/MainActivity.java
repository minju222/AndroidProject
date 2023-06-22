package edu.jaen.android.tag_recognition_foreground;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.jaen.android.tag_recognition_foreground.databinding.ActivityMainBinding;

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

		String action = getIntent().getAction();
		binding.textview.setText(action);

		Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
		nAdapter = NfcAdapter.getDefaultAdapter(this);

		Intent i = new Intent(this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_MUTABLE);
		IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

		try {
			filter.addDataType("*/*");
		} catch (IntentFilter.MalformedMimeTypeException e) {
			e.printStackTrace();
		}
		filters = new IntentFilter[]{filter,};
	}

	@Override
	protected void onResume() {
		super.onResume();
		nAdapter.enableForegroundDispatch(this, pIntent, filters, null);
	}

	@Override
	protected void onPause() {
		super.onPause();
		nAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Toast.makeText(this, "onNewIntent", Toast.LENGTH_SHORT).show();

		String action = intent.getAction();
		Log.d(TAG, "New Intent action : " + action);

		parseData(intent);

	}

	private void parseData(Intent intent) {
		Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		Log.d(TAG, "parseData1: " + data);

		NdefMessage ndefM = (NdefMessage) data[0];
		NdefRecord ndefR = ndefM.getRecords()[0];

		byte[] byteArr = ndefR.getPayload();

		//앞 en 빼고
		//String textData = new String(byteArr, 3, byteArr.length-3);
		binding.textview.setText("nfc tag data : " + new String(byteArr));

	}
}
