package com.example.pavilion.proyectomoviles.Services;


import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
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
    /*Call<estudiante> call = connectionManager.getMainInterface().getStudent(carnetTextView.getText().toString(),
            Integer.parseInt(mPasswordView.getText().toString()));
                call.enqueue(new Callback<odontologia.proyectoodontologia.estudiante>() {
        @Override
        public void onResponse(Call<estudiante> call, Response<estudiante> response) {
            Student student = Student.getInstance();
            student.FillInformation(response.body().getCarne(), response.body().getPin(), String.valueOf(response.body().getBecado()),
                    response.body().getNombre(), response.body().getApe1(), response.body().getApe2(),
                    response.body().getCarrera(), response.body().getEstadoCivil(), response.body().getCarneCCSS(),
                    response.body().getFechaNacimiento(), response.body().getCedula(), response.body().getTelefono());
            Toast.makeText(LoginActivity.this, "Bienvenido " + student.getNombre(), Toast.LENGTH_SHORT).show();
            startActivity(MainActivityIntent);
        }

        @Override
        public void onFailure(Call<estudiante> call, Throwable t) {
            Toast.makeText(LoginActivity.this, "Error de inicio de sesión", Toast.LENGTH_SHORT).show();
        }
    });*/

}
