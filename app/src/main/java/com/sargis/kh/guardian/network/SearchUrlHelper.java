package com.sargis.kh.guardian.network;

import com.sargis.kh.guardian.utils.Constants;

public class SearchUrlHelper {

    public static String getSearchUrlByPage(int pageNumber) {
        // In every try the application loads 10 pages
        pageNumber = pageNumber * 10 - 9;

        String url = "https://content.guardianapis.com/search?order-by=newest&page=" + pageNumber + "&page-size=10&show-fields=thumbnail,body&api-key=" + Constants.API_KEY;
        return url;
    }

    //2019-05-08T12:58:11Z
    public static String getSearchUrlByDateTime(String fromDatetime) {
        String url = "https://content.guardianapis.com/search?from-date=" + fromDatetime + "&show-fields=thumbnail,body&api-key=" + Constants.API_KEY;
        return url;
    }

}