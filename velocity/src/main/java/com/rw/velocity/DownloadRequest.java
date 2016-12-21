package com.rw.velocity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 21/12/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */

class DownloadRequest extends Request
{
    DownloadRequest(RequestBuilder builder)
    {
        super(builder);
    }

    @Override
    protected void readResponse()
    {
        NetLog.d("read response in download request");
        try
        {
            if (mResponseCode / 100 == 2) //all 2xx codes are OK
            {
                InputStream inputStream = mConnection.getInputStream();


                FileOutputStream outputStream = new FileOutputStream(mBuilder.downloadFile);

                int bytesRead;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

                mResponse.append(mConnection.getContentType());
                mSuccess = true;
            }
            else
                mSuccess = false;

        }
        catch (IOException ioe)
        {
            mResponse = new StringBuilder(ioe.getMessage());
            mSuccess = false;
        }
    }
}
