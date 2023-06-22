package com.scsa.workshop3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity_SCSA";
    @Nullable
    @Override
    public String getAttributionTag() {
        return super.getAttributionTag();
    }

    List<MemoDto> list = new ArrayList<>();
    MyAdapter adapter;

    //..여기부터는 문자 수신하면 목록에 등록하는 코드(menifest 파일에 usepermission 코드 추가해야함)
    private static final int REQUEST_PERMISSIONS = 100;
    private SMSReceiver receiver;
    private final String [] REQUIRED_PERMISSIONS = new String []{
            "android.permission.RECEIVE_SMS"
    }; //..문자 변수 선언 끝

    @Override
    //처음 실행됐을 떄 실행되는 함수
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list.add(new MemoDto("부서회의", "전체미팅 건입니다.", "2023-05-01"));
        list.add(new MemoDto("개발미팅", "과정 개발 미팅입니다.", "2023-06-01"));
        list.add(new MemoDto("소개팅", "미팅.", "2023-06-02"));

        //activity_main.xml의 listView 가져옴
        ListView listView = findViewById(R.id.listView);

        //위의 list view에 context menu가 달릴거라는 의미(밑에 함수로 따로 또 구현해서 삭제버튼이 길게 누르면 뜸)
        registerForContextMenu(listView);

        //새로 생성한 adapter가 list에서 정보를 가져와서 listView로 데이터를 하나씩 넣어서 보여줌
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        //listview에서 하나가 클릭이 되면 MemoInfo로 이동
        listView.setOnItemClickListener((parent, view, position, id) -> {
            //position과 item을 momoinfo로 넘겨줌
            Intent intent = new Intent(MainActivity.this, MemoInfo.class);
            intent.putExtra("position", position);
            intent.putExtra("item", list.get(position));
            //MemoInfo로 넘어가는 startActivityForResult실행되고 난 이후에 onActivityResult로 가서 저장함, requestCode는 2 부분이 실행됨
            startActivityForResult(intent, 2);

//            //리스트 누르면 전화걸기 앱이 실행되게 하는 방법
//            Intent intent = new Intent();
//            //누르면 전화걸기 앱으로 들어감
//            getIntent().setAction(Intent.ACTION_DIAL);
//            //앱에 data를 줌 -> 전화걸기 앱이 실행되면 넘겨준 번호가 입력된 채로 뜸
//            intent.setData(Uri.parse("tel:01038892892"));
//            //누르기 실행
//            startActivity(intent);


//             //리스트 누르면 브라우저 여는 방법
//            Intent intent = new Intent();
//            //누르면 전화걸기 앱으로 들어감
//            getIntent().setAction(Intent.ACTION_VIEW);
//            //앱에 url 데이터를 줌
//            intent.setData(Uri.parse("https://www.naver.com"));
//            //누르기 실행
//            startActivity(intent);
//
        });

        //..여기부터 on Create 끝까지는 메세지 받아서 목록에 저장하는 코드
        // 1. 권한이 있는지 확인.
        int permission = ContextCompat.checkSelfPermission(this,REQUIRED_PERMISSIONS[0]);
        // 2. 권한이 없으면 런타임 퍼미션 창 띄우기. 있으면 정상진행.
        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_PERMISSIONS);
        }

        // SMSReceiver에서 메모 내용 받은 것 목록에 저장
        Intent msgintent = getIntent();
        String sms = msgintent.getStringExtra("sms");
        if (sms != null){
            list.add(new MemoDto(sms, "From Message : " + sms, "2023-06-05"));
        }
        //..메세지 onCreate 부분 끝
    }

    //start of options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //위의 앱바의 더하기 버튼 만드는 것
        getMenuInflater().inflate(R.menu.menu, menu); //리소스 밑에 만들어논 menu.xml에서 가져와서 앱바에다가 표시
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //앱바의 버튼을 를 클릭하면 이동하는 함수
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // 버튼 누르는 액션 시작하면 이동
        startActivityForResult(new Intent(this, MemoEdit.class),1);
        return super.onOptionsItemSelected(item);
    }
    //end of options menu

    //startActivityForResult로 갔다가 onActivityResult로 데이터 들고 돌아옴
    @Override
    //resultCode는
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //requestCode 프로그램에서 보장하므로 신경x, resultCode는 EditMemo에서 받아옴, Intent는 받아온 데이터
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            if (resultCode == RESULT_OK && data != null) { //사용자가 그냥 뒤로가기 버튼을 누르면 null임 -> 이 경우 제외하기 위함
                //result라는 이름으로 받아온 내용을 MemoDto calss의 dto 변수에 담음
                MemoDto dto = (MemoDto) data.getSerializableExtra("result");
                Log.d(TAG, "onActivityResult: " + data.getSerializableExtra("result")); // 확인용
                list.add(dto); // 리스트에  dto 저장
                adapter.notifyDataSetChanged();// list에 내용이 추가되어서 adapter로 새로 화면 만들어서 보여줌
            }
        }
        if(requestCode==2) {
            if (resultCode == RESULT_OK && data != null) { //사용자가 그냥 뒤로가기 버튼을 누르면 null임 -> 이 경우 제외하기 위함
                //result라는 이름으로 받아온 내용을 MemoDto calss의 dto 변수에 담음
                MemoDto dto = (MemoDto) data.getSerializableExtra("result");
                int position = (int) data.getSerializableExtra("position");
                Log.d(TAG, "onActivityResult: " + data.getSerializableExtra("result")); // 확인용
                list.set(position, dto); // 리스트에 넘겨받은 dto 저장
                adapter.notifyDataSetChanged();// list에 내용이 추가되어서 adapter로 새로 화면 만들어서 보여줌
            }
        }
    }

    @Override
    //길게 꾹 눌렀을 때 삭제라는 버튼이 뜨게 함
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,0,0,"삭제");
    }

    @Override
    // 어느 위치에서 삭제 버튼이 클릭됐는지 가져오는 함수
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo aptInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        Toast.makeText(this, "position:"+aptInfo.position, Toast.LENGTH_SHORT).show();

        //받아온 position의 데이터 삭제
        list.remove(aptInfo.position);
        //list 삭제됐으니까 새로 화면 그림
        adapter.notifyDataSetChanged();

        return super.onContextItemSelected(item);

    }


    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        // 화면구성하는 함수(list 좌라락 뜨게 함)
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            //convertview가 처음 만들어졌을때만 layoutinflater로 inflate, 그 이외에는 재사용
            if(view == null){
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                view = inflater.inflate(R.layout.item, parent, false);
            }

            //리스트에서 position에 해당하는 값을 가져와서 넣음
            MemoDto dto = list.get(position);

            TextView textView = view.findViewById(R.id.title);
            TextView date = view.findViewById(R.id.date);
            textView.setText(dto.getTitle());
            date.setText(dto.getDate());

            return view;
        }
    }

    //.. 여기부터는 문자 수신하면 목록에 등록하는 코드들
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
        //SMS_RECEIVED filter가 오면 registerReceiver가 실행됨(==문자가 오면 실행됨)
        registerReceiver(receiver, filter);
    }

    @Override
    //Resume에서 reciver를 받고(regist 실행됨)
    protected void onResume() {
        super.onResume();
        regist();
    }

    @Override
    //Pause되면 receiver 버림(app이 백그라운드로 내려가면 문자 수신 안함)
    //Pause되면 receiver 버림(app이 백그라운드로 내려가면 문자 수신 안함)
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

}