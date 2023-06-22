package com.scsa.androidproject2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.scsa.androidproject2.databinding.ActivityNoteListBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    private static final String TAG = "NoteListActivity_SCSA";
    public static final int INSERT_ID = Menu.FIRST;
    public static final int DELETE_ID = Menu.FIRST + 1;
    public static final int COMPLETE_ID = Menu.FIRST + 2;
    private NoteDbManager manager;
    private NoteAdapter adapter;

    private ActivityNoteListBinding binding;
    List<Note> list;

    List<Note> filteredList2;

    List<Note> filteredList3;

    List<Note> bnote;
    private static final int STATUS_INCOMPLETE = 0;
    private static final int STATUS_COMPLETE = 1;

    int page_status=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.returnBtn.setOnClickListener(v -> {
            Intent intent = new Intent(NoteListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        binding.plusButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteEditActivity.class);
            createActivity.launch(intent);
        });

        binding.allBtn.setOnClickListener(v->{
            page_status = 1;
            initView();
        });

        binding.progressBtn.setOnClickListener(v->{
            page_status = 2;
            initView2();
        });

        binding.completedBtn.setOnClickListener(v -> {
            page_status = 3;
            initView3();
        });

        if (page_status==-1){
            initView();
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        AdapterView.AdapterContextMenuInfo aptInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        if (page_status==1|page_status==-1){
            bnote = list;
        }else if (page_status == 2){
            bnote = filteredList2;
        }else{
            bnote = filteredList3;
        }
        long noteId = aptInfo.id;
        int selectedItemId = bnote.get((int) noteId).getId();
        Log.d(TAG, "(int) noteId: "+ bnote.get((int) noteId).getId());
        Note selectedNote = null;
        for (Note snote : bnote) {
            Log.d(TAG, "배열 순회 확인: "+snote);
            if (snote.getId() == selectedItemId) {
                Log.d(TAG, "snote.getId(): "+snote.getId());
                selectedNote = snote;
                Log.d(TAG, "selectedNote"+selectedNote);
                break;
            }
        }
        if (selectedNote != null) {
            if (selectedNote.getStatus() == STATUS_COMPLETE) {
                menu.add(0, COMPLETE_ID, 0, "In progress");
            } else {
                menu.add(0, COMPLETE_ID, 0, "Complete");
            }
        }

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (page_status==1|page_status==-1){
            bnote = list;
        }else if (page_status == 2){
            bnote = filteredList2;
        }else{
            bnote = filteredList3;
        }
        switch (item.getItemId()) {
            case DELETE_ID:
                ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
                AdapterView.AdapterContextMenuInfo aptInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
                long noteId = aptInfo.id;
                int selectedItemId = bnote.get((int) noteId).getId();
                Log.d(TAG, "(int) noteId: "+ bnote.get((int) noteId).getId());
                manager.delete(selectedItemId);
                if (page_status == -1|page_status==1){
                    initView();
                }else if(page_status == 2){
                    initView2();
                }else{
                    initView3();
                }
                refresh();
                return true;

            case COMPLETE_ID:
                ContextMenu.ContextMenuInfo menuInfo2 = item.getMenuInfo();
                AdapterView.AdapterContextMenuInfo aptInfo2 = (AdapterView.AdapterContextMenuInfo) menuInfo2;
                long noteId2 = aptInfo2.id;
                int selectedItemId2 = bnote.get((int) noteId2).getId();
                List<Note> notes = manager.getNotes();
                int idx = 0;
                for (Note snote : notes){
                    if(selectedItemId2 == snote.getId()){
                        break;
                    }
                    idx++;
                }
                Log.d(TAG, "idx: " + idx);
                Note finalnote = manager.getNotes().get(idx);
                Log.d(TAG, "finalnote: "+finalnote);
                if (finalnote.getStatus() == STATUS_INCOMPLETE){
                    String updatedTitle = "✔ " + finalnote.getTitle();
                    finalnote.setTitle(updatedTitle);
                    finalnote.setStatus(STATUS_COMPLETE);
                    manager.update(finalnote);
                    if (page_status == -1|page_status==1){
                        initView();
                    }else if(page_status == 2){
                        initView2();
                    }else{
                        initView3();
                    }
                    refresh();
                }else{
                    String updatedTitle = finalnote.getTitle().substring(2);
                    finalnote.setTitle(updatedTitle);
                    finalnote.setStatus(STATUS_INCOMPLETE);
                    manager.update(finalnote);
                    if (page_status == -1|page_status==1){
                        initView();
                    }else if(page_status == 2){
                        initView2();
                    }else{
                        initView3();
                    }
                    refresh();
                }
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
                    Note note = (Note) bundle.getSerializable(NoteDbManager.KEY_NOTE);
                    Log.d(TAG, "List class의 bundle: "+note);
                    manager.insert(note);

                    List<Note> notes = manager.getNotes();
                    for (Note storedNote : notes) {
                        Log.d(TAG, "저장된 노트: " + storedNote);
                    }

                    if (page_status == -1|page_status==1){
                        initView();
                    }else if(page_status == 2){
                        initView2();
                    }else{
                        initView3();
                    }
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
                    Note note = (Note) bundle.getSerializable(NoteDbManager.KEY_NOTE);
                    manager.update(note);
                    if (page_status == -1|page_status==1){
                        initView();
                    }else if(page_status == 2){
                        initView2();
                    }else{
                        initView3();
                    }
                    refresh();
                }
            });

    private void initView() {
        manager = NoteDbManager.getInstance(this);
        list = manager.getNotes();
        adapter = new NoteAdapter(list);

        int greyColor = ContextCompat.getColor(this, R.color.light_purple);
        int whiteColor = ContextCompat.getColor(this, R.color.white);
        int blackColor = ContextCompat.getColor(this, R.color.black);
        binding.allBtn.setBackgroundColor(greyColor);
        binding.allBtn.setTextColor(whiteColor);
        binding.progressBtn.setBackgroundColor(whiteColor);
        binding.progressBtn.setTextColor(blackColor);
        binding.completedBtn.setBackgroundColor(whiteColor);
        binding.completedBtn.setTextColor(blackColor);
//        Comparator<Note> dateComparator = new Comparator<Note>() {
//            @Override
//            public int compare(Note note1, Note note2) {
//                if (note1.getDate().equals("") && !note2.getDate().equals("")) {
//                    return 1; // note1이 ""이고 note2가 ""이 아닌 경우, note1을 뒤로 보냄
//                } else if (!note1.getDate().equals("") && note2.getDate().equals("")) {
//                    return -1; // note1이 ""이 아니고 note2가 ""인 경우, note2를 뒤로 보냄
//                } else if (!note1.getDate().equals("") && !note2.getDate().equals("")) {
//                    try {
//                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
//                        Date date1 = format.parse(note1.getDate());
//                        Date date2 = format.parse(note2.getDate());
//
//                        // 오늘 날짜와 비교하여 차이를 구합니다.
//                        long diff1 = Math.abs(date1.getTime() - System.currentTimeMillis());
//                        long diff2 = Math.abs(date2.getTime() - System.currentTimeMillis());
//
//                        // 차이가 작은 순서로 정렬합니다.
//                        return Long.compare(diff1, diff2);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return 0;
//            }
//        };
//
//        Collections.sort(list, dateComparator);
//        adapter.updateList(list);
        // 1. 어댑터 생성
        adapter = new NoteAdapter(list);
        // 2.  뷰와 어댑터 결합
        binding.listview.setAdapter(adapter);
        //3. cliek event 처리
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Note note = list.get(position);
            Intent intent = new Intent(NoteListActivity.this, NoteEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(NoteDbManager.KEY_NOTE, note);
            intent.putExtras(bundle);
            editActivity.launch(intent);
        });

        registerForContextMenu(binding.listview);
    }

    private void initView2() {
        manager = NoteDbManager.getInstance(this);
        list = manager.getNotes();

        int greyColor = ContextCompat.getColor(this, R.color.light_purple);
        int whiteColor = ContextCompat.getColor(this, R.color.white);
        int blackColor = ContextCompat.getColor(this, R.color.black);
        binding.progressBtn.setBackgroundColor(greyColor);
        binding.progressBtn.setTextColor(whiteColor);
        binding.allBtn.setBackgroundColor(whiteColor);
        binding.allBtn.setTextColor(blackColor);
        binding.completedBtn.setBackgroundColor(whiteColor);
        binding.completedBtn.setTextColor(blackColor);

        filteredList2 = new ArrayList<>();

        for (Note note : list) {
            if (note.getStatus() == STATUS_INCOMPLETE) {
                note.setStatus(0);
                filteredList2.add(note);
            }
        }


        Comparator<Note> dateComparator2 = new Comparator<Note>() {
            @Override
            public int compare(Note note1, Note note2) {
                if (note1.getDate().equals("") && !note2.getDate().equals("")) {
                    return 1; // note1이 ""이고 note2가 ""이 아닌 경우, note1을 뒤로 보냄
                } else if (!note1.getDate().equals("") && note2.getDate().equals("")) {
                    return -1; // note1이 ""이 아니고 note2가 ""인 경우, note2를 뒤로 보냄
                } else if (!note1.getDate().equals("") && !note2.getDate().equals("")) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        Date date1 = format.parse(note1.getDate());
                        Date date2 = format.parse(note2.getDate());

                        // 오늘 날짜와 비교하여 차이를 구합니다.
                        long diff1 = Math.abs(date1.getTime() - System.currentTimeMillis());
                        long diff2 = Math.abs(date2.getTime() - System.currentTimeMillis());

                        // 차이가 작은 순서로 정렬합니다.
                        return Long.compare(diff1, diff2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return 0;
            }
        };

        Collections.sort(filteredList2, dateComparator2);

        adapter = new NoteAdapter(filteredList2);
        // 2.  뷰와 어댑터 결합
        binding.listview.setAdapter(adapter);
        //3. cliek event 처리
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Note note = filteredList2.get(position);
            Intent intent = new Intent(NoteListActivity.this, NoteEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(NoteDbManager.KEY_NOTE, note);
            intent.putExtras(bundle);
            editActivity.launch(intent);
        });

        registerForContextMenu(binding.listview);
    }

    private void initView3() {
        manager = NoteDbManager.getInstance(this);
        list = manager.getNotes();

        int greyColor = ContextCompat.getColor(this, R.color.light_purple);
        int whiteColor = ContextCompat.getColor(this, R.color.white);
        int blackColor = ContextCompat.getColor(this, R.color.black);
        binding.completedBtn.setBackgroundColor(greyColor);
        binding.completedBtn.setTextColor(whiteColor);
        binding.allBtn.setBackgroundColor(whiteColor);
        binding.allBtn.setTextColor(blackColor);
        binding.progressBtn.setBackgroundColor(whiteColor);
        binding.progressBtn.setTextColor(blackColor);

        filteredList3 = new ArrayList<>();

        for (Note note : list) {
            if (note.getStatus() == STATUS_COMPLETE) {
                int n_id = note.getId();
                Note filteredNote = new Note(n_id, note.getTitle(), note.getBody(), 1, note.getDate());
                Log.d(TAG, "initView3: "+ filteredNote);
                filteredList3.add(filteredNote);
            }
        }

        Comparator<Note> dateComparator3 = new Comparator<Note>() {
            @Override
            public int compare(Note note1, Note note2) {
                if (note1.getDate().equals("") && !note2.getDate().equals("")) {
                    return 1; // note1이 ""이고 note2가 ""이 아닌 경우, note1을 뒤로 보냄
                } else if (!note1.getDate().equals("") && note2.getDate().equals("")) {
                    return -1; // note1이 ""이 아니고 note2가 ""인 경우, note2를 뒤로 보냄
                } else if (!note1.getDate().equals("") && !note2.getDate().equals("")) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        Date date1 = format.parse(note1.getDate());
                        Date date2 = format.parse(note2.getDate());

                        // 오늘 날짜와 비교하여 차이를 구합니다.
                        long diff1 = Math.abs(date1.getTime() - System.currentTimeMillis());
                        long diff2 = Math.abs(date2.getTime() - System.currentTimeMillis());

                        // 차이가 작은 순서로 정렬합니다.
                        return Long.compare(diff1, diff2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return 0;
            }
        };

        Collections.sort(filteredList3, dateComparator3);

        // 1. 어댑터 생성
        adapter = new NoteAdapter(filteredList3);
        // 2.  뷰와 어댑터 결합
        binding.listview.setAdapter(adapter);
        //3. cliek event 처리
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Note note = filteredList3.get(position);
            Intent intent = new Intent(NoteListActivity.this, NoteEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(NoteDbManager.KEY_NOTE, note);
            intent.putExtras(bundle);
            editActivity.launch(intent);
        });

        registerForContextMenu(binding.listview);
    }


    private void refresh() {
        manager.selectAll();
        adapter.notifyDataSetChanged();
    }


    private class NoteAdapter extends BaseAdapter {
        private List<Note> notes;

        public void updateList(List<Note> newList){
            list = newList;
            notifyDataSetChanged();
        }
        public NoteAdapter(List<Note> notes){
            this.notes = notes;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;

            if (convertView == null) {

                convertView = LayoutInflater.from(NoteListActivity.this).inflate(R.layout.notes_row, viewGroup, false);
                holder = new ViewHolder();
                holder.textView = convertView.findViewById(R.id.tv_content);
                holder.dateTextView = convertView.findViewById(R.id.tv_date); // 추가된 코드
                convertView.setPadding(4,4,4,4);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Note note = notes.get(i);
            holder.textView.setText(notes.get(i).getTitle());
            holder.dateTextView.setText(note.getDate()); // 추가된 코드


            if (note.getStatus() == STATUS_COMPLETE) {
                holder.textView.setTextColor(ContextCompat.getColor(NoteListActivity.this, R.color.green)); // Set green color for complete status
                convertView.setBackgroundColor(ContextCompat.getColor(NoteListActivity.this, R.color.light_green));
            } else {
                holder.textView.setTextColor(ContextCompat.getColor(NoteListActivity.this, R.color.black)); // Set default color for incomplete status
                convertView.setBackgroundColor(ContextCompat.getColor(NoteListActivity.this, R.color.white));
            }
            return convertView;
        }

        class ViewHolder {
            TextView textView;
            TextView dateTextView; // 추가된 코드
        }

        @Override
        public int getCount() {
            return notes.size();
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