package com.rw.velocity;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Build;
import android.webkit.MimeTypeMap;

import java.io.File;
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
    boolean readResponse()
    {
        boolean ret;

        try
        {
            if (mResponseCode / 100 == 2) //all 2xx codes are OK
            {
                InputStream inputStream = mConnection.getInputStream();


                FileOutputStream outputStream = new FileOutputStream(mBuilder.downloadFile);

                int bytesRead;
                int totalRead = 0;
                int contentLen = mConnection.getContentLength();
                byte[] buffer = new byte[Velocity.Settings.MAX_BUFFER];
                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    outputStream.write(buffer, 0, bytesRead);

                    totalRead += bytesRead;

                    if (mBuilder.progressListener != null)
                    {
                        int prog = totalRead * 100 / contentLen;
                        if (prog > mPrevProgress)
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
            mResponse = new StringBuilder(ioe.toString());
            ret = false;
        }


        if(mBuilder.context != null)
        {
            //add file to download ui
            DownloadManager manager = (DownloadManager)mBuilder.context.getSystemService(Context.DOWNLOAD_SERVICE);
            File f = new File(mBuilder.downloadFile);
            String extension = MimeTypeMap.getFileExtensionFromUrl(f.getAbsolutePath());
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

            if(mimeType == null)
                mimeType = "*/*";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
            {
                manager.addCompletedDownload(mBuilder.downloadUiTitle, mBuilder.downloadUiDescr, true, mimeType, f.getAbsolutePath(), f.length(), true);
            }
        }


        return ret;
    }
}
