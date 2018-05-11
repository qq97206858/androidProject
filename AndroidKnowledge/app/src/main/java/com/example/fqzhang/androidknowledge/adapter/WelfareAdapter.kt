package com.example.fqzhang.androidknowledge.adapter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.fqzhang.androidknowledge.R
import com.example.fqzhang.androidknowledge.bean.WelfareListResponse

/**
 * Created by fqzhang on 2018/4/22.
 */
class WelfareAdapter(var context:Context,list: MutableList<WelfareListResponse.Data>):BaseQuickAdapter<WelfareListResponse.Data,BaseViewHolder>(R.layout.welfare_item,list) {
    override fun convert(helper: BaseViewHolder, item: WelfareListResponse.Data?) {
        item?:return
        var url = item.url
        var imageView = helper.getView<ImageView>(R.id.imageView)
        val text = if (item.desc.length > 48)
            item.desc.substring(0, 48) + "..."
        else
            item.desc
        Glide.with(context)
                .load(url).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageView)
        helper.setText(R.id.date,text)
    }
}