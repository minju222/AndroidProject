package kr.jaen.android.notepad2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NoteDbManager {

    public static final String KEY_NOTE = "note";
    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "notes";
    private static final String[] DATABASE_COLUMNS = { "_id", "title", "body" };

    private List<Note> notes;
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public List<Note> getNotes() {
        selectAll();
        return notes;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String TAG = "DatabaseHelper_SCSA";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + "(" + "\n");
            sql.append(DATABASE_COLUMNS[0] + " integer primary key autoincrement, \n");
            sql.append(DATABASE_COLUMNS[1] + " text not null, \n");
            sql.append(DATABASE_COLUMNS[2] + " text not null \n");
            sql.append(");");

            db.execSQL(sql.toString());

            Log.d(TAG, "onCreate()::테이블 생성 완료");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = "DROP TABLE IF EXISTS " + DATABASE_TABLE;
            db.execSQL(sql);
            onCreate(db);

            Log.d(TAG, "onUpgrade()::업그레이드 완료");
        }
    }

    // Singleton Pattern 적용
    private static NoteDbManager instance;
    private NoteDbManager(Context context) {
        notes = new ArrayList<>();
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public static NoteDbManager getInstance(Context context) {
        if (instance == null) {
            instance = new NoteDbManager(context);
        }
        return instance;
    }

    public long insert(Note note) {
        ContentValues values = new ContentValues();
        values.put(DATABASE_COLUMNS[1], note.getTitle());
        values.put(DATABASE_COLUMNS[2], note.getBody());
        return db.insert(DATABASE_TABLE, null, values);
    }

    public void selectAll() {
        notes.clear();

        Cursor c = db.query(DATABASE_TABLE, DATABASE_COLUMNS, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                notes.add(new Note(c.getInt(0), c.getString(1), c.getString(2)));
            } while(c.moveToNext());
        }
        c.close();
    }

    public int update(Note note) {
        ContentValues values = new ContentValues();
        values.put(DATABASE_COLUMNS[1], note.getTitle());
        values.put(DATABASE_COLUMNS[2], note.getBody());

        return db.update(DATABASE_TABLE, values,
                DATABASE_COLUMNS[0] + " = ?", new String[]{String.valueOf(note.getId())});
    }

    public int delete(int _id) {
        return db.delete(DATABASE_TABLE, DATABASE_COLUMNS[0] + " = " + _id, null);
    }
}
