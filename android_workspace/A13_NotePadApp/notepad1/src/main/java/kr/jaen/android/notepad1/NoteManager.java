package kr.jaen.android.notepad1;

import java.util.ArrayList;
import java.util.List;

public class NoteManager {

    public static final String KEY_NOTE = "note";
    private List<Note> notes;

    public List<Note> getNotes() {
        return notes;
    }

    // Singleton Pattern 적용
    private static NoteManager instance;
    private NoteManager() {
        notes = new ArrayList<>();
    }
    public static NoteManager getInstance() {
        if (instance == null) {
            instance = new NoteManager();
        }
        return instance;
    }

    public void insert(Note note) {
        int _id = 1;
        if (notes.size() > 0) {
            Note last = notes.get(notes.size() - 1);
            _id = last.getId() + 1;
        }
        note.setId(_id);
        notes.add(note);
    }

    public void update(Note note) {
        for (int i = 0; i < notes.size(); i++) {
            Note n = notes.get(i);
            if (n.getId() == note.getId()) {
                notes.set(i, note);
                break;
            }
        }
    }

    public void delete(int _id) {
        for (int i = 0; i < notes.size(); i++) {
            Note n = notes.get(i);
            if (n.getId() == _id) {
                notes.remove(i);
                break;
            }
        }
    }
}
