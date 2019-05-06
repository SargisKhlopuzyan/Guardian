package com.sargis.kh.guardian.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {

    @SerializedName("status") public String status;

    @SerializedName("userTier") public String userTier;

    @SerializedName("total") public Integer total;

    @SerializedName("startIndex") public Integer startIndex;

    @SerializedName("pageSize") public Integer pageSize;

    @SerializedName("currentPage") public Integer currentPage;

    @SerializedName("pages") public Integer pages;

    @SerializedName("orderBy") public String orderBy;

    @SerializedName("results") public List<Results> results;

}