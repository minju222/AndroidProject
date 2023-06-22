package edu.jaen.android.memo2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.jaen.android.memo2.databinding.NoteEditBinding;
import edu.jaen.android.memo2.util.CheckPermission;

@SuppressLint("MissingPermission")
public class NoteEdit extends AppCompatActivity {

    private static final String TAG = "NoteEdit_SCSA";
    private Integer mRowId;

    private BeaconManager beaconManager;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;

    private CheckPermission checkPermission;

    private static final int PERMISSION_REQUEST_CODE = 18;
    private String[] runtimePermissions;

    private static final String BEACON_UUID = "fda50693-a4e2-4fb1-afcf-c6eb07647825";
    private static final String BEACON_MAJOR = "10004";
    private static final String BEACON_MINOR = "54480";

    // Beacon의 Region 설정
    // 비교데이터들로, 설치 지역이 어딘지 판단하기 위한 데이터.
    //estimote : apple, eddystone : google
    private Region region = new Region("estimote"
            , Arrays.asList(Identifier.parse(BEACON_UUID), Identifier.parse(BEACON_MAJOR) , Identifier.parse(BEACON_MINOR))
            , "00:81:F9:E2:69:F8"
    );

    private NoteEditBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NoteEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //31 이상
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            runtimePermissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT
            };
        } else {
            runtimePermissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
        }

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

    @Override
    protected void onResume() {
        super.onResume();

        //바로 동작하면 resume하자마자 닫겨벼려서, 5초 있다가 등록되도록 지정함.
        handler.postDelayed(() -> {
            //detacting되는 해당 region의 beacon정보를 받는 클래스 지정.
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeacons(region);
        }, 5_000);
    }
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.removeRangeNotifier(rangeNotifier);
    }

    private static final double BEACON_DISTANCE = 5.0;

    //매초마다 해당 리전의 beacon 정보들을 collection으로 제공받아 처리한다.
    RangeNotifier rangeNotifier = (beacons, region) -> {
        if (beacons.size() > 0) {
            Iterator iterator = beacons.iterator();
            while (iterator.hasNext()){
                Beacon beacon = (Beacon)iterator.next();
                String msg = "distance: " + beacon.getDistance();
                // 사정거리 내에 있을 경우 이벤트 표시 다이얼로그 팝업
                if(beacon.getDistance() <= BEACON_DISTANCE){
                    Log.d(TAG, "didRangeBeaconsInRegion: distance 이내.");

                    Bundle bundle = new Bundle();

                    Note note = new Note(binding.title.getText().toString(), binding.body.getText().toString(), System.currentTimeMillis() );
                    bundle.putSerializable(NoteManager.KEY_NOTE, note);
                    bundle.putSerializable("wantToQuit", true);

                    if (mRowId != null) {
                        bundle.putInt(NoteManager.KEY_ROWID, mRowId);
                    }

                    Intent mIntent = new Intent();
                    mIntent.putExtras(bundle);
                    setResult(RESULT_OK, mIntent);
                    finish();

                }else{
                    Log.d(TAG, "didRangeBeaconsInRegion: distance 이외.");
                }
                Log.d(TAG, "distance: " + beacon.getDistance() + " id:" + beacon.getId1() + "/" + beacon.getId2() + "/" + beacon.getId3());
            }
        }

        if(beacons.isEmpty()){
            Log.d(TAG, "didRangeBeaconsInRegion: 비컨을 찾을 수 없습니다.");
        }
    };

    private void initView(){
        mRowId = null;
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mRowId = extras.getInt(NoteManager.KEY_ROWID);
            Note note = (Note)extras.getSerializable(NoteManager.KEY_NOTE);

            binding.title.setText(note.title);
            binding.body.setText(note.body);
        }
       
        binding.confirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Bundle bundle = new Bundle();

                Note note = new Note(binding.title.getText().toString(), binding.body.getText().toString(), System.currentTimeMillis() );
                bundle.putSerializable(NoteManager.KEY_NOTE, note);

                if (mRowId != null) {
                    bundle.putInt(NoteManager.KEY_ROWID, mRowId);
                }
                
                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });
    }
}
