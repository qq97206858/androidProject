package com.example.fqzhang.androidknowledge.model

import com.example.fqzhang.androidknowledge.bean.BannerResponse
import com.example.fqzhang.androidknowledge.bean.HomeListResponse
import com.example.fqzhang.androidknowledge.cancelByActive
import com.example.fqzhang.androidknowledge.constant.Constant
import com.example.fqzhang.androidknowledge.presenter.HomePresenter
import com.example.fqzhang.androidknowledge.retrofit.RetrofitHelper
import com.example.fqzhang.androidknowledge.tryCatch
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

/**
 * Created by fqzhang on 2018/4/8.
 */
class HomeModelImpl:HomeModel {
    override fun getHomeBanner(homeBanner: HomePresenter.OnBannerListener) {
        try {
            async (UI){
                homeBannerAsync.cancelByActive()
                homeBannerAsync = RetrofitHelper.retrofitService.getBanner()
                var result = homeBannerAsync?.await()
                if (result == null) {
                    homeBanner.getBannerFailed(Constant.RESULT_NULL)
                } else {
                    homeBanner.getBannerSuccess(result)
                }
            }
        } catch (e:Exception) {
            e.printStackTrace()
            homeBanner.getBannerFailed(Constant.RESULT_NULL)
        }
    }


    private var homeListAsync: Deferred<HomeListResponse>? = null
    private var homeBannerAsync: Deferred<BannerResponse>? = null
    override fun getHomeList(homeListListener: HomePresenter.OnHomeListListener, page: Int) {

        try {
            async(UI) {
                homeListAsync?.cancelByActive()
                homeListAsync = RetrofitHelper.retrofitService.getHomeList(page)
                val result = homeListAsync?.await()
                if (result == null ){
                    homeListListener.getHomeListFailed(Constant.RESULT_NULL)
                } else {
                    homeListListener.getHomeListSuccess(result)
                }
            }
        } catch (e:Exception) {
            e.printStackTrace()
            homeListListener.getHomeListFailed(e.toString())
        }
    }

}