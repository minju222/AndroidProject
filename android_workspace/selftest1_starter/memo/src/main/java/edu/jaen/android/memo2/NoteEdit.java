package edu.jaen.android.memo2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.jaen.android.memo2.databinding.NoteEditBinding;

public class NoteEdit extends AppCompatActivity {

    private Integer mRowId;

    private NoteEditBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NoteEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mRowId = null;
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mRowId = extras.getInt(NoteManager.KEY_ROWID);
            Note note = (Note)extras.getSerializable(NoteManager.KEY_NOTE);

            binding.title.setText(note.title);
            binding.body.setText(note.body);
        }
       
        binding.confirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Bundle bundle = new Bundle();

                Note note = new Note(binding.title.getText().toString(), binding.body.getText().toString(), System.currentTimeMillis() );
                bundle.putSerializable(NoteManager.KEY_NOTE, note);

                if (mRowId != null) {
                    bundle.putInt(NoteManager.KEY_ROWID, mRowId);
                }
                
                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });
    }
}
