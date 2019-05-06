package com.sargis.kh.guardian;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.sargis.kh.guardian.adapters.HomePageAdapter;
import com.sargis.kh.guardian.databinding.ActivityHomePageBinding;
import com.sargis.kh.guardian.listeners.OnBottomReachedListener;
import com.sargis.kh.guardian.models.DataResponse;
import com.sargis.kh.guardian.presenters.DataController;
import com.sargis.kh.guardian.presenters.HomePagePresenter;

import java.util.List;

import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity implements HomePageContract.View, OnBottomReachedListener {

    ActivityHomePageBinding binding;
    HomePageContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);

        presenter = new HomePagePresenter(this);

        setupRecyclerView();

        binding.setOnRefreshListener(() -> {
            // Refresh items
            getInitialDataByPage();
        });

        getInitialDataByPage();
    }

    @Override
    public void displayError(String errorMessage) {
//        SwipeRefreshLayout swipeRefreshLayout;
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//            }
//        });
    }

    @Override
    public void loadedData(DataResponse dataResponse) {
        HomePageAdapter dataAdapter = (HomePageAdapter) binding.recyclerView.getAdapter();
        dataAdapter.setResults(dataResponse.getResponse().results);

        // Stop refresh animation
        binding.setIsRefreshing(false);
        binding.setIsStatusVisible(false);
    }

    @Override
    public void onBottomReached(int position) {
        // Start refresh animation
        if (!binding.swipeRefreshLayout.isRefreshing()) {
            binding.swipeRefreshLayout.setRefreshing(true);
            getDataOfNextPage();
        }
    }

    @Override
    public void onBackPressed() {
        int firstVisibleItemPosition = ((LinearLayoutManager)binding.recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisibleItemPosition == 0 || firstVisibleItemPosition == -1) {
            super.onBackPressed();
        } else {
            binding.recyclerView.smoothScrollToPosition(0);
        }
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HomePageAdapter adapter = new HomePageAdapter(this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void getInitialDataByPage() {
        DataController.getInstance().setLoadedTriesCount(1);
        presenter.getDataSearchedByPage(DataController.getInstance().getLoadedTriesCount());
    }

    private void getDataOfNextPage() {
        DataController.getInstance().increaseLoadedTriesBy(1);
        presenter.getDataSearchedByPage(DataController.getInstance().getLoadedTriesCount());
    }



}