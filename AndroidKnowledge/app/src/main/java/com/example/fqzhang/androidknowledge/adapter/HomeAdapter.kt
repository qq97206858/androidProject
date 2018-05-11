package com.example.fqzhang.androidknowledge.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.fqzhang.androidknowledge.R
import com.example.fqzhang.androidknowledge.bean.Datas

/**
 * Created by fqzhang on 2018/4/15.
 */
class HomeAdapter(val context: Context, datas:MutableList<Datas>):BaseQuickAdapter<Datas,BaseViewHolder>(R.layout.home_list_item, datas){
    override fun convert(helper: BaseViewHolder, item: Datas?) {
        item ?: return
        helper.setText(R.id.homeItemTitle,item.title)
                .setText(R.id.homeItemAuthor,item.author)
                .setText(R.id.homeItemType,item.chapterName)
                .addOnClickListener(R.id.homeItemType)
                .setTextColor(R.id.homeItemType, context.resources.getColor(R.color.colorPrimary))
                .linkify(R.id.homeItemType)
                .setText(R.id.homeItemDate, item.niceDate)
                .setImageResource(
                        R.id.homeItemLike,
                        if (item.collect) R.drawable.ic_action_like else R.drawable.ic_action_no_like
                )
                .addOnClickListener(R.id.homeItemLike)
    }
}