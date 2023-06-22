package kr.jaen.android.notepad2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import kr.jaen.android.notepad2.databinding.ActivityNoteEditBinding;

public class NoteEditActivity extends AppCompatActivity {

    private ActivityNoteEditBinding binding;

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

            binding.title.setText(note.getTitle());
            binding.body.setText(note.getBody());
        }

        binding.confirm.setOnClickListener(view -> {
            String title = String.valueOf(binding.title.getText());
            String body = String.valueOf(binding.body.getText());
            note.setTitle(title);
            note.setBody(body);

            Bundle reply = new Bundle();
            reply.putSerializable(NoteDbManager.KEY_NOTE, note);

            Intent intent = new Intent();
            intent.putExtras(reply);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}