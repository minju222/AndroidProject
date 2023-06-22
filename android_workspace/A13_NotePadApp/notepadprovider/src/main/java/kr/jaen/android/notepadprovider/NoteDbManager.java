package kr.jaen.android.notepadprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NoteDbManager extends ContentProvider {

    private static final String TAG = "NoteDbManager_SCSA";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate()");

        this.dbHelper = new DatabaseHelper(getContext());
        this.db = dbHelper.getWritableDatabase();

        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "insert()::" + uri);
        long rowId = db.insert(NoteConstant.DATABASE_TABLE, null, values);
        return ContentUris.withAppendedId(NoteConstant.CONTENT_URI, rowId);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query()::" + uri);
        return db.query(NoteConstant.DATABASE_TABLE, projection,
                null, null, null, null, null);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update()::" + uri);
        return db.update(NoteConstant.DATABASE_TABLE, values, selection, selectionArgs);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete()::" + uri);
        return db.delete(NoteConstant.DATABASE_TABLE, selection, selectionArgs);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String TAG = "DatabaseHelper_SCSA";

        public DatabaseHelper(Context context) {
            super(context, NoteConstant.DATABASE_NAME, null, NoteConstant.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE IF NOT EXISTS " + NoteConstant.DATABASE_TABLE + "(" + "\n");
            sql.append(NoteConstant._ID + " integer primary key autoincrement, \n");
            sql.append(NoteConstant.TITLE + " text not null, \n");
            sql.append(NoteConstant.BODY + " text not null \n");
            sql.append(");");

            db.execSQL(sql.toString());

            Log.d(TAG, "onCreate()::테이블 생성 완료");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = "DROP TABLE IF EXISTS " + NoteConstant.DATABASE_TABLE;
            db.execSQL(sql);
            onCreate(db);

            Log.d(TAG, "onUpgrade()::업그레이드 완료");
        }
    }
}
