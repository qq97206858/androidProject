package com.example.fqzhang.androidknowledge.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.fqzhang.androidknowledge.R
import com.example.fqzhang.androidknowledge.bean.KnowledgeData
import com.example.fqzhang.androidknowledge.loge

/**
 * Created by fqzhang on 2018/4/19.
 */
class TypeAdapter(var context: Context,var datas:MutableList<KnowledgeData> ):BaseQuickAdapter<KnowledgeData,BaseViewHolder>(R.layout.type_list_item,datas) {
    override fun convert(helper: BaseViewHolder, item: KnowledgeData?) {
        item ?: return

        var types = ""
        for(i in 0 until  item.children.size) {
            loge("typeAdapter",item.children[i].name)
            types += "  ${item.children[i].name}"
        }
        loge("typeAdapter",types)
        helper.setText(R.id.title,item.name)
                .setText(R.id.type,types.replaceFirst("  ",""))
    }
}