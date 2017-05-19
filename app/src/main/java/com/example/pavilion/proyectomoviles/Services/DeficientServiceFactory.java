package com.example.pavilion.proyectomoviles.Services;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Pavilion on 18/5/2017.
 */
public interface DeficientServiceFactory {

    @GET("/deficients")
    Call<List<DeficientModel>> getDeficients();

    @POST("/deficients")
    Call<DeficientModel> setDeficient(@Body DeficientModel model);
}
