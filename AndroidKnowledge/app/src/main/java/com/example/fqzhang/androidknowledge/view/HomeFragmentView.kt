package com.example.fqzhang.androidknowledge.view

import com.example.fqzhang.androidknowledge.bean.BannerResponse
import com.example.fqzhang.androidknowledge.bean.HomeListResponse

/**
 * Created by fqzhang on 2018/4/7.
 */
interface HomeFragmentView {
    fun getHomeListFailed(errorMessage:String?)
    fun getHomeListSuccess(result:HomeListResponse)
    fun getHomeListZero()
    fun getHomeListSmall(result: HomeListResponse)
    fun getBannerSuccess(result: BannerResponse)
    fun getBannerFailed(errorMessage: String?)
    fun getBannerZero()
}