package com.sargis.kh.guardian.presenters;

import com.sargis.kh.guardian.HomePageContract;
import com.sargis.kh.guardian.helpers.HelperSharedPreferences;
import com.sargis.kh.guardian.models.DataResponse;
import com.sargis.kh.guardian.network.calls.Data;
import com.sargis.kh.guardian.network.calls.GetDataCallback;

import okhttp3.ResponseBody;

public class HomePagePresenter implements HomePageContract.Presenter {

    HomePageContract.View viewCallback;

    public HomePagePresenter(HomePageContract.View viewCallback) {
        this.viewCallback = viewCallback;
    }

    @Override
    public void getDataSearchedByPage(int page) {

        Data.getDataSearchedByPage(new GetDataCallback<DataResponse>() {
            @Override
            public void onSuccess(DataResponse dataResponse) {
                if (page == 1
                        && dataResponse != null
                        && dataResponse.getResponse() != null
                        && dataResponse.getResponse().results != null
                        && dataResponse.getResponse().results.size() > 0) {

                    String lastWebPublicationDate = dataResponse.getResponse().results.get(0).webPublicationDate;
                    HelperSharedPreferences.setLastWebPublicationDateIncreasedByOneSecond(lastWebPublicationDate);
                }

                viewCallback.dataLoadedByPage(dataResponse);
            }

            @Override
            public void onError(int errorCode, ResponseBody errorResponse) {
                viewCallback.displayError("error code : " + errorCode);
            }

            @Override
            public void onFailure(Throwable failure) {
                viewCallback.displayError(failure.getMessage());
            }

        }, page);
    }

    @Override
    public void getDataSearchedByFromDate(String fromDate) {
        Data.getDataSearchedByFromDate(new GetDataCallback<DataResponse>() {
            @Override
            public void onSuccess(DataResponse dataResponse) {
                if (dataResponse != null
                        &&  dataResponse.getResponse() != null
                        && dataResponse.getResponse().results != null
                        && dataResponse.getResponse().results.size() > 0) {

                    if (dataResponse.getResponse().results.size() > 0) {
                        String lastWebPublicationDate = dataResponse.getResponse().results.get(0).webPublicationDate;
                        HelperSharedPreferences.setLastWebPublicationDateIncreasedByOneSecond(lastWebPublicationDate);
                    }
                }
                viewCallback.dataLoadedByFromDate(dataResponse);
            }

            @Override
            public void onError(int errorCode, ResponseBody errorResponse) {
                viewCallback.displayError("error code : " + errorCode);
            }

            @Override
            public void onFailure(Throwable failure) {
                viewCallback.displayError(failure.getMessage());
            }
        }, fromDate);


    }

}
