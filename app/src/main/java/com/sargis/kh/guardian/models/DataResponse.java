package com.sargis.kh.guardian.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataResponse implements Serializable {

    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }
}
