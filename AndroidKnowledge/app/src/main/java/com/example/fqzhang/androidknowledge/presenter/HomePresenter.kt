package com.example.fqzhang.androidknowledge.presenter

import com.example.fqzhang.androidknowledge.bean.BannerResponse
import com.example.fqzhang.androidknowledge.bean.HomeListResponse

/**
 * Created by fqzhang on 2018/4/7.
 */
interface HomePresenter {
    interface OnHomeListListener{
        fun getHomeList(page:Int = 0)
        fun getHomeListSuccess(result:HomeListResponse)
        fun getHomeListFailed(errorMessage:String)
    }
    interface OnBannerListener{
        fun getHomeBanner()
        fun getBannerSuccess(result: BannerResponse)
        fun getBannerFailed(errorMessage: String)
    }
}