package com.sargis.kh.guardian;

import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;

import com.sargis.kh.guardian.content_providers.DataContentProvider;
import com.sargis.kh.guardian.database.DataSQLiteOpenHelper;
import com.sargis.kh.guardian.databinding.ActivityArticleViewPageBinding;
import com.sargis.kh.guardian.models.Results;
import com.sargis.kh.guardian.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleViewPageActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Results results = new Results();

    ActivityArticleViewPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_view_page);
        setSupportActionBar(binding.toolbar);

        Bundle bundle = getIntent().getExtras();
        results = (Results)bundle.getSerializable(Constants.PayloadKey.RESULT);

        binding.setTitle(results.webTitle);

        if (results.fields != null && results.fields.thumbnail != null) {
            Picasso.get().load(results.fields.thumbnail)
                    .placeholder(R.drawable.white_image)
                    .into(binding.imageView);
        } else {
            Picasso.get().load(R.drawable.white_image)
                    .into(binding.imageView);
        }

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadData(results.fields.body,"text/html", null);

        getSupportLoaderManager().restartLoader(Constants.Loader.ID_LOADER_ITEM_BY_ID, null, this);


        binding.setOnSaveClick(v -> {
            Log.e("LOG_TAG", "setOnSaveClick");
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataSQLiteOpenHelper.ARTICLE_ID, results.id);
            contentValues.put(DataSQLiteOpenHelper.CATEGORY, results.sectionName);
            contentValues.put(DataSQLiteOpenHelper.TITLE, results.webTitle);
            contentValues.put(DataSQLiteOpenHelper.BODY, results.fields.body);
            contentValues.put(DataSQLiteOpenHelper.THUMBNAIL_URL, results.fields.thumbnail);
            contentValues.put(DataSQLiteOpenHelper.IS_SAVED, true);
            contentValues.put(DataSQLiteOpenHelper.IS_PINNED, results.isPinned);
            results.isSaved = 1;
            binding.setIsItemSaved(true);

            if (results.isPinned == 1) {
                String selectionPinned = DataSQLiteOpenHelper.ARTICLE_ID + " =?";
                String [] selectionArgPinned = new String[]{results.id};
                getContentResolver().update(DataContentProvider.CONTENT_URI_DATA, contentValues, selectionPinned, selectionArgPinned);
            } else {
                getContentResolver().insert(DataContentProvider.CONTENT_URI_DATA, contentValues);
            }
        });

        binding.setOnCheckboxClick(v -> {
            boolean isChecked = ((CheckBox)v).isChecked();
            Log.e("LOG_TAG", "setOnCheckedChange:: isChecked: " + isChecked);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataSQLiteOpenHelper.ARTICLE_ID, results.id);
            contentValues.put(DataSQLiteOpenHelper.CATEGORY, results.sectionName);
            contentValues.put(DataSQLiteOpenHelper.TITLE, results.webTitle);
            contentValues.put(DataSQLiteOpenHelper.BODY, results.fields.body);
            contentValues.put(DataSQLiteOpenHelper.THUMBNAIL_URL, results.fields.thumbnail);
            contentValues.put(DataSQLiteOpenHelper.IS_SAVED, results.isSaved);
            contentValues.put(DataSQLiteOpenHelper.IS_PINNED, isChecked);

            results.isPinned = isChecked ? 1 : 0;

            String selection = DataSQLiteOpenHelper.ARTICLE_ID + " =?";
            String [] selectionArg = new String[]{results.id};

            if (results.isSaved == 1) {
                getContentResolver().update(DataContentProvider.CONTENT_URI_DATA, contentValues, selection, selectionArg);
            } else {
                if(isChecked) {
                    getContentResolver().insert(DataContentProvider.CONTENT_URI_DATA, contentValues);
                } else {
                    getContentResolver().delete(DataContentProvider.CONTENT_URI_DATA, selection, selectionArg);
                }
            }

        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.e("LOG_TAG", "onCreateLoader");
        CursorLoader cursorLoader = null;
        switch (id) {
            case Constants.Loader.ID_LOADER_ITEM_BY_ID:
                String selectionPinned = DataSQLiteOpenHelper.ARTICLE_ID + " =?";
                String [] selectionArgPinned = new String[]{results.id};
                cursorLoader = new CursorLoader(getApplicationContext(), DataContentProvider.CONTENT_URI_DATA, null, selectionPinned, selectionArgPinned, null);
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        Log.e("LOG_TAG", "onLoadFinished");
        if (cursor == null || cursor.isClosed()) return;
        List<Results> resultsList = Results.fromCursor(cursor);
        if (resultsList.size() == 0) return;

        switch (loader.getId()) {
            case Constants.Loader.ID_LOADER_ITEM_BY_ID:
                results.isSaved = resultsList.get(0).isSaved;
                results.isPinned = resultsList.get(0).isPinned;
                binding.setIsItemSaved(results.isSaved == 1);
                binding.setIsItemPinned(results.isPinned == 1);
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

}