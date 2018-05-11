package com.example.fqzhang.androidknowledge.model

import com.example.fqzhang.androidknowledge.bean.KnowledgeTreeResponse
import com.example.fqzhang.androidknowledge.cancelByActive
import com.example.fqzhang.androidknowledge.constant.Constant
import com.example.fqzhang.androidknowledge.presenter.TypePresenter
import com.example.fqzhang.androidknowledge.retrofit.RetrofitHelper
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

/**
 * Created by fqzhang on 2018/4/19.
 */
class TypeModel {

    private var typeListAsync:Deferred<KnowledgeTreeResponse>? = null
    fun getKnowledgeTree(typeListListener:TypePresenter){
        try {
            async (UI){
                typeListAsync.cancelByActive()
                typeListAsync = RetrofitHelper.retrofitService.getKnowledgeTree()
                var result = typeListAsync?.await()
                if (result == null) {
                    typeListListener.getTypeListFailed(Constant.RESULT_NULL)
                } else {
                    typeListListener.getTypeListSuccess(result)
                }
            }
        } catch (e:Exception){
            e.printStackTrace()
            typeListListener.getTypeListFailed(e.toString())
        }
    }
}