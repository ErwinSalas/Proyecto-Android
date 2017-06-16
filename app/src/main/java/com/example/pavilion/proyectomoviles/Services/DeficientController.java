package com.example.pavilion.proyectomoviles.Services;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pavilion on 18/5/2017.
 */
public class DeficientController {
    private DeficientServiceFactory service;
    private final Retrofit retrofit;

    private String API_ROOT = " https://sheltered-headland-93521.herokuapp.com";


    public DeficientController() {
        this.retrofit = new Retrofit.Builder().baseUrl(API_ROOT).addConverterFactory(GsonConverterFactory.create()).build();
        this.service = retrofit.create(DeficientServiceFactory.class);
    }

    public DeficientServiceFactory getService() {
        return service;
    }

    public void create() {

        Deficient deficient = Deficient.getInstance();
        Call<Deficient> call = this.service.setDeficient(deficient);
        call.enqueue(new Callback<Deficient>() {
            @Override
            public void onResponse(Call<Deficient> call, Response<Deficient> response) {

            }

            @Override
            public void onFailure(Call<Deficient> call, Throwable t) {

            }
        });
    }

    public void obtain() {

        List<Deficient> deficient = new ArrayList<>();
        Call<List<Deficient>> call = this.service.getDeficients();
        call.enqueue(new Callback<List<Deficient>>() {
            @Override
            public void onResponse(Call<List<Deficient>> call, Response<List<Deficient>> response) {
                List<Deficient> deficient = response.body();

            }

            @Override
            public void onFailure(Call<List<Deficient>> call, Throwable t) {

            }
        });
    }
}
