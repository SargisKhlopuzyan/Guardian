package com.sargis.kh.guardian.network.calls;

import com.sargis.kh.guardian.models.DataResponse;

public class Data {

    public static void getDataSearchedByPage(GetDataCallback<DataResponse> dataCallback, int page) {
        AsynchronousRequests.getDataSearchedByPage(dataCallback, page);
    }

    public static void getDataSearchedByFromDate(GetDataCallback<DataResponse> dataCallback, String fromDate) {
        AsynchronousRequests.getDataSearchedByFromDate(dataCallback, fromDate);
    }

}