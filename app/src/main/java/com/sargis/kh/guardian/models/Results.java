package com.sargis.kh.guardian.models;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.sargis.kh.guardian.database.DataSQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;

public class Results implements Serializable {

    @SerializedName("id") public String id;

    @SerializedName("sectionName") public String sectionName;

    @SerializedName("webPublicationDate") public String webPublicationDate;

    @SerializedName("webTitle") public String webTitle;

    @SerializedName("fields") public Fields fields;

    @SerializedName("isSaved") public int isSaved;

    @SerializedName("isPinned") public int isPinned;

    public static ArrayList<Results> fromCursor(Cursor cursor) {

        ArrayList<Results> resultsList = new ArrayList<>();

        if (cursor.moveToFirst()){
            do{
                Results results = new Results();

                String articleId = cursor.getString(cursor.getColumnIndex(DataSQLiteOpenHelper.ARTICLE_ID));
                String category = cursor.getString(cursor.getColumnIndex(DataSQLiteOpenHelper.CATEGORY));
                String title = cursor.getString(cursor.getColumnIndex(DataSQLiteOpenHelper.TITLE));
                String body = cursor.getString(cursor.getColumnIndex(DataSQLiteOpenHelper.BODY));
                String thumbnailUrl = cursor.getString(cursor.getColumnIndex(DataSQLiteOpenHelper.THUMBNAIL_URL));
                int isSaved = cursor.getInt(cursor.getColumnIndex(DataSQLiteOpenHelper.IS_SAVED));
                int isPinned = cursor.getInt(cursor.getColumnIndex(DataSQLiteOpenHelper.IS_PINNED));

                results.id = articleId;
                results.sectionName = category;
                results.webTitle = title;
                results.fields = new Fields();
                results.fields.body = body;
                results.fields.thumbnail = thumbnailUrl;
                results.isSaved = isSaved;
                results.isPinned = isPinned;

                resultsList.add(results);
            } while(cursor.moveToNext());
        }
        return resultsList;
    }
}