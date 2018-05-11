package com.example.fqzhang.androidknowledge.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.fqzhang.androidknowledge.R
import kotlinx.android.synthetic.main.home_list_item.view.*

/**
 * Created by fqzhang on 2018/4/7.
 */
class HomeAdapterHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    var title: TextView? = null
    var type: TextView? = null
    var author: TextView? = null
    var date: TextView? = null
    var like: ImageView? = null

    init {
        itemView?.run {
            title = findViewById(R.id.homeItemTitle)
            type = findViewById(R.id.homeItemType)
            author = findViewById(R.id.homeItemAuthor)
            date = findViewById(R.id.homeItemDate)
            like = findViewById(R.id.homeItemLike)
        }
    }
}