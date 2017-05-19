package com.example.pavilion.proyectomoviles.Services;


import android.widget.Toast;

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

    public String withoutParenthesis(String currentString) {
        int position = 0;
        String cadena = "";
        while (position < currentString.length()) {
            if (currentString.charAt(position) == '[' || currentString.charAt(position) == ']') {
                position++;
            } else {
                cadena += currentString.charAt(position);
            }
        }
        return cadena;
    }

    public void create(String currentString, String name, String description) {

        currentString = withoutParenthesis(currentString);

        String[] separated = currentString.split(",");
        Float r = Float.parseFloat(separated[0]);
        Float g = Float.parseFloat(separated[1]);
        Float b = Float.parseFloat(separated[2]);
        Float a = Float.parseFloat(separated[3]);

        DeficientModel deficientModel = new DeficientModel(name, description, r, g, b, a);
        Call<DeficientModel> call = this.service.setDeficient(deficientModel);
        call.enqueue(new Callback<DeficientModel>() {
            @Override
            public void onResponse(Call<DeficientModel> call, Response<DeficientModel> response) {

            }

            @Override
            public void onFailure(Call<DeficientModel> call, Throwable t) {

            }
        });
    }

    public void obtain() {

        List<DeficientModel> deficientModel = new ArrayList<>();
        Call<List<DeficientModel>> call = this.service.getDeficients();
        call.enqueue(new Callback<List<DeficientModel>>() {
            @Override
            public void onResponse(Call<List<DeficientModel>> call, Response<List<DeficientModel>> response) {
                List<DeficientModel> deficientModel = response.body();

            }

            @Override
            public void onFailure(Call<List<DeficientModel>> call, Throwable t) {

            }
        });
    }
}
