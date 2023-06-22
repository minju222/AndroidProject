package kr.jaen.android.mediastore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    private static final String TAG = "MainActivity_SCSA";

    private static final int REQUEST_PERMISSIONS = 100;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 사용자에게 권한 허용 요청하기 (Android 6.0, API 23 이상부터 필요)
            String[] permissions = {"android.permission.READ_MEDIA_AUDIO"};

            int mask = 0;
            for (int i = 0; i < permissions.length; i++) {
                int check = checkSelfPermission(permissions[i]);
                if (check == PackageManager.PERMISSION_GRANTED) {
                    mask += 1 << i;
                }
            }

            // 모든 권한을 획득하지 못했다면
            if (mask != 1) {
                requestPermissions(permissions, REQUEST_PERMISSIONS);
            } else {
                fetch();
            }
        }
        else {
            fetch();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            int mask = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    mask += 1 << i;
                }
            }

            if (mask == 1) {
                Toast.makeText(MainActivity.this,
                        "권한 획득 성공!", Toast.LENGTH_SHORT).show();
                fetch();
            } else {
                Toast.makeText(MainActivity.this,
                        "권한 획득 실패!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetch() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        c = getContentResolver().query(uri, null, null, null, sortOrder);
        /*c = new CursorLoader(this, uri, null, null, null, sortOrder)
                .loadInBackground();*/

        String[] from = new String[] {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME
        };

        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}