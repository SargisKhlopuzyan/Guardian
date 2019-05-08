package com.sargis.kh.guardian;

import com.sargis.kh.guardian.models.DataResponse;

public interface HomePageContract {

    interface View {
        void displayError(String errorMessage);
        void dataLoadedByPage(DataResponse dataResponse);
        void dataLoadedByFromDate(DataResponse dataResponse);
    }

    interface Presenter {
        void getDataSearchedByPage(int page);
        void getDataSearchedByFromDate(String fromDate);
    }

}