package com.sargis.kh.guardian.network.calls;

import com.sargis.kh.guardian.models.DataResponse;
import com.sargis.kh.guardian.network.APIService;
import com.sargis.kh.guardian.network.RetrofitClientInstance;
import com.sargis.kh.guardian.network.SearchUrlHelper;

import retrofit2.Call;
import retrofit2.Callback;

public class AsynchronousRequests {

    public static void getDataSearchedByPage(GetDataCallback<DataResponse> dataCallback, int page) {
        APIService service = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);
        Call<DataResponse> call = service.getDataSearchedByPage(SearchUrlHelper.getSearchUrlByPage(page));
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, retrofit2.Response<DataResponse> dataResponse) {
                if (dataResponse.isSuccessful()) {
                    dataCallback.onSuccess(dataResponse.body());
                } else {
                    dataCallback.onError(dataResponse.code(), dataResponse.errorBody());
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                dataCallback.onFailure(t);
            }
        });
    }

    public static void getDataSearchedByFromDate(GetDataCallback<DataResponse> dataCallback, String fromDate) {
        APIService service = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);
        Call<DataResponse> call = service.getDataSearchedByPage(SearchUrlHelper.getSearchUrlByFromDate(fromDate));
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, retrofit2.Response<DataResponse> dataResponse) {
                if (dataResponse.isSuccessful()) {
                    dataCallback.onSuccess(dataResponse.body());
                } else {
                    dataCallback.onError(dataResponse.code(), dataResponse.errorBody());
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                dataCallback.onFailure(t);
            }
        });
    }

}