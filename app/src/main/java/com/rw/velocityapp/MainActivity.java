package com.rw.velocityapp;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.rw.velocity.Velocity;
import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity
{
    private TextView textView;
    private ImageView imageView;
    private ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("downloading file...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);


        downloadRequest(file);

    }

    private void downloadRequest(String url)
    {
        progressDialog.show();
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "sample2.bin";

        File f = new File(filepath);
        Log.d("IMG", "permissions: read: " + f.canRead()+", write: "+f.canWrite());
        Log.d("IMG", "target filename: " + filepath);

        Velocity.download(url).setDownloadFile(filepath)
                .withProgressListener(new Velocity.ProgressListener()
                {
                    @Override
                    public void onFileProgress(int percentage)
                    {
                        //Log.d("IMG", "progress: " + percentage);
                        progressDialog.setProgress(percentage);
                    }
                })
                .connect(new Velocity.ResponseListener()
        {
            @Override
            public void onVelocitySuccess(Velocity.Response response)
            {
                Log.d("IMG", "response: "+response.body);
                textView.setText(response.body);
                progressDialog.dismiss();
            }

            @Override
            public void onVelocityFailed(Velocity.Response error)
            {
                Log.d("IMG", "error: "+error.body);
                textView.setText(error.status + ": "+ error.body);
                progressDialog.dismiss();
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
