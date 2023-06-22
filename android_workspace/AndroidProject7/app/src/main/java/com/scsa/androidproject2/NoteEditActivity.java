package com.scsa.androidproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.scsa.androidproject2.databinding.ActivityNoteEditBinding;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NoteEditActivity extends AppCompatActivity {
    private static final String TAG = "NoteEditActivity_SCSA";
    private ActivityNoteEditBinding binding;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNoteEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Note note = new Note();

        // NoteListActivity에서 보내온 Bundle이 있는지 확인
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Note temp = (Note) bundle.getSerializable(NoteDbManager.KEY_NOTE);
            note.setId(temp.getId());
            note.setTitle(temp.getTitle());
            note.setBody(temp.getBody());
            note.setStatus(temp.getStatus());
            note.setDate(temp.getDate());
            Log.d(TAG, "bundle 확인"+temp);

            binding.title.setText(note.getTitle());
            binding.body.setText(note.getBody());
            binding.date.setText(note.getDate());
            binding.date.setTypeface(null, Typeface.NORMAL);
            binding.date.setTextSize(20);
        }

        binding.cancel.setOnClickListener(v -> {
            finish();
        });

        binding.confirm.setOnClickListener(view -> {
            String title = String.valueOf(binding.title.getText());
            String body = String.valueOf(binding.body.getText());
            String date = String.valueOf(binding.date.getText());
            int status = note.getStatus();
            note.setTitle(title);
            note.setBody(body);
            note.setStatus(status);
            note.setDate(date);
            binding.date.setTypeface(null, Typeface.NORMAL);
            binding.date.setTextSize(20);

            Log.d(TAG, "onCreate: "+date);
            if(title.isEmpty()|date.equals("우측 캘린더 아이콘을 클릭해주세요")){
                if(title.isEmpty()){
                    Toast.makeText(this, "Title을 작성하세요", Toast.LENGTH_SHORT).show();
                }else if(date.equals("우측 캘린더 아이콘을 클릭해주세요")){
                    Toast.makeText(this, "Date를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }else{
                Bundle reply = new Bundle();
                reply.putSerializable(NoteDbManager.KEY_NOTE, note);
                Log.d(TAG, "Confirm bundle: "+reply);

                Intent intent = new Intent();
                intent.putExtras(reply);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        binding.dateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar calendar = Calendar.getInstance();
                int pYear = calendar.get(Calendar.YEAR);
                int pMonth = calendar.get(Calendar.MONTH);
                int pDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(NoteEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        month = month+1;
                        String date = year +"/" +month +"/"+day;
                        binding.date.setText(date);
                        binding.date.setTypeface(null, Typeface.NORMAL);
                        binding.date.setTextSize(20);

                    }
                },pYear, pMonth, pDay);
                datePickerDialog.show();
            }
        });
    }
}