package com.example.pavilion.proyectomoviles;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.pavilion.proyectomoviles.Services.DeficientController;
import com.example.pavilion.proyectomoviles.Services.Deficient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddDiseaseFragment extends Fragment {
    View rootView;
    private static final String TAG = "OCVSample::Activity";
    public static String nombre, descripcion;
    private DeficientController deficientController = new DeficientController();

    public AddDiseaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_add_disease, container, false);





        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        Log.e(TAG, "*************//////+++++CARAMBAABABABABABABABAB++++/////7**************" );
        builder.setView(inflater.inflate(R.layout.fragment_add_disease, container, false))
                // Add action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog dialogObj =Dialog.class.cast(dialog);
                        EditText input_nombre=(EditText) dialogObj.findViewById(R.id.diseases);
                        EditText input_descripcion=(EditText) dialogObj.findViewById(R.id.description);
                        final String color = getArguments().getString("color");
                        Log.e(TAG, "*************//////++++++00000000000++++++/////7**************" );
                        String colorSemiProcesado = withoutParenthesis(color);
                        Log.e(TAG, colorSemiProcesado);
                        Log.e(TAG, "*************//////+++++++11111111111+++++/////7**************" );
                        String[] separated = colorSemiProcesado.split(",");
                        Log.e(TAG, separated.toString() );
                        double r = Double.parseDouble(separated[0]);
                        double g = Double.parseDouble(separated[1]);
                        double b = Double.parseDouble(separated[2]);
                        double a = Double.parseDouble(separated[3]);
                        Log.e(TAG, "*************//////+++++++2222222222+++++/////7**************" );
                        nombre = input_nombre.getText().toString();
                        input_nombre.setText("mierda");
                        Log.e(TAG, input_nombre.getText() + "AQUI VA");
                        Log.e(TAG, nombre + "AQUI VA");
                        descripcion = input_descripcion.getText().toString();
                        Deficient deficient = Deficient.getInstance();
                        Log.e(TAG, deficient.getDescription() + "+++++++++++++-----...,,,,.,.++++++++++++");
                        Log.e(TAG, nombre + "AQUI VA NOMBRE");
                        deficient.FillInformation(0, nombre, descripcion, r, g, b, a);
                        Log.e(TAG, "*************//////+++++++333333333333333333333+++++/////7**************" );
                        Log.e(TAG, deficient.getName() );

                        Call<Deficient> call = deficientController.getService().setDeficient(deficient);
                        call.enqueue(new Callback<Deficient>() {
                            @Override
                            public void onResponse(Call<Deficient> call, Response<Deficient> response) {
                                Log.e(TAG, "*************//////+++++444444444444444+++++++/////7**************" );

                            }

                            @Override
                            public void onFailure(Call<Deficient> call, Throwable t) {
                                Log.e(TAG, "*************//////+++++5555555555555+++++++/////7**************" );
                            }
                        });

                    }
                });
        builder.show();
        return rootView;

    }

    private void closefragmentA(AddDiseaseFragment addDiseaseFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(addDiseaseFragment);
        fragmentTransaction.commit();

    }

    public String withoutParenthesis(String currentString) {
        int position = 0;
        String cadena = "";
        while (position < currentString.length()) {
            if (currentString.charAt(position) != '[' && currentString.charAt(position) != ']') {
                cadena += currentString.charAt(position);
            }
            position++;
        }
        return cadena;
    }

}
