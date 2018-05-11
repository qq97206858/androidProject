package com.example.fqzhang.androidknowledge.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fqzhang.androidknowledge.R
import com.example.fqzhang.androidknowledge.bean.Datas
/**
 * Created by fqzhang on 2018/4/7.
 */
class HomeAdapter2(context: Context, var datas:MutableList<Datas>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var inflater:LayoutInflater? = null
    init {
        inflater = LayoutInflater.from(context)
    }
    override fun getItemCount(): Int {
        return datas.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
       var viewRoot = inflater?.inflate(R.layout.home_list_item,parent,false)
       return  HomeAdapterHolder(viewRoot)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
       if (holder is HomeAdapterHolder) {
           var data = datas[position]
           holder.title?.text = data.title
           holder.author?.text = data.author
           holder.date?.text = data.niceDate
           holder.type?.run{
               Linkify.addLinks(this,Linkify.ALL)
               text = data.chapterName
               setOnClickListener {

               }
               setTextColor(context.resources.getColor(R.color.colorPrimary))
           }
           holder.like?.run{
               setOnClickListener {

               }
               setImageResource(if(data.collect)R.drawable.ic_action_like else R.drawable.ic_action_no_like)
           }
       }
    }
}