package com.sargis.kh.guardian.network;

import com.sargis.kh.guardian.presenters.DataController;
import com.sargis.kh.guardian.utils.Constants;

public class SearchUrlHelper {

    public static String getSearchUrlByPage(int page) {
        String url = "https://content.guardianapis.com/search?order-by=newest&page=" + page + "&page-size=" + DataController.getInstance().getPageSize() + "&show-fields=thumbnail,body&api-key=" + Constants.API_KEY;
        return url;
    }

    //2019-05-08T12:58:11Z
    public static String getSearchUrlByFromDate(String fromDate) {
        String url = "https://content.guardianapis.com/search?from-date=" + fromDate + "&show-fields=thumbnail,body&api-key=" + Constants.API_KEY;
        return url;
    }

}