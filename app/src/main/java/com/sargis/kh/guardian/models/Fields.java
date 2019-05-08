package com.sargis.kh.guardian.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Fields implements Serializable {
    @SerializedName("thumbnail") public String thumbnail;
    @SerializedName("body") public String body;
}
