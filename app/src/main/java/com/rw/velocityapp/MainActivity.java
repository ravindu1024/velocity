package com.rw.velocityapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rw.velocity.Velocity;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Velocity.initialize(3);

        Velocity.load("ddd").withHeaders(null).connect(null);

    }
}
