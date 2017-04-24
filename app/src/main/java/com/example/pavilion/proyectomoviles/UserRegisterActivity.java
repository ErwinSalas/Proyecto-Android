package com.example.pavilion.proyectomoviles;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pavilion.proyectomoviles.DataBase.BDHelper;

public class UserRegisterActivity extends AppCompatActivity {

    BDHelper MyDB;
    EditText etEmail, etName, etLastName, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        etName = (EditText) findViewById(R.id.etName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        MyDB = new BDHelper(this);

        Button property = (Button) findViewById(R.id.add);
        property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarUsuario();
                Toast.makeText(getApplicationContext(), "GUARDADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void insertarUsuario() {
        String email = etEmail.getText().toString();
        String name = etName.getText().toString();
        String lastName = etLastName.getText().toString();
        String password = etPassword.getText().toString();
        MyDB.insertUser(email, name, lastName, password);

        Cursor loguedIn = MyDB.getUserData(email, password);
        if(loguedIn.moveToFirst()) {
            if (!loguedIn.isClosed()) {
                loguedIn.close();
            }
            else {
                Toast.makeText(UserRegisterActivity.this,"Welcome ",Toast.LENGTH_LONG).show();
                etEmail.setText(loguedIn.getString(0) + " , " + loguedIn.getString(1));
            }
        }else{
            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

