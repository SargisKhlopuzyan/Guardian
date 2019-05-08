package com.sargis.kh.guardian.presenters;

import com.sargis.kh.guardian.HomePageContract;
import com.sargis.kh.guardian.models.DataResponse;
import com.sargis.kh.guardian.network.APIService;
import com.sargis.kh.guardian.network.RetrofitClientInstance;
import com.sargis.kh.guardian.network.SearchUrlHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePagePresenter implements HomePageContract.Presenter {

    HomePageContract.View viewCallback;

    public HomePagePresenter(HomePageContract.View viewCallback) {
        this.viewCallback = viewCallback;
    }

    @Override
    public void getDataSearchedByPage(int pageIndex) {
        /*Create handle for the RetrofitInstance interface*/
        APIService service = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);
        Call<DataResponse> call = service.getDataSearchedByPage(SearchUrlHelper.getSearchUrlByPage(pageIndex));
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                viewCallback.loadedData(response.body());
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                viewCallback.displayError(t.getMessage());
            }
        });
    }



}
