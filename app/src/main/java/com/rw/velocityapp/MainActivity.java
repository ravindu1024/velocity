package com.rw.velocityapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rw.velocity.Logger;
import com.rw.velocity.OAuthBuilder;
import com.rw.velocity.Velocity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    private TextView textView;
    private ImageView imageView;
    private ProgressDialog progressDialog;


    private String m3 = "https://media.caranddriver.com/images/media/331369/m-is-for-mega-2015-bmw-m3-pricing-surfaces-photo-583888-s-450x274.jpg";
    private String randomImage = "http://lorempixel.com/400/200/abstract";
    private String file = "http://mirror.internode.on.net/pub/test/5meg.test1";
    private String pdf = "http://www.flar.net/uploads/default/calendar/99f3a2304c1754aecffab145a5c80b98.pdf";
    private String textUrl = "https://www.example.com";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        Velocity.initialize(3);
        Velocity.getSettings().setTimeout(6000);
        Velocity.getSettings().setReadTimeout(10000);
        Velocity.getSettings().setMultipartBoundary("----WebKitFormBoundaryQHJL2hsKnlU26Mm3");
        Velocity.getSettings().setMaxTransferBufferSize(1024);
        //Velocity.getSettings().setGloballyMocked(true);
        Velocity.getSettings().setMaxRedirects(10);
        Velocity.getSettings().setLoggingEnabled(true);
        Velocity.getSettings().setCustomLogger(new CustomLogger("Velocity"));
        //Velocity.getSettings().setResponseCompressionEnabled(true);




        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("downloading file...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //textRequest("http://139.162.28.197:8080/listing/search");
                downloadRequest("http://139.162.28.197:8080/listing/image/a26d7290-ab75-4291-b86b-d39e43eb2eed");
                //textRequest("http://139.162.28.197:8080/listing/image/a26d7290-ab75-4291-b86b-d39e43eb2eed");
            }
        });

//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                Velocity.Response r = Velocity.get(textUrl).connectBlocking();
//                Log.d("IMG", "response: " + r.body);
//            }
//        }).start();
    }

    class Survey
    {
        String guest_id = "492";
        String survey_id = "1";


    }


    private void doMultiRequest()
    {
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "sample2.bin";


        Velocity.get(getTestUrl(1)).queue(0);
        Velocity.download(file).setDownloadFile(filepath).queue(1);
        Velocity.get(randomImage).queue(2);



        Velocity.executeQueue(new Velocity.MultiResponseListener()
        {
            @Override
            public void onVelocityMultiResponseSuccess(HashMap<Integer, Velocity.Response> responseMap)
            {
                for(int i : responseMap.keySet())
                {
                    Velocity.Response r = responseMap.get(i);

                    Log.d("IMG", "id: " + i + ", response: "+r.body);

                    if(i == 2)
                        imageView.setImageBitmap(r.image);
                }
            }

            @Override
            public void onVelocityMultiResponseError(HashMap<Integer, Velocity.Response> errorMap)
            {
                Log.d("IMG", "multirequest failed");
            }
        });
    }

    private String getTestUrl(int i)
    {
        return "https://jsonplaceholder.typicode.com/posts/" + i;
    }


    private void downloadRequest(String url)
    {
        progressDialog.setMessage("downloading file");
        progressDialog.show();
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "sample2.png";

        Log.d("IMG", "target filename: " + filepath);

        Velocity.download(url)
                .setDownloadFile(filepath)
                .withResponseCompression()
                .addToDownloadsFolder(this, "download test", "test file")
                .withProgressListener(new Velocity.ProgressListener()
                {
                    @Override
                    public void onFileProgress(int percentage)
                    {

                        progressDialog.setProgress(percentage);
                    }
                })
                .connect(new Velocity.ResponseListener()
                {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response)
                    {
                        //Log.d("IMG", "response: " + response.body);
                        textView.setText(response.body);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error)
                    {
                        Log.d("IMG", "error: " + error.body);
                        textView.setText(error.responseCode + ": " + error.body);
                        progressDialog.dismiss();
                    }
                });
    }

    private void textRequest(String url)
    {
        Velocity
                .get(url)
                .withHeader("postcode", "3020")
                .withResponseCompression()
                .connect(new Velocity.ResponseListener()
        {
            @Override
            public void onVelocitySuccess(Velocity.Response response)
            {
                Log.d("IMG", "response: " + response.body);
                //textView.setText(response.body);
                if (response.image != null)
                    imageView.setImageBitmap(response.image);

                Log.d("IMG", "response toString:");
                //Log.d("IMG", response.toString());
            }

            @Override
            public void onVelocityFailed(Velocity.Response error)
            {
                textView.setText(error.body);
                Log.d("IMG", "response: " + error.body);
            }
        });
    }

    class CustomLogger extends Logger
    {

        CustomLogger(String tag)
        {
            super(tag);
        }

        @Override
        protected void logConnectionError(@NonNull Velocity.Response error, @Nullable Exception systemError)
        {
            //super.logConnectionError(error, systemError);
            Log.d("IMG", "error: "+error.body);
        }
    }
}
