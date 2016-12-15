package com.rw.velocityapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.rw.velocity.Velocity;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Velocity.initialize(3);

        Velocity.load("ff").withData(null);

        Velocity.load("http://www.google.com").connect(new Velocity.DataCallback()
        {
            @Override
            public void onVelocitySuccess(Velocity.Data response)
            {
                Log.d("IMG", "response: " + response.body);
            }

            @Override
            public void onVelocityFailed(Velocity.Data error)
            {
                Log.d("IMG", "response: " + error.body);
            }
        });


    }
}
