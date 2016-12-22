package com.rw.velocity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 21/12/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */

class UploadRequest extends Request
{
    UploadRequest(RequestBuilder builder)
    {
        super(builder);
    }


    @Override
    void setupRequestHeaders()
    {
        super.setupRequestHeaders();

        mConnection.setRequestProperty("Connection", "Keep-Alive");
        mConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + Velocity.Settings.BOUNDARY);
    }


    @Override
    void setupRequestBody() throws IOException
    {
        int bytesRead;
        int bytesAvailable;
        int bufferSize;
        byte[] buffer;
        int prevPercentage = 0;
        int maxBufferSize = Velocity.Settings.MAX_BUFFER;
        InputStream fileInputStream;


        mConnection.setDoInput(true);
        mConnection.setDoOutput(true);
        mConnection.setUseCaches(false);


        if (mBuilder.uploadStream != null)
            fileInputStream = mBuilder.uploadStream;
        else
            fileInputStream = new FileInputStream(new File(mBuilder.uploadFile));


        DataOutputStream dos = new DataOutputStream(mConnection.getOutputStream());

        dos.writeBytes(Velocity.Settings.TWOHYPHENS + Velocity.Settings.BOUNDARY + Velocity.Settings.LINEEND);
        dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"uploadedfile\"" + Velocity.Settings.LINEEND);
        dos.writeBytes(Velocity.Settings.LINEEND);

        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        int totalToWrite = bytesAvailable;
        int totalWritten = 0;
        while (bytesRead > 0)
        {
            dos.write(buffer, 0, bufferSize);

            totalWritten += bufferSize;

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            if (mBuilder.progressListener != null)
            {
                int prog = totalWritten * 100 / totalToWrite;
                if (prog > prevPercentage)
                {
                    prevPercentage = prog;
                    mBuilder.progressListener.onFileProgress(prog);
                }
            }
        }

        dos.writeBytes(Velocity.Settings.LINEEND);
        dos.writeBytes(Velocity.Settings.TWOHYPHENS + Velocity.Settings.BOUNDARY + Velocity.Settings.TWOHYPHENS + Velocity.Settings.LINEEND);


        fileInputStream.close();
        dos.flush();
        dos.close();

    }

}
