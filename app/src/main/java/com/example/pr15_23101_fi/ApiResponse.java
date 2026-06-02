package com.example.pr15_23101_fi;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse<T> {
    @SerializedName("success")
    public boolean success;

    @SerializedName("data")
    public List<T> data;

    public boolean isSuccess() { return success; }
    public List<T> getData() { return data; }
}