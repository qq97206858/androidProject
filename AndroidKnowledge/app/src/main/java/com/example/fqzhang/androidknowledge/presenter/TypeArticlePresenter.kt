package com.example.fqzhang.androidknowledge.presenter

import com.example.fqzhang.androidknowledge.bean.ArticleListResponse

/**
 * Created by fqzhang on 2018/4/21.
 */
interface TypeArticlePresenter {

    fun getTypeArticleList(page:Int = 0,cid:Int)
    fun getTypeArticleListSuccess(result:ArticleListResponse)
    fun getTypeArticleListFailed(errorMsg:String)
}