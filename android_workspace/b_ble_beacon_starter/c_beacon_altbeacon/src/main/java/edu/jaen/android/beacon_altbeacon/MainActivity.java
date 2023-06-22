package edu.jaen.android.beacon_altbeacon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.jaen.android.beacon_altbeacon.databinding.ActivityMainBinding;
import edu.jaen.android.util.CheckPermission;

//android beacon library를 사용하여 개발한다.
//https://github.com/AltBeacon/android-beacon-library

@RequiresApi(api = Build.VERSION_CODES.O)
@SuppressLint("MissingPermission")
public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity_SCSA";

	private BeaconManager beaconManager;

	private BluetoothManager bluetoothManager;
	private BluetoothAdapter bluetoothAdapter;

	private CheckPermission checkPermission;

	private static final int PERMISSION_REQUEST_CODE = 18;
	private String[] runtimePermissions = {
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.BLUETOOTH_SCAN,
			Manifest.permission.BLUETOOTH_ADVERTISE,
			Manifest.permission.BLUETOOTH_CONNECT
	};

	private static final String BEACON_UUID = "fda50693-a4e2-4fb1-afcf-c6eb07647825";
	private static final String BEACON_MAJOR = "10004";
	private static final String BEACON_MINOR = "54480";

	// Beacon의 Region 설정
	// 비교데이터들로, 설치 지역이 어딘지 판단하기 위한 데이터.
	//estimote : apple, eddystone : google
	private Region region = new Region("estimote"
			, Identifier.parse(BEACON_UUID)
			, Identifier.parse(BEACON_MAJOR)
			, Identifier.parse(BEACON_MINOR)
	);

	private static final double BEACON_DISTANCE = 5.0;
	private boolean eventPopUpAble = true;

	private ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate: ");

		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		//BeaconManager 지정
		beaconManager = BeaconManager.getInstanceForApplication(this);
//		estimo 비컨을 분석 하도록 하기 위하여 beacon parser 오프셋, 버전등을 setLayout으로 지정한다.
//		m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24
//		설명: 0 ~ 1 바이트는 제조사를 나타내는 필드로 파싱하지 않는다.
//		    2~3 바이트는 0x02, 0x15 이다.
//		    4~19 바이트들을 첫번째 ID로 매핑한다.(UUID)
//		    20~21 바이트들을 두번째 ID로 매핑한다.(Major)
//		    22-23 바이트들을 세번째 ID로 매핑한다.(Minor)
//		    24~24 바이트들을 txPower로 매핑한다.
		beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

		bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		bluetoothAdapter = bluetoothManager.getAdapter();

		checkPermission = new CheckPermission(this);

		if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
			Toast.makeText(this, "블루투스 기능을 확인해 주세요.", Toast.LENGTH_SHORT).show();

			Intent bleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(bleIntent, 1);
		}

		if (!checkPermission.runtimeCheckPermission(this, runtimePermissions)) {
			ActivityCompat.requestPermissions(this, runtimePermissions, PERMISSION_REQUEST_CODE);
		} else { //이미 전체 권한이 있는 경우
			startScan();
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSION_REQUEST_CODE) {
			if (checkPermission.hasPermission(runtimePermissions, grantResults)) {
				startScan();
			} else {
				checkPermission.requestPermission();
			}
		}
	}


	private void startScan() {

		// 리전에 비컨이 있는지 없는지..정보를 받는 클래스 지정
		beaconManager.addMonitorNotifier(monitorNotifier);
		beaconManager.startMonitoring(region);

		//detacting되는 해당 region의 beacon정보를 받는 클래스 지정.
		beaconManager.addRangeNotifier(rangeNotifier);
		beaconManager.startRangingBeacons(region);
	}

	//모니터링 결과를 처리할 Notifier를 지정.
	// region에 해당하는 beacon 유무 판단
	MonitorNotifier monitorNotifier = new MonitorNotifier() {
		@Override
		public void didEnterRegion(Region region) { //발견 함.
			Log.d(TAG, "I just saw an beacon for the first time!");
		}

		@Override
		public void didExitRegion(Region region) { //발견 못함.
			Log.d(TAG, "I no longer see an beacon");
		}

		@Override
		public void didDetermineStateForRegion(int state, Region region) { //상태변경
			Log.d(TAG, "I have just switched from seeing/not seeing beacons: " + state);
		}
	};

	//매초마다 해당 리전의 beacon 정보들을 collection으로 제공받아 처리한다.
	RangeNotifier rangeNotifier = new RangeNotifier() {
		@Override
		public void didRangeBeaconsInRegion(Collection beacons, Region region) {
			if (beacons.size() > 0) {
				Iterator iterator = beacons.iterator();
				while (iterator.hasNext()){
					Beacon beacon = (Beacon)iterator.next();
					String msg = "distance: " + beacon.getDistance();
					// 사정거리 내에 있을 경우 이벤트 표시 다이얼로그 팝업
					if(beacon.getDistance() <= BEACON_DISTANCE){
						Log.d(TAG, "didRangeBeaconsInRegion: distance 이내.");
						String txt = binding.textView.getText().toString();
						binding.textView.setText(((new Date()).toString().substring(0, 20) + msg) + "\n\n"+ txt );
					}else{
						Log.d(TAG, "didRangeBeaconsInRegion: distance 이외.");
						eventPopUpAble = true;
					}
					Log.d(TAG, "distance: " + beacon.getDistance() + " id:" + beacon.getId1() + "/" + beacon.getId2() + "/" + beacon.getId3());
				}
			}

			if(beacons.isEmpty()){
				Log.d(TAG, "didRangeBeaconsInRegion: 비컨을 찾을 수 없습니다.");
			}
		}
	};

	@Override
	protected void onStop() {
		super.onStop();
//		finish();
	}

	// destroy에서 beacon scan을 중지 시킨다.
	// beacon scan을 중지 하지 않으면 일정 시간 이후 다시 scan이 가능하다.
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy: ");
	}
}
