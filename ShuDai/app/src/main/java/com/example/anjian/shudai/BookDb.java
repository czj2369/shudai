package com.example.anjian.shudai;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/9/14.
 */

public class BookDb extends SQLiteOpenHelper{
    public static String CRATE_BOOK = "create table book (" +
            "id integer primary key autoincrement," +
            "bookname text," +
            "bookhref text," +
            "bookauthor text," +
            "bookimg text," +
            "bookcontinue text," +
            "position integer," +
            "selectwhich integer)";

    private Context mContext;

    public BookDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CRATE_BOOK);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
