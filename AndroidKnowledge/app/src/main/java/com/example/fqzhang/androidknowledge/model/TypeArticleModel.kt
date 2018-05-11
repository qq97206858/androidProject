package com.example.fqzhang.androidknowledge.model

import com.example.fqzhang.androidknowledge.bean.ArticleListResponse
import com.example.fqzhang.androidknowledge.bean.FriendListResponse
import com.example.fqzhang.androidknowledge.cancelByActive
import com.example.fqzhang.androidknowledge.constant.Constant
import com.example.fqzhang.androidknowledge.presenter.TypeArticlePresenter
import com.example.fqzhang.androidknowledge.retrofit.RetrofitHelper
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

/**
 * Created by fqzhang on 2018/4/21.
 */
class TypeArticleModel {
    private  var asyncArticlaList:Deferred<ArticleListResponse>? = null
    fun getTypeArticleList(listener:TypeArticlePresenter,page:Int,cid:Int){
        async (UI){
            try {
                asyncArticlaList.cancelByActive()
                asyncArticlaList = RetrofitHelper.retrofitService.getArticleList(page,cid)
                var result = asyncArticlaList?.await()
                result?:let {
                    listener.getTypeArticleListFailed(Constant.RESULT_NULL)
                    return@async
                }
                listener.getTypeArticleListSuccess(result)
            }catch (e:Exception){
                e.printStackTrace()
                listener.getTypeArticleListFailed(e.toString())
            }
        }
    }
}