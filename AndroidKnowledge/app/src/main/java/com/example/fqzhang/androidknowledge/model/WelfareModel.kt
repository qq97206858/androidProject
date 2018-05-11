package com.example.fqzhang.androidknowledge.model

import com.example.fqzhang.androidknowledge.bean.WelfareListResponse
import com.example.fqzhang.androidknowledge.cancelByActive
import com.example.fqzhang.androidknowledge.presenter.WelfarePresenter
import com.example.fqzhang.androidknowledge.retrofit.RetrofitHelper
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

/**
 * Created by fqzhang on 2018/4/22.
 */
class WelfareModel {
    var asyncWelfareList:Deferred<WelfareListResponse>? = null
    fun getWelfareList(listener: WelfarePresenter, page:Int){
        try {
            async (UI){
                asyncWelfareList.cancelByActive()
                asyncWelfareList = RetrofitHelper.gankService.getWelfareList(page)
                var result = asyncWelfareList?.await()
                if (result == null) {
                    listener.getWelfareListFailed()
                } else {
                    listener.getWelfareListSuccess(result)
                }
            }
        }catch (e:Exception) {
            e.printStackTrace()
            listener.getWelfareListFailed()
        }
    }
}