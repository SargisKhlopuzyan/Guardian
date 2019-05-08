package com.sargis.kh.guardian.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DataDb.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "data";

    public static final String ID = "_id";
    public static final String ARTICLE_ID = "article_id";
    public static final String CATEGORY = "category";
    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String THUMBNAIL_URL = "thumbnail_url";
    public static final String IS_SAVED = "is_saved";
    public static final String IS_PINNED = "is_pinned";

    public DataSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY,"
                + ARTICLE_ID + " TEXT NOT NULL,"
                + CATEGORY + " TEXT NOT NULL,"
                + TITLE + " TEXT NOT NULL,"
                + BODY + " TEXT NOT NULL,"
                + THUMBNAIL_URL + " TEXT NOT NULL,"
                + IS_SAVED + " INTEGER NOT NULL,"
                + IS_PINNED + " INTEGER NOT NULL);";

        db.execSQL(CREATE_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}