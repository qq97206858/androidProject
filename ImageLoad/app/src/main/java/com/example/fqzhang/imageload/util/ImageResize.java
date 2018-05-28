package com.example.fqzhang.imageload.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * Created by fqzhang on 2018/4/5.
 */

public class ImageResize {

    public static Bitmap decodeBitmapFromResource(Resources res,int resId,int requireWidth,int requireHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize = calculateSampleSize(options,requireWidth,requireHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    private static int calculateSampleSize(BitmapFactory.Options options, int requireWidth, int requireHeight) {
        int sampleSize = 1;
        int height = options.outHeight;
        int width = options.outWidth;
        while (height/2>requireHeight && width/2>requireWidth) {
            height /= 2;
            width /= 2;
            sampleSize *= 2;
        }
        return sampleSize;
    }
    public static Bitmap decodeBitmapFromFile(FileDescriptor fd,int requireWidth,int requireHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd,null,options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options,requireWidth,requireHeight);
        return BitmapFactory.decodeFileDescriptor(fd,null,options);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d("tag", "origin, w= " + width + " h=" + height);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        Log.d("tag", "sampleSize:" + inSampleSize);
        return inSampleSize;
    }
}
