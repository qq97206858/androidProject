package com.example.fqzhang.androidknowledge.bean

/**
 * Created by fqzhang on 2018/4/21.
 */
data class FriendListResponse (
        var errorCode:Int,
        var errorMsg:String,
        var data:List<Data>?
){
    data class Data(
            var id:Int,
            var name:String,
            var link:String,
            var visible:Int,
            var order:Int,
            var icon:Any
    )
}