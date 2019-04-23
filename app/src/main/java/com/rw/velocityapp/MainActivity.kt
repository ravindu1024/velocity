package com.rw.velocityapp

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.rw.velocity.Logger
import com.rw.velocity.Velocity

class MainActivity : AppCompatActivity() {

    private var textView: TextView? = null
    private var imageView: ImageView? = null
    private var progressDialog: ProgressDialog? = null


    private val m3 = "https://www.formula1.com/content/dam/fom-website/sutton/2018/Abu%20Dhabi/Sunday/dcd1825no2013.jpg.transform/9col/image.jpg"
    private val randomImage = "http://lorempixel.com/400/200/abstract"
    private val file = "http://mirror.internode.on.net/pub/test/5meg.test1"
    private val pdf = "http://www.flar.net/uploads/default/calendar/99f3a2304c1754aecffab145a5c80b98.pdf"
    private val textUrl = "https://www.example.com"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById<View>(R.id.textView) as TextView
        imageView = findViewById<View>(R.id.imageView) as ImageView

        Velocity.initialize(3)
        Velocity.getSettings().setTimeout(6000)
        Velocity.getSettings().setReadTimeout(10000)
        Velocity.getSettings().setMultipartBoundary("----WebKitFormBoundaryQHJL2hsKnlU26Mm3")
        Velocity.getSettings().setMaxTransferBufferSize(1024)
        //Velocity.getSettings().setGloballyMocked(true);
        Velocity.getSettings().setMaxRedirects(10)
        Velocity.getSettings().setLoggingEnabled(true)
        Velocity.getSettings().setCustomLogger(object : Logger("fff") {
            override fun logConnectionError(error: Velocity.Response, systemError: Exception?) {
                Log.d("Velocity", "got custom log")
            }
        })
        //Velocity.getSettings().setResponseCompressionEnabled(true);


        progressDialog = ProgressDialog(this)
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setMessage("downloading file...")
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog!!.setCancelable(false)

        imageView!!.setOnClickListener {
            textRequest(m3)
            //downloadRequest("http://139.162.28.197:8080/listing/image/a26d7290-ab75-4291-b86b-d39e43eb2eed");
            //textRequest("http://139.162.28.197:8080/listing/image/a26d7290-ab75-4291-b86b-d39e43eb2eed");
        }

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

    internal inner class Survey {
        var guest_id = "492"
        var survey_id = "1"


    }


    private fun doMultiRequest() {
        val filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + "sample2.bin"


        Velocity.get(getTestUrl(1)).queue(0)
        Velocity.download(file).setDownloadFile(filepath).queue(1)
        Velocity.get(randomImage).queue(2)



        Velocity.executeQueue { responseMap ->
            for (i in responseMap.keys) {
                val r = responseMap[i]

                if (r!!.isSuccess) {

                    Log.d("IMG", "id: " + i + ", response: " + r.body)

                    if (i == 2)
                        imageView!!.setImageBitmap(r.image)
                } else {
                    Log.d("IMG", "multirequest failed")
                }
            }
        }
    }

    private fun getTestUrl(i: Int): String {
        return "https://jsonplaceholder.typicode.com/posts/$i"
    }


    private fun downloadRequest(url: String) {
        progressDialog!!.setMessage("downloading file")
        progressDialog!!.show()
        val filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + "sample2.png"

        Log.d("IMG", "target filename: $filepath")

        Velocity.download(url)
                .setDownloadFile(filepath)
                .withResponseCompression()
                .addToDownloadsFolder(this, "download test", "test file")
                .withProgressListener { percentage -> progressDialog!!.progress = percentage }
                .connect { response ->
                    if (response.isSuccess) {
                        //Log.d("IMG", "response: " + response.body);
                        textView!!.text = response.body
                        progressDialog!!.dismiss()
                    } else {
                        Log.d("IMG", "error: " + response.body)
                        textView!!.text = response.responseCode.toString() + ": " + response.body
                        progressDialog!!.dismiss()
                    }
                }
    }

    private fun textRequest(url: String) {
        Velocity
                .get(url)
                .withHeader("postcode", "3020")
                .withResponseCompression()
                .connect { response ->
                    if (response.isSuccess) {
                        Log.d("IMG", "response: " + response.body)
                        //textView.setText(response.body);
                        if (response.image != null)
                            imageView!!.setImageBitmap(response.image)

                        Log.d("IMG", "response toString:")
                        //Log.d("IMG", response.toString());
                    } else {
                        textView!!.text = response.body
                        Log.d("IMG", "response: " + response.body)
                    }
                }
    }

    internal inner class CustomLogger(tag: String) : Logger(tag) {

        override fun logConnectionError(error: Velocity.Response, systemError: Exception?) {
            //super.logConnectionError(error, systemError);
            Log.d("IMG", "error: " + error.body)
        }
    }
}
