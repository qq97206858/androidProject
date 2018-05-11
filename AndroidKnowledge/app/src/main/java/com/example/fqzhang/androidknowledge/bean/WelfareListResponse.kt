package com.example.fqzhang.androidknowledge.bean

/**
 * Created by fqzhang on 2018/4/22.
 */
data class WelfareListResponse(
        var error:Boolean,
        var results:List<Data>
){
    data class Data(
            var _id:String,
            var createdAt:String,
            var desc:String,
            var publishedAt:String,
            var source:String,
            var type:String,
            var url:String,
            var used:Boolean,
            var who:String
    )
}