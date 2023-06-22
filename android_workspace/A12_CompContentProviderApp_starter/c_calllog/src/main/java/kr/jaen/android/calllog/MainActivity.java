package kr.jaen.android.calllog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends ListActivity {
    private static final String TAG = "MainActivity_SCSA";
    private static final int REQUEST_PERMISSIONS = 100;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 사용자에게 권한 허용 요청하기 (Android 6.0, API 23 이상부터 필요)
        String[] permissions = { "android.permission.READ_CALL_LOG" };
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                "android.permission.READ_CALL_LOG");
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
        }
        else {
            fetch();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if ("android.permission.READ_CALL_LOG".equals(permissions[0])
                    && grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MainActivity.this,
                        "권한 획득 성공!", Toast.LENGTH_SHORT).show();

                fetch();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "권한 획득 실패!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetch() {
        //최근 통화 목록 URI
        Uri uri = CallLog.Calls.CONTENT_URI;
        c = getContentResolver().query(uri, null, null, null, null);
        /*c = new CursorLoader(this, uri, null, null, null, null)
                .loadInBackground();*/

        //전체 출력
        //커서를 맨 윗줄로 이동
        if(c.moveToFirst()) {
            do {
                int count = c.getColumnCount();
                //열 이동하면서 출력
                for (int i = 0; i < count; i++) {
                    //i번째 column의 이름을 가져오는 것
                    String name = c.getColumnName(i);
                    //i번째 column의 내용을 가져오는 것
                    String value = c.getString(i);
                    Log.d(TAG, "fetch: index: "+ i +", name" + name + ", value : " + value);

                }
            } while (c.moveToNext());//다음줄로 이동(행이동)
        }
        c.moveToFirst(); //커서 다시 첫번째로 올림

        //최근 전화 목록 가져와서 화면에 뿌리기
        //TYPE은 전화 한건지 받은건지 등 받아오는 것
        String[] from = new String[] { CallLog.Calls.NUMBER, CallLog.Calls.TYPE };
        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
        //상속받아서 만든 adapter(안드로이드에서 제공)
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        c.moveToPosition(position);
        String number = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
        //"tel://"uri 전화 걸 수 있는 어플로 이동(여러개면 선택창 뜸)
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + number));
        startActivity(intent);
    }
}