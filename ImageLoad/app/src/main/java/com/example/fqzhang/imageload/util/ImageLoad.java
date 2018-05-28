package com.example.fqzhang.imageload.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.fqzhang.imageload.R;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fqzhang on 2018/4/5.
 */

public class ImageLoad {
    private LruCache<String,Bitmap> mLrucache = null;
    private ExecutorService service =  Executors.newFixedThreadPool(4);
    private static final String TAG = "IMAGELOAD_TAG";
    private static final int CACHE_SIZE = 1024*8;
    private static final  int MAX_DISCACHE_SIZE = 50*1024*1024;
    private static final String UNIQUE_CACHE_NAME = "bitmap";
    private Context applicationContext;
    private DiskLruCache mDisLruCache;
    private static final int INDEX = 0;
    private static final int KEY_TAG =  R.id.imageview;

    private static final int CPU_COUNT = Runtime.getRuntime()
            .availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), sThreadFactory);
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            ImageResult result = (ImageResult) msg.obj;
            String tag = (String) result.imageView.getTag(KEY_TAG);
            if (tag.equals(result.url)) {
                result.imageView.setImageBitmap(result.bitmap);
            }
        }
    };
    private ImageLoad(Context context){
        applicationContext = context.getApplicationContext();
        mDisLruCache = createDiskLruCache();
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
        mLrucache = new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight()/1024;
            }
        };
    }
    public static ImageLoad build(Context context){
        return new ImageLoad(context);
    }
    public void bindImage(final ImageView view, final String url, final int requireWidth, final int requireHeight) {
        Bitmap bitmap = loadBitmapFromCache(url);
        view.setTag(KEY_TAG , url);
        if (bitmap != null) {
            view.setImageBitmap(bitmap);
        }
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(url,requireWidth,requireHeight);
                if (bitmap != null) {
                    ImageResult result = new ImageResult(bitmap,view,url);
                    Message message = Message.obtain();
                    message.obj = result;
                    mHandler.sendMessage(message);
                }

            }
        };
        service.execute(task);
    }

    public Bitmap loadBitmap(String url, int requireWidth, int requireHeight) {
        Bitmap bitmap = loadBitmapFromCache(url);
        if(bitmap != null) {
            Log.e(TAG,"cache 中存在");
            return bitmap;
        }
        try {
           bitmap = loadBitmapFromDisLrucache(url,requireWidth,requireHeight);
           if (bitmap != null ) {
               Log.e(TAG,"Diskcache 中存在");
               return bitmap;
           }
           bitmap = loadBitmapFromHttp(url,requireWidth,requireHeight);
           if (bitmap != null) {
               Log.e(TAG,"网络下载");
           }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap == null || mDisLruCache == null) {
            Log.e(TAG,"发生异常！！或者diskLruCache 为null");
            bitmap = loadBitmapFromUrl(url);
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromUrl(String url) {
        HttpURLConnection urlConnection = null;
        BufferedInputStream bis = null;
        Bitmap bitmap = null;
        try {
            URL ul = new URL(url);
            urlConnection = (HttpURLConnection) ul.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            bis = new BufferedInputStream(inputStream,CACHE_SIZE);
            bitmap = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromCache(String url){
        String key = toHashFromUrl(url);
        Bitmap bitmap = mLrucache.get(key);
        return bitmap;
    }
    private Bitmap loadBitmapFromDisLrucache(String url, int requireWidth, int requireHeight) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            Log.e(TAG,"this is a mainUIThread！！！");
            throw new RuntimeException("this is a mainUIThread！！！");
        }
        Bitmap bitmap = null;
        String key = toHashFromUrl(url);
        DiskLruCache.Snapshot snapshot = mDisLruCache.get(key);
        if (snapshot != null) {
            FileInputStream inputStream = (FileInputStream) snapshot.getInputStream(INDEX);
            FileDescriptor fd = inputStream.getFD();
            bitmap = ImageResize.decodeBitmapFromFile(fd, requireWidth, requireHeight);
            if (bitmap != null) {
                addBitmapToLruCache(bitmap,key);
            }
        }
        return bitmap;
    }
    private void addBitmapToLruCache(Bitmap bitmap,String key){
        if (mLrucache.get(key) == null) {
            mLrucache.put(key,bitmap);
        }
    }
    public Bitmap loadBitmapFromHttp(String url, int requireWidth, int requireHeight){
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.e(TAG,"Not MainUI thread");
            throw new RuntimeException("only main Thread can not change UI");
        }
        if (mDisLruCache == null) {
            return null;
        }
        try {
            DiskLruCache.Editor editor = mDisLruCache.edit(toHashFromUrl(url));
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(INDEX);
                if (downloadUrlToStream(url,outputStream)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                mDisLruCache.flush();
            }
            return loadBitmapFromDisLrucache(url,requireWidth,requireHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean downloadUrlToStream(String url, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedInputStream bis = null;
        try {
            URL url1 = new URL(url);
            urlConnection = (HttpURLConnection) url1.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            bis = new BufferedInputStream(inputStream,CACHE_SIZE);
            int b;
            while((b = bis.read()) != -1){
                outputStream.write(b);
            }
            outputStream.flush();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private DiskLruCache createDiskLruCache(){
        try{
            File cacheDir = getDiskCacheDir(applicationContext, UNIQUE_CACHE_NAME);
            Log.e(TAG,cacheDir.getAbsolutePath());
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            if (getUsableSpace(cacheDir) < MAX_DISCACHE_SIZE) {
                return null;
            }
            DiskLruCache diskLruCache = DiskLruCache.open(cacheDir, getAppVersion(applicationContext), 1, MAX_DISCACHE_SIZE);
            return diskLruCache;
        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    }
    public File getDiskCacheDir(Context context,String uniqueName) {
        String cachePath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();

        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.pathSeparator + uniqueName);
    }
    public int getAppVersion(Context context){
        try{
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  1;
    }
    public long getUsableSpace(File file){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return file.getUsableSpace();
        }
        StatFs statFs = new StatFs(file.getPath());
        return statFs.getBlockSize()*statFs.getAvailableBlocks();
    }
    class ImageResult{
        public Bitmap bitmap;
        public ImageView imageView;
        public String url;
        public ImageResult(Bitmap bitmap,ImageView imageView,String url) {
            this.bitmap = bitmap;
            this.imageView = imageView;
            this.url = url;
        }
    }
    private String toHashFromUrl(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            byte[] bytes = digest.digest();
            return toHexString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return String.valueOf(url.hashCode());
        }
    }

    private String toHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String s = Integer.toHexString(0xFF & bytes[i]);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s);
        }
        return sb.toString();
    }
}
