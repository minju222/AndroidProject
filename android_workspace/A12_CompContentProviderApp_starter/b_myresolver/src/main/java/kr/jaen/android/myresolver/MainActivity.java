package kr.jaen.android.myresolver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import kr.jaen.android.myresolver.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Target ContentProvider의 Uri
    public static final String AUTHORITY = "kr.jaen.android.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String DEFAULT_SORT_ORDER = "title";
    public static final String TITLE = "title";
    public static final String BODY = "body";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnInsert.setOnClickListener(view -> {
            Uri newRowUri = getContentResolver().insert(CONTENT_URI, new ContentValues());
        });

        binding.btnSelect.setOnClickListener(view -> {
            Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
        });

        binding.btnUpdate.setOnClickListener(view -> {
            int cnt = getContentResolver().update(CONTENT_URI, new ContentValues(), null, null);
        });

        binding.btnDelete.setOnClickListener(view -> {
            int cnt = getContentResolver().delete(CONTENT_URI, null, null);
        });
    }
}