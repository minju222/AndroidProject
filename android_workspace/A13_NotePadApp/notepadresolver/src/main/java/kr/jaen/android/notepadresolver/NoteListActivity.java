package kr.jaen.android.notepadresolver;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import kr.jaen.android.notepadresolver.databinding.ActivityNoteListBinding;
import kr.jaen.android.notepadresolver.databinding.RowNoteListBinding;

public class NoteListActivity extends AppCompatActivity {

    private static final String TAG = "NoteListActivity_SCSA";
    public static final int INSERT_ID = Menu.FIRST;
    public static final int DELETE_ID = Menu.FIRST + 1;

    private List<Note> list;
    private NoteAdapter adapter;
    private ActivityNoteListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNoteListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                Intent intent = new Intent(this, NoteEditActivity.class);
                createActivity.launch(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case DELETE_ID:
                ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
                AdapterView.AdapterContextMenuInfo aptInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

                Note note = this.list.get(aptInfo.position);
                delete(note.getId());

                refresh();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    ActivityResultLauncher<Intent> createActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d(TAG, "onActivityResult()");
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    Bundle bundle = intent.getExtras();
                    Note note = (Note) bundle.getSerializable(NoteConstant.DATABASE_TABLE);
                    insert(note);
                    refresh();
                }
            });

    ActivityResultLauncher<Intent> editActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d(TAG, "onActivityResult()");
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    Bundle bundle = intent.getExtras();
                    Note note = (Note) bundle.getSerializable(NoteConstant.DATABASE_TABLE);
                    update(note);
                    refresh();
                }
            });

    private void initView() {
        this.list = new ArrayList<>();
        selectAll();

        // 1. 어댑터 생성
        adapter = new NoteAdapter();

        // 2. 뷰와 어댑터 결합
        binding.listview.setAdapter(adapter);

        //3. cliek event 처리
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Note note = list.get(position);
            Intent intent = new Intent(NoteListActivity.this, NoteEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(NoteConstant.DATABASE_TABLE, note);
            intent.putExtras(bundle);
            editActivity.launch(intent);
        });

        registerForContextMenu(binding.listview);
    }

    private void refresh() {
        selectAll();
        adapter.notifyDataSetChanged();
    }

    public void insert(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteConstant.TITLE, note.getTitle());
        values.put(NoteConstant.BODY, note.getBody());
        Uri uri = getContentResolver().insert(NoteConstant.CONTENT_URI, values);
    }

    private void selectAll() {
        this.list.clear();

        Cursor c = getContentResolver().query(NoteConstant.CONTENT_URI,
                new String[]{ NoteConstant._ID, NoteConstant.TITLE, NoteConstant.BODY },
                null, null, null);

        if (c.moveToFirst()) {
            do {
                this.list.add(new Note(c.getInt(0), c.getString(1), c.getString(2)));
            } while(c.moveToNext());
        }
        c.close();
    }

    public int update(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteConstant.TITLE, note.getTitle());
        values.put(NoteConstant.BODY, note.getBody());

        return getContentResolver().update(NoteConstant.CONTENT_URI, values,
                NoteConstant._ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public int delete(int _id) {
        return getContentResolver().delete(NoteConstant.CONTENT_URI,
                NoteConstant._ID + " = ?",
                new String[]{String.valueOf(_id)});
    }


    private class NoteAdapter extends BaseAdapter {
        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;

            if (convertView == null) {

//                convertView = LayoutInflater.from(NoteListActivity.this).inflate(R.layout.row_note_list, viewGroup, false);
//                holder = new ViewHolder();
//                holder.textView = convertView.findViewById(R.id.tv_content);

                RowNoteListBinding binding = RowNoteListBinding.inflate(LayoutInflater.from(NoteListActivity.this), viewGroup, false);
                convertView = binding.getRoot();

                holder = new ViewHolder();
                holder.textView = binding.tvContent;

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView.setText(list.get(i).getTitle());

            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
    }
}