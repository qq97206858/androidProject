package com.example.fqzhang.androidknowledge.adapter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.fqzhang.androidknowledge.R
import com.example.fqzhang.androidknowledge.bean.BannerResponse
/**
 * Created by fqzhang on 2018/4/15.
 */
class BannerAdapter(var context: Context,var datas:MutableList<BannerResponse.Data> ):BaseQuickAdapter<BannerResponse.Data,BaseViewHolder>(R.layout.banner_item,datas) {
    override fun convert(helper: BaseViewHolder, item: BannerResponse.Data?) {
        item ?: return
        helper.setText(R.id.bannerTitle,item.title.trim())
        val view = helper.getView<ImageView>(R.id.bannerImage)
        Glide.with(context).load(item.imagePath).placeholder(R.mipmap.ic_launcher).into(view)
    }
}