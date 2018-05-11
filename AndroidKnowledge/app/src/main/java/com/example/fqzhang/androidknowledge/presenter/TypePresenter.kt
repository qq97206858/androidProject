package com.example.fqzhang.androidknowledge.presenter

import com.example.fqzhang.androidknowledge.bean.KnowledgeTreeResponse

/**
 * Created by fqzhang on 2018/4/19.
 */
interface TypePresenter {
    fun getTypeList()
    fun getTypeListFailed(errorMsg:String)
    fun getTypeListSuccess(result:KnowledgeTreeResponse)
}