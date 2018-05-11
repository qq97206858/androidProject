package com.example.fqzhang.androidknowledge.presenter

import com.example.fqzhang.androidknowledge.bean.WelfareListResponse
import com.example.fqzhang.androidknowledge.model.WelfareModel

/**
 * Created by fqzhang on 2018/4/22.
 */
class WelfarePresenter(var welfareFragment: WelfareFragment) {
    var welfareModel = WelfareModel()
    fun getWelfareList(page:Int){
        welfareModel.getWelfareList(this,page)
    }
    fun getWelfareListSuccess(result:WelfareListResponse){
        if (result.error) {
            welfareFragment.getWelfareListFailed()
        } else {
            welfareFragment.getWelfareListSuccess(result.results)
        }
    }
    fun getWelfareListFailed(){
        welfareFragment.getWelfareListFailed()
    }
}