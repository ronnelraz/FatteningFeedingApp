package com.ronnelrazo.fatteningfeedingapp.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface APIInterface {



    @POST("PC_FAT_USER")
    Call<Object> PC_FAT_USER();

    @POST("PC_FAT_USER_AUTHORIZE")
    Call<Object> PC_FAT_USER_AUTHORIZE();

    @FormUrlEncoded
    @POST("FR_MS_TRN_FEED")
    Call<Object> FR_MS_TRN_FEED(
            @Field("org_code") String org_code,
            @Field("farm") String farm
    );

    @FormUrlEncoded
    @POST("FR_FARM_ORG")
    Call<Object> FR_FARM_ORG(
            @Field("org_code") String org_code
    );


    @FormUrlEncoded
    @POST("FR_MS_SWINE_MATERIAL")
    Call<Object> FR_MS_SWINE_MATERIAL(
            @Field("org_code") String org_code,
            @Field("farm") String farm
    );

    @FormUrlEncoded
    @POST("FR_MAS_CLOSE_PERIOD")
    Call<Object> FR_MAS_CLOSE_PERIOD(
            @Field("org_code") String org_code,
            @Field("farm") String farm
    );

    @FormUrlEncoded
    @POST("FDSTOCKBALANCE")
    Call<Object> FDSTOCKBALANCE(
            @Field("org_code") String org_code
    );

    @FormUrlEncoded
    @POST("FR_MAS_FARM_INFORMATION")
    Call<Object> FR_MAS_FARM_INFORMATION(
            @Field("org_code") String org_code
    );

//    @GET("/group/{id}/users")
//    List<User> groupList(@Path("id") int groupId, @QueryMap Map<String, String> options);

    @GET("feed_stock_balance{id}.json")
    Call<Object> get_feed_stock_balance(@Path("id") String id);

    @GET("fr_farm_org{id}.json")
    Call<Object> get_fr_farm_org(@Path("id") String id);

    @GET("fr_mas_close_period{id}.json")
    Call<Object> get_fr_mas_close_period(@Path("id") String id);

    @GET("fr_mas_farm_information{id}.json")
    Call<Object> get_fr_mas_farm_information(@Path("id") String id);

    @GET("fr_mas_swine_material{id}.json")
    Call<Object> get_fr_mas_swine_material(@Path("id") String id);

    @GET("fr_mas_trn_feed{id}.json")
    Call<Object> get_fr_mas_trn_feed(@Path("id") String id);



//    FR_MAS_CLOSE_PERIOD
//    FDSTOCKBALANCE

}
