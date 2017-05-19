package com.example.pavilion.proyectomoviles.Services;

import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Pavilion on 18/5/2017.
 */
public interface DeficientServiceFactory {

    @POST("/deficients")
    void createDeficient(@Body DeficientModel post, Callback<Boolean> postCallback);

    @DELETE("/deficients")
    void deleteDeficient(@Query("id") String nombre, Callback<Boolean> postCallback);

    @GET("/deficients")
    void getDeficients(Callback<ArrayList<DeficientModel>> callback);


}
