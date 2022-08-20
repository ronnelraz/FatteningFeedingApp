package com.ronnelrazo.fatteningfeedingapp.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface APIInterface {


//    @FormUrlEncoded
//    @POST("login")
//    Call<Object> loginAPI(
//            @Field("username") String username,
//            @Field("password") String password
//    );


    @POST("PC_FAT_USER")
    Call<Object> PC_FAT_USER();

    @POST("PC_FAT_USER_AUTHORIZE")
    Call<Object> PC_FAT_USER_AUTHORIZE();


}
