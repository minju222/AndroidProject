package edu.jaen.android.beacon_basic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import edu.jaen.android.beacon_basic.databinding.ActivityMainBinding;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.mfdata.IBeaconManufacturerData;

@SuppressLint("MissingPermission")
public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity_SCSA";

	private BluetoothManager bluetoothManager;
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothLeScanner bluetoothLeScanner;
	private LeDeviceListAdapter leDeviceListAdapter;
	private String addr;

	private CheckPermission checkPermission;

	private static final int PERMISSION_REQUEST_CODE = 18;
	private String[] runtimePermissions = {
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.BLUETOOTH_SCAN,
			Manifest.permission.BLUETOOTH_ADVERTISE,
			Manifest.permission.BLUETOOTH_CONNECT
	};

	private ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		checkPermission = new CheckPermission(this);
		bluetoothAdapter = bluetoothManager.getAdapter();
		bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();


		if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
			Toast.makeText(this, "블루투스 기능을 확인해 주세요.", Toast.LENGTH_SHORT).show();

			Intent bleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(bleIntent, 1);
		}

		if (!checkPermission.runtimeCheckPermission(this, runtimePermissions)) {
			ActivityCompat.requestPermissions(this, runtimePermissions, PERMISSION_REQUEST_CODE);
		} else { //이미 전체 권한이 있는 경우
			initView();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSION_REQUEST_CODE) {
			if (checkPermission.hasPermission(runtimePermissions, grantResults)) {
				initView();
			} else {
				checkPermission.requestPermission();
			}
		}
	}


	public void initView() {
		leDeviceListAdapter = new LeDeviceListAdapter(this);
		binding.devicelist.setAdapter(leDeviceListAdapter);
		binding.devicelist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				BluetoothDevice device = (BluetoothDevice) leDeviceListAdapter.getItem(position);
				addr = device.toString();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_scan:
				// Toast.makeText(this, "Start Scan", Toast.LENGTH_SHORT).show();
				startScan();
				//30초 후 종료요청한다.
//				new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						Toast.makeText(getApplicationContext(), "stopScan...", Toast.LENGTH_SHORT).show();
//						stopScan();
//					}
//				}, 30 * 1_000);

				break;

			case R.id.action_stop:
				// Toast.makeText(this, "Stop Scan", Toast.LENGTH_SHORT).show();
				stopScan();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startScan() {
		Log.d(TAG, "startScan");
		leDeviceListAdapter.clear();
		leDeviceListAdapter.notifyDataSetChanged();
		// IBeacon Sensor Scan
		bluetoothLeScanner.startScan(mLeScanCallback);
	}

	private void stopScan() {
		Log.d(TAG, "stopSCan");

		// IBeacon Sensor Scan
		bluetoothLeScanner.stopScan(mLeScanCallback);

	}

	BluetoothDevice remoteDevice;

	// ======================IBeacon 용 LeScanCallback=======================

	private ScanCallback mLeScanCallback = new ScanCallback() {
		private int tx;
		private int major;
		private int minor;
		private double distance;

		@Override
		public void onScanResult(int callbackType, ScanResult result) {
			Log.d(TAG, "onScanResult: result:" + result);
			processResult(result.getDevice(), result.getRssi(), result.getScanRecord());
		}

		public void processResult(BluetoothDevice device, int rssi, ScanRecord scanRecord) {
			remoteDevice = device;
			Log.d(TAG, "device >>>> " + device + " updated.");

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					leDeviceListAdapter.addDevice(remoteDevice);
					leDeviceListAdapter.notifyDataSetChanged();
				}
			});

			if (device.toString().equals(addr)) {
				final BluetoothLeDevice deviceLe = new BluetoothLeDevice(
						device, rssi, scanRecord.getBytes(), System.currentTimeMillis());
				try {

					Log.d(TAG, "Selected Device " + deviceLe.getAddress() + " updated.");

					IBeaconManufacturerData iBeaconData = new IBeaconManufacturerData(deviceLe);
					tx = iBeaconData.getCalibratedTxPower();
					major = iBeaconData.getMajor();
					minor = iBeaconData.getMinor();
					Log.d(TAG, "UUID: "+iBeaconData.getUUID());
					Log.d(TAG, "major: "+iBeaconData.getMajor());
					Log.d(TAG, "minor: "+iBeaconData.getMinor());

					// distance = (float) rssi / (float) tx;
					distance = Math.pow(12.0,
							1.5 * (((float) rssi / (float) tx) - 1));

					System.out.println("rssi : " + rssi + ", tx : " + tx
							+ ", =====거리 : " + distance);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							binding.mTv.setText("Major : " + major + ", Minor " + minor
									+ ", distance : " + distance);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	// IOS 거리 계산 공식 : 값이 많이 튐
	private double calculateAccuracy(int txPower, double rssi) {

		if (rssi == 0) {
			return -1.0; // if we can not determine accuracy, return -1.
		}

		double ratio = rssi * 1.0 / txPower;
		if (ratio < 1.0) {
			return Math.pow(ratio, 10);
		} else {
			double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
			return accuracy;
		}
	}

	private double getNewAccuracy(int txPower, double rssi) {
		return Math.pow(12.0, 1.5 * ((rssi / txPower) - 1));
	}
}
