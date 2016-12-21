package com.rw.velocityapp;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.rw.velocity.Velocity;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
{
    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        imageView = (ImageView)findViewById(R.id.imageView);

        Velocity.initialize(3);
        Velocity.getSettings().setTimeout(6000);
        Velocity.getSettings().setReadTimeout(10000);

        String m3 = "https://media.caranddriver.com/images/media/331369/m-is-for-mega-2015-bmw-m3-pricing-surfaces-photo-583888-s-450x274.jpg";
        String randomImage = "http://lorempixel.com/400/200/abstract";
        String file = "http://mirror.internode.on.net/pub/test/5meg.test1";
        String pdf = "http://www.flar.net/uploads/default/calendar/99f3a2304c1754aecffab145a5c80b98.pdf";




        downloadRequest(pdf);

    }

    private void downloadRequest(String url)
    {
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "pdfsample.pdf";

        Log.d("IMG", "target filename: " + filepath);

        Velocity.download(url).setDownloadFile(filepath).connect(new Velocity.ResponseListener()
        {
            @Override
            public void onVelocitySuccess(Velocity.Response response)
            {
                Log.d("IMG", "response: "+response.body);
                textView.setText(response.body);
            }

            @Override
            public void onVelocityFailed(Velocity.Response error)
            {
                Log.d("IMG", "error: "+error.body);
                textView.setText(error.status + ": "+ error.body);
            }
        });
    }

    private void textRequest(String url)
    {
        Velocity.load(url).connect(new Velocity.ResponseListener()
        {
            @Override
            public void onVelocitySuccess(Velocity.Response response)
            {
                //Log.d("IMG", "response: " + response.body);
                textView.setText(response.body);
                if(response.image != null)
                    imageView.setImageBitmap(response.image);
            }

            @Override
            public void onVelocityFailed(Velocity.Response error)
            {
                textView.setText(error.body);
                Log.d("IMG", "response: " + error.body);
            }
        });
    }
}
