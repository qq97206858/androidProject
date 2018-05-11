package com.example.fqzhang.androidknowledge.bean

/**
 * Created by fqzhang on 2018/4/7.
 */
data class Data (
        var offset:Int,
        var size:Int,
        var total:Int,
        var pageCount:Int,
        var curPage:Int,
        var over:Boolean,
        var datas:List<Datas>?
)