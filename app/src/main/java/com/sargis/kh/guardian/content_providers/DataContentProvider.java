package com.sargis.kh.guardian.content_providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sargis.kh.guardian.database.DataSQLiteOpenHelper;

public class DataContentProvider extends ContentProvider {

    public static final String AUTHORITY = "data.content.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String TABLE_NAME = "data";

    // content URI = base content URI + path
    public static final Uri CONTENT_URI_DATA = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    private static final int KEY_DATA = 100;
    private static final int KEY_DATA_WITH_ID = 101;

    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private DataSQLiteOpenHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, KEY_DATA);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", KEY_DATA_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DataSQLiteOpenHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,  projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri (getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        Uri returnUri;

        int match = URI_MATCHER.match(uri);
        long id;
        switch (match) {
            case KEY_DATA:
                id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).appendPath("" + id).build();
                    getContext().getContentResolver().notifyChange(uri, null);  //TODO
                    return returnUri;
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int i = db.delete(TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null); //TODO
        return i;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int i = db.update(TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null); //TODO
        return i;
    }

}