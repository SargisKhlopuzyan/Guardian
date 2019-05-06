package com.sargis.kh.guardian.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Results implements Serializable {

    @SerializedName("id") public String id;

    @SerializedName("sectionId") public String sectionId;

    @SerializedName("sectionName") public String sectionName;

    @SerializedName("webPublicationDate") public String webPublicationDate;

    @SerializedName("webTitle") public String webTitle;

    @SerializedName("webUrl") public String webUrl;

    @SerializedName("apiUrl") public String apiUrl;

}