package com.example.fqzhang.androidknowledge.bean

import java.io.Serializable

/**
 * Created by fqzhang on 2018/4/19.
 */
data class KnowledgeData(
        var courseId:Int,
        var id:Int,
        var name:String,
        var order:Int,
        var parentChapterId:Int,
        var visible:Int,
        var children:List<Children>
) : Serializable{
    data class Children (
        var id: Int,
        var name:String,
        var order:Int,
        var parentChapterId: Int,
        var visible: Int,
        var children: List<Children>
    ): Serializable
}