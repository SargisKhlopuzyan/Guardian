package com.sargis.kh.guardian;

import com.sargis.kh.guardian.models.DataResponse;

public interface HomePageContract {

    interface View {
        void displayError(String errorMessage);
        void loadedData(DataResponse dataResponse);
    }

    interface Presenter {
        void getDataSearchedByPage(int pageIndex);
    }

}