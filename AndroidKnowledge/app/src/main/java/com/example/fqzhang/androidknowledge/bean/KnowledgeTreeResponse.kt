package com.example.fqzhang.androidknowledge.bean

/**
 * Created by fqzhang on 2018/4/19.
 */
data class KnowledgeTreeResponse(
        var errorCode:Int,
        var errorMsg:String,
        var data:List<KnowledgeData>
)