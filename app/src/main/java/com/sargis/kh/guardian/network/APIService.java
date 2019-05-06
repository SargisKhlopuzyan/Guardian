package com.sargis.kh.guardian.network;


import com.sargis.kh.guardian.models.DataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface APIService {
    @GET Call<DataResponse> getDataSearchedByPage(@Url String url);
}