package com.example.fqzhang.imageload.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.fqzhang.imageload.R;
import com.example.fqzhang.imageload.util.ImageLoad;
import com.example.fqzhang.imageload.util.ImageLoader2;

import java.util.ArrayList;

/**
 * Created by fqzhang on 2018/4/5.
 */

public class MAdapter extends BaseAdapter {
    private boolean mIsGridViewIdle = true;
    ArrayList<String> datas = new ArrayList<>();
    Context context = null;

    private ImageLoad mImageLoad = null;
    public MAdapter(ArrayList<String> datas,Context context){
        this.datas = datas;
        this.context = context;
        mImageLoad = ImageLoad.build(context);

    }

    public void setmIsGridViewIdle(boolean mIsGridViewIdle) {
        this.mIsGridViewIdle = mIsGridViewIdle;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public String getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageHolder holder = null;
        if (view == null) {
            view = View.inflate(context, R.layout.item,null);
            holder = new ImageHolder();
            holder.imageView = view.findViewById(R.id.imageview);
            view.setTag(holder);
        }else {
            holder = (ImageHolder) view.getTag();
        }
        ImageView imageView = holder.imageView;
        String tag = (String) imageView.getTag();
        String url = getItem(i);
        if (!url.equals(tag)) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));
        }
        if (mIsGridViewIdle) {
            imageView.setTag(url);
            //mImageLoad.bindBitmap(url,imageView,100,400);
            mImageLoad.bindImage(imageView,url,100,400);
        }
        return view;
    }
    static class ImageHolder {
        ImageView imageView;
    }
}
