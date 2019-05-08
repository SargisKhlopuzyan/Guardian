package com.sargis.kh.guardian;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.sargis.kh.guardian.adapters.HomePageAdapter;
import com.sargis.kh.guardian.adapters.HomePagePinnedAdapter;
import com.sargis.kh.guardian.content_providers.DataContentProvider;
import com.sargis.kh.guardian.database.DataSQLiteOpenHelper;
import com.sargis.kh.guardian.databinding.ActivityHomePageBinding;
import com.sargis.kh.guardian.listeners.OnBottomReachedListener;
import com.sargis.kh.guardian.models.DataResponse;
import com.sargis.kh.guardian.models.Results;
import com.sargis.kh.guardian.presenters.DataController;
import com.sargis.kh.guardian.presenters.HomePagePresenter;
import com.sargis.kh.guardian.services.JobSchedulerUtil;
import com.sargis.kh.guardian.utils.Constants;

import java.util.List;

public class HomePageActivity extends AppCompatActivity implements HomePageContract.View, OnBottomReachedListener, LoaderManager.LoaderCallbacks<Cursor> {

    ActivityHomePageBinding binding;
    HomePageContract.Presenter presenter;

    boolean isOfflineMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);

        JobSchedulerUtil.scheduleJob(getApplicationContext());

        presenter = new HomePagePresenter(this);

        binding.setIsPinnedItemsAvailable(false);
        setupRecyclerViewPinned();
        setupRecyclerView();

        if (isOfflineMode) {
            getSupportLoaderManager().restartLoader(Constants.Loader.ID_LOADER_PINNED_AND_SAVED_DATA, null, this);
            getSupportLoaderManager().restartLoader(Constants.Loader.ID_LOADER_SAVED_DATA, null, this);
        } else {
            //TODO
            getInitialDataByPage();
            getSupportLoaderManager().restartLoader(Constants.Loader.ID_LOADER_PINNED_DATA, null, this);
        }

        binding.setOnRefreshListener(() -> {
            if (isOfflineMode) {
                binding.setIsRefreshing(false);
            } else {
                //TODO
                getInitialDataByPage();
            }
        });
    }

    @Override
    public void displayError(String errorMessage) {
        binding.setIsRefreshing(false);
        binding.setIsStatusVisible(true);
        binding.setStatus(errorMessage);
    }

    @Override
    public void loadedData(DataResponse dataResponse) {
        HomePageAdapter dataAdapter = (HomePageAdapter) binding.recyclerView.getAdapter();
        dataAdapter.addNewResults(dataResponse.getResponse().results);

        binding.setIsRefreshing(false);
        binding.setIsStatusVisible(false);
    }

    @Override
    public void onBottomReached(int position) {
        if (!binding.swipeRefreshLayout.isRefreshing() && !isOfflineMode) {
            binding.swipeRefreshLayout.setRefreshing(true);
            getDataOfNextPage();
        }
    }

    @Override
    public void onBackPressed() {

        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) binding.recyclerView.getLayoutManager();

        int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];

        ((StaggeredGridLayoutManager) binding.recyclerView.getLayoutManager()).findFirstVisibleItemPositions(lastPositions);// getChildCount(); //findFirstVisibleItemPosition();
        int firstVisibleItem = Math.min(lastPositions[0], lastPositions[1]);//findMax(lastPositions);

        if (firstVisibleItem > 0) {
            binding.recyclerView.smoothScrollToPosition(0);
        } else {
            super.onBackPressed();
        }
    }

    private void setupRecyclerViewPinned() {

        binding.recyclerViewPinned.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        HomePagePinnedAdapter adapter = new HomePagePinnedAdapter(result -> {
            Intent intent = new Intent(HomePageActivity.this, ArticleViewPageActivity.class);
            intent.putExtra(Constants.PayloadKey.RESULT, result);
            startActivityForResult(intent, Constants.RequestCode.ARTICLE_VIEW_ACTIVITY_REQUEST_CODE);
        });

        binding.recyclerViewPinned.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        HomePageAdapter adapter = new HomePageAdapter(this, result -> {
                Intent intent = new Intent(HomePageActivity.this, ArticleViewPageActivity.class);
                intent.putExtra(Constants.PayloadKey.RESULT, result);
                startActivityForResult(intent, Constants.RequestCode.ARTICLE_VIEW_ACTIVITY_REQUEST_CODE);
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void getInitialDataByPage() {
        DataController.getInstance().setLoadedTriesCount(1);
        presenter.getDataSearchedByPage(DataController.getInstance().getLoadedTriesCount());
    }

    private void getDataOfNextPage() {
        DataController.getInstance().increaseLoadedTriesCount(1);
        presenter.getDataSearchedByPage(DataController.getInstance().getLoadedTriesCount());
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        CursorLoader cursorLoader = null;
        switch (id) {
            case Constants.Loader.ID_LOADER_PINNED_AND_SAVED_DATA:
                String selectionPinnedAndSaved = DataSQLiteOpenHelper.IS_SAVED + " =? AND " + DataSQLiteOpenHelper.IS_PINNED + " =?";
                String [] selectionArgPinnedAndSaved = new String[]{"1", "1"};
                cursorLoader = new CursorLoader(getApplicationContext(), DataContentProvider.CONTENT_URI_DATA, null, selectionPinnedAndSaved, selectionArgPinnedAndSaved, null);
                break;
            case Constants.Loader.ID_LOADER_PINNED_DATA:
                String selectionPinned = DataSQLiteOpenHelper.IS_PINNED + " =?";
                String [] selectionArgPinned = new String[]{"1"};
                cursorLoader = new CursorLoader(getApplicationContext(), DataContentProvider.CONTENT_URI_DATA, null, selectionPinned, selectionArgPinned, null);
                break;
            case Constants.Loader.ID_LOADER_SAVED_DATA:
                String selectionSaved = DataSQLiteOpenHelper.IS_SAVED + " =?";
                String [] selectionArgSaved = new String[]{"1"};
                cursorLoader = new CursorLoader(getApplicationContext(), DataContentProvider.CONTENT_URI_DATA, null, selectionSaved, selectionArgSaved, null);
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.isClosed()) return;

        List<Results> resultsList = Results.fromCursor(cursor);

        switch (loader.getId()) {
            case Constants.Loader.ID_LOADER_PINNED_AND_SAVED_DATA:
                if(resultsList.isEmpty()) {
                    binding.setIsPinnedItemsAvailable(false);
                }
                else {
                    binding.setIsPinnedItemsAvailable(true);
                }

                ((HomePagePinnedAdapter) binding.recyclerViewPinned.getAdapter()).setResults(resultsList);
                break;
            case Constants.Loader.ID_LOADER_PINNED_DATA:
                if(resultsList.isEmpty()) {
                    binding.setIsPinnedItemsAvailable(false);
                }
                else {
                    binding.setIsPinnedItemsAvailable(true);
                }

                ((HomePagePinnedAdapter) binding.recyclerViewPinned.getAdapter()).setResults(resultsList);
                break;
            case Constants.Loader.ID_LOADER_SAVED_DATA:
                ((HomePageAdapter) binding.recyclerView.getAdapter()).setResults(resultsList);
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

}