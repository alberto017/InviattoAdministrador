package com.example.inviattoadministrador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.database.DatabaseReference;

public class SplashScreen extends AppCompatActivity {

    //Declaracion de variables
    private DatabaseReference databaseReference = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent Acceso = new Intent(SplashScreen.this, Inicio.class);
                startActivity(Acceso);
                finish();
            }
        }, 2000);
    }//onCreate
}//SplashScreen