package com.sargis.kh.guardian.presenters;

import android.content.SharedPreferences;
import android.util.Log;

import com.sargis.kh.guardian.GuardianApplication;
import com.sargis.kh.guardian.HomePageContract;
import com.sargis.kh.guardian.models.DataResponse;
import com.sargis.kh.guardian.network.calls.Data;
import com.sargis.kh.guardian.network.calls.GetDataCallback;
import com.sargis.kh.guardian.utils.Constants;

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

                    SharedPreferences pref = GuardianApplication.getContext().getSharedPreferences(Constants.SharedPreferences.NAME,0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    String webPublicationDate = dataResponse.getResponse().results.get(0).webPublicationDate;
                    editor.putString(Constants.SharedPreferences.LAST_WEB_PUBLICATION_DATE, webPublicationDate);
                    editor.commit();
                    Log.e("LOG_TAG", "webPublicationDate: " + webPublicationDate);
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

    }

}
