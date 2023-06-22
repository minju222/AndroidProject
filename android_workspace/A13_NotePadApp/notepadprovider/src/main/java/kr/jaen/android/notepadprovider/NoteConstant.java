package kr.jaen.android.notepadprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NoteConstant implements BaseColumns {

    private static final String AUTHORITY = "kr.jaen.android.notepadprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");
    public static final String DATABASE_NAME = "data";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "notes";
    public static final String TITLE = "title";
    public static final String BODY = "body";

}
