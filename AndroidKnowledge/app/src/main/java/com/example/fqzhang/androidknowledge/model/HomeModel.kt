package com.example.fqzhang.androidknowledge.model

import com.example.fqzhang.androidknowledge.presenter.HomePresenter

/**
 * Created by fqzhang on 2018/4/8.
 */
interface HomeModel {
    fun getHomeList(homeListListener: HomePresenter.OnHomeListListener,page:Int)
    fun getHomeBanner(homeBanner: HomePresenter.OnBannerListener)
}