package com.example.pavilion.proyectomoviles.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Pavilion on 18/5/2017.
 */
public interface DeficientServiceFactory {

    @GET("/deficients")
    Call<List<Deficient>> getDeficients();

    @POST("/deficients")
    Call<Deficient> setDeficient(@Body Deficient deficient);
}
