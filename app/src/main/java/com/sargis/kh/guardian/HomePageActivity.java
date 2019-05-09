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
import com.sargis.kh.guardian.helpers.HelperSharedPreferences;
import com.sargis.kh.guardian.listeners.OnBottomReachedListener;
import com.sargis.kh.guardian.models.DataResponse;
import com.sargis.kh.guardian.models.Results;
import com.sargis.kh.guardian.network.DataController;
import com.sargis.kh.guardian.presenters.HomePagePresenter;
import com.sargis.kh.guardian.services.JobSchedulerUtil;
import com.sargis.kh.guardian.utils.Constants;
import com.sargis.kh.guardian.utils.NetworkUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity implements HomePageContract.View, OnBottomReachedListener, LoaderManager.LoaderCallbacks<Cursor> {

    ActivityHomePageBinding binding;
    HomePageContract.Presenter presenter;
    boolean isOnlyCacheDataAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);

        JobSchedulerUtil.scheduleJob(getApplicationContext());

        presenter = new HomePagePresenter(this);

        binding.setIsPinnedItemsAvailable(false);
        setupRecyclerViewPinned();
        setupRecyclerView();

        if (savedInstanceState == null) {
            if (NetworkUtils.isNetworkAvailable(this)) {
                getInitialDataByPage();
                restartLoaderPinnedData();
                setIsOnlyCacheDataAvailable(false);
            } else {
                getCacheData();
            }
        } else {

            setIsOnlyCacheDataAvailable(savedInstanceState.getBoolean(Constants.PayloadKey.IS_ONLY_CACHE_DATA_AVAILABLE));

            if (NetworkUtils.isNetworkAvailable(this)) {
                getInitialDataByPage();
                restartLoaderPinnedData();
                setIsOnlyCacheDataAvailable(false);

//                //TODO This need to be implemented, in case the data should be saved during orientation change.
//                The problem occurs when restoring big data. the application throws an exception.
//                We can use LiveData or fragment in case the data should be saved.

//                Intent intent = getIntent();
//                if (intent.hasExtra(Constants.PayloadKey.FROM_DATE)) {
//                    String fromDate = intent.getStringExtra(Constants.PayloadKey.FROM_DATE);
//                    presenter.getDataSearchedByFromDate(fromDate);
//                }
            } else {
                getCacheData();
            }
        }

        binding.setOnRefreshListener(() -> {
            binding.setIsRefreshing(true);

            if (!NetworkUtils.isNetworkAvailable(this)) {
                getCacheData();
            } else {
                if (isDataAvailable() && !isOnlyCacheDataAvailable()) {
                    presenter.getDataSearchedByFromDate(HelperSharedPreferences.getLastWebPublicationDate());
                } else{
                    setIsOnlyCacheDataAvailable(false);
                    getInitialDataByPage();
                    restartLoaderPinnedData();
                }
            }
        });
    }

    private boolean isOnlyCacheDataAvailable() {
        return isOnlyCacheDataAvailable;
    }

    private void setIsOnlyCacheDataAvailable(boolean isOnlyCacheDataAvailable) {
        this.isOnlyCacheDataAvailable = isOnlyCacheDataAvailable;
    }

    private void openArticleDetailViewScreen(Results result) {
        destroyLoaderCachedData();
        destroyLoaderPinnedData();
        destroyLoaderPinnedAndCachedData();

        Intent intent = new Intent(HomePageActivity.this, ArticleViewPageActivity.class);
        intent.putExtra(Constants.PayloadKey.RESULT, result);
        startActivityForResult(intent, Constants.RequestCode.ARTICLE_VIEW_ACTIVITY_REQUEST_CODE);
    }

    private void getCacheData() {
        setIsOnlyCacheDataAvailable(true);
        binding.setIsRefreshing(true);
        restartLoaderCachedData();
        restartLoaderPinnedAndCachedData();
    }

    private boolean isDataAvailable(){
        HomePageAdapter dataAdapter = (HomePageAdapter) binding.recyclerView.getAdapter();
        return dataAdapter.getData() != null && dataAdapter.getData().size() > 0;
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.PayloadKey.IS_ONLY_CACHE_DATA_AVAILABLE, isOnlyCacheDataAvailable());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DataResponse dataResponse) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            return;
        }

        String lastWebPublicationDate = dataResponse.getResponse().results.get(0).webPublicationDate;
        HelperSharedPreferences.setLastWebPublicationDateIncreasedByOneSecond(lastWebPublicationDate);

        if (isDataAvailable() && !isOnlyCacheDataAvailable()) {
            dataLoadedByFromDate(dataResponse);
        } else {
            getInitialDataByPage();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void displayError(String errorMessage) {
        binding.setIsRefreshing(false);
        binding.setIsStatusVisible(true);
        binding.setStatus(errorMessage);
    }

    @Override
    public void dataLoadedByPage(DataResponse dataResponse) {
        ((HomePageAdapter) binding.recyclerView.getAdapter()).addDataSearchedByPage(dataResponse.getResponse().results);
        setIsOnlyCacheDataAvailable(false);
        binding.setIsRefreshing(false);
        binding.setIsStatusVisible(false);
    }

    @Override
    public void dataLoadedByFromDate(DataResponse dataResponse) {
        ((HomePageAdapter) binding.recyclerView.getAdapter()).addDataSearchedByFromDate(dataResponse.getResponse().results);
        setIsOnlyCacheDataAvailable(false);
        binding.setIsRefreshing(false);
        binding.setIsStatusVisible(false);
    }

    @Override
    public void onBottomReached(int position) {
        if (!binding.swipeRefreshLayout.isRefreshing() && NetworkUtils.isNetworkAvailable(this)) {
            binding.swipeRefreshLayout.setRefreshing(true);
            getDataOfNextPage();
        }
    }

    @Override
    public void onBackPressed() {

        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) binding.recyclerView.getLayoutManager();

        int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];

        ((StaggeredGridLayoutManager) binding.recyclerView.getLayoutManager()).findFirstVisibleItemPositions(lastPositions);
        int firstVisibleItem = Math.min(lastPositions[0], lastPositions[1]);

        if (firstVisibleItem > 0) {
            binding.recyclerView.smoothScrollToPosition(0);
        } else {
            super.onBackPressed();
        }
    }

    private void setupRecyclerViewPinned() {

        binding.recyclerViewPinned.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        HomePagePinnedAdapter adapter = new HomePagePinnedAdapter(result -> {
            openArticleDetailViewScreen(result);
        });

        binding.recyclerViewPinned.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        HomePageAdapter adapter = new HomePageAdapter(this, result -> {
            openArticleDetailViewScreen(result);
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void getInitialDataByPage() {
        binding.setIsRefreshing(true);
        DataController.getInstance().setPage(1);
        presenter.getDataSearchedByPage(DataController.getInstance().getPage());
    }

    private void getDataOfNextPage() {
        binding.setIsRefreshing(true);
        DataController.getInstance().increasePageBy(1);
        presenter.getDataSearchedByPage(DataController.getInstance().getPage());
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        CursorLoader cursorLoader = null;
        switch (id) {
            case Constants.Loader.ID_LOADER_PINNED_AND_CACHED_DATA:
                String selectionPinnedAndSaved = DataSQLiteOpenHelper.IS_SAVED + " =? AND " + DataSQLiteOpenHelper.IS_PINNED + " =?";
                String [] selectionArgPinnedAndSaved = new String[]{"1", "1"};
                cursorLoader = new CursorLoader(getApplicationContext(), DataContentProvider.CONTENT_URI_DATA, null, selectionPinnedAndSaved, selectionArgPinnedAndSaved, null);
                break;
            case Constants.Loader.ID_LOADER_PINNED_DATA:
                String selectionPinned = DataSQLiteOpenHelper.IS_PINNED + " =?";
                String [] selectionArgPinned = new String[]{"1"};
                cursorLoader = new CursorLoader(getApplicationContext(), DataContentProvider.CONTENT_URI_DATA, null, selectionPinned, selectionArgPinned, null);
                break;
            case Constants.Loader.ID_LOADER_CACHED_DATA:
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

        ArrayList<Results> resultsList = Results.fromCursor(cursor);
        switch (loader.getId()) {
            case Constants.Loader.ID_LOADER_PINNED_AND_CACHED_DATA:
                binding.setIsPinnedItemsAvailable(!resultsList.isEmpty());
                ((HomePagePinnedAdapter) binding.recyclerViewPinned.getAdapter()).setResults(resultsList);
                break;
            case Constants.Loader.ID_LOADER_PINNED_DATA:
                binding.setIsPinnedItemsAvailable(!resultsList.isEmpty());
                ((HomePagePinnedAdapter) binding.recyclerViewPinned.getAdapter()).setResults(resultsList);
                break;
            case Constants.Loader.ID_LOADER_CACHED_DATA:
                binding.setIsRefreshing(false);

                ((HomePageAdapter) binding.recyclerView.getAdapter()).setResults(resultsList);
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RequestCode.ARTICLE_VIEW_ACTIVITY_REQUEST_CODE  && resultCode  == RESULT_OK) {
            if (isOnlyCacheDataAvailable()) {
                restartLoaderPinnedAndCachedData();
            } else {
                restartLoaderPinnedData();
            }
        }
    }

    private  void restartLoaderCachedData() {
        getSupportLoaderManager().restartLoader(Constants.Loader.ID_LOADER_CACHED_DATA, null, this);
    }

    private  void restartLoaderPinnedData() {
        getSupportLoaderManager().restartLoader(Constants.Loader.ID_LOADER_PINNED_DATA, null, this);
    }

    private  void restartLoaderPinnedAndCachedData() {
        getSupportLoaderManager().restartLoader(Constants.Loader.ID_LOADER_PINNED_AND_CACHED_DATA, null, this);
    }


    private  void destroyLoaderCachedData() {
        getSupportLoaderManager().destroyLoader(Constants.Loader.ID_LOADER_CACHED_DATA);
    }

    private  void destroyLoaderPinnedData() {
        getSupportLoaderManager().destroyLoader(Constants.Loader.ID_LOADER_PINNED_DATA);
    }

    private  void destroyLoaderPinnedAndCachedData() {
        getSupportLoaderManager().destroyLoader(Constants.Loader.ID_LOADER_PINNED_AND_CACHED_DATA);
    }

}