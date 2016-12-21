package com.rw.velocity;

import java.io.FileNotFoundException;
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
    private int mPrevProgress = 0;

    DownloadRequest(RequestBuilder builder)
    {
        super(builder);
    }

    @Override
    protected boolean readResponse()
    {
        boolean ret = false;

        NetLog.d("read response in download request");
        try
        {
            if (mResponseCode / 100 == 2) //all 2xx codes are OK
            {
                InputStream inputStream = mConnection.getInputStream();


                FileOutputStream outputStream = new FileOutputStream(mBuilder.downloadFile);

                int bytesRead;
                int totalRead = 0;
                int contentLen = mConnection.getContentLength();
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    outputStream.write(buffer, 0, bytesRead);

                    totalRead += bytesRead;

                    if(mBuilder.progressListener != null)
                    {
                        int prog = totalRead * 100 / contentLen;
                        if(prog > mPrevProgress)
                        {
                            mPrevProgress = prog;
                            mBuilder.progressListener.onFileProgress(prog);
                        }
                    }
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

                mResponse.append(mConnection.getContentType());
                ret = true;
            }
            else
                ret = false;

        }
        catch (IOException ioe)
        {
            mResponse = new StringBuilder(ioe.getMessage());
            ret = false;
        }


        return ret;
    }
}
