package com.example.pavilion.proyectomoviles.Services;



import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pavilion on 18/5/2017.
 */
public class DeficientController {
    private DeficientServiceFactory service;
    private String API_ROOT = " https://sheltered-headland-93521.herokuapp.com";

    public DeficientController() {
//        RestAdapter.Builder builder = new RestAdapter.Builder()
//                .setEndpoint(API_ROOT)
//                .setClient(new OkClient(new OkHttpClient()));
//        wscServicio = builder.build().create(WebServiceConn.class);
//
//    }

    private ArrayList<DeficientModel> index(){
        ArrayList<DeficientModel> res;
        service.getDeficients(new Callback<ArrayList<DeficientModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DeficientModel>> call, Response<ArrayList<DeficientModel>> response) {

            }

            @Override
            public void onFailure(Call<ArrayList<DeficientModel>> call, Throwable throwable) {

            }
        });
    }








}
