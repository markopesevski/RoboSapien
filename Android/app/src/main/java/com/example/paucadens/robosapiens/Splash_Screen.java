package com.example.paucadens.robosapiens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent mainIntent = new Intent(this, Connexio.class);
        startActivity(mainIntent);
        finish();
    }
}
