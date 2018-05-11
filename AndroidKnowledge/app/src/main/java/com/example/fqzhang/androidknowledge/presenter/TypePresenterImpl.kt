package com.example.fqzhang.androidknowledge.presenter

import com.example.fqzhang.androidknowledge.bean.KnowledgeTreeResponse
import com.example.fqzhang.androidknowledge.model.TypeModel
import com.example.fqzhang.androidknowledge.ui.fragment.TypeFragment

/**
 * Created by fqzhang on 2018/4/19.
 */
class TypePresenterImpl(val typeFragment:TypeFragment) :TypePresenter {
    var typeModel = TypeModel()
    override fun getTypeList() {
        typeModel.getKnowledgeTree(this)
    }

    override fun getTypeListFailed(errorMsg:String) {
        typeFragment.getTypeListFailed(errorMsg)
    }

    override fun getTypeListSuccess(result: KnowledgeTreeResponse) {
        typeFragment.getTypeListSuccess(result)
    }
}