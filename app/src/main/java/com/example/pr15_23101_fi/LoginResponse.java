package com.example.pr15_23101_fi;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("nickName")
    private String nickName;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("token")
    private String token;

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getNickName() { return nickName; }
    public String getAvatar() { return avatar; }
    public String getToken() { return token; }
}