package in.iamkelv.balances.model;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;

public interface WisePayService {

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/auth")
    void authUser(@Field("username") String username, @Field("password") String password, Callback<?> cb);

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/balances")
    void checkBalances(@Field("username") String username, @Field("password") String password, Callback<Balances> cb);

}