package com.sargis.kh.guardian.models;

import com.google.gson.annotations.SerializedName;

public class DataResponse {

    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }
}
