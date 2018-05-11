package com.example.fqzhang.androidknowledge.presenter

import android.os.Parcel
import android.os.Parcelable
import com.example.fqzhang.androidknowledge.bean.BannerResponse
import com.example.fqzhang.androidknowledge.bean.HomeListResponse
import com.example.fqzhang.androidknowledge.model.HomeModel
import com.example.fqzhang.androidknowledge.model.HomeModelImpl
import com.example.fqzhang.androidknowledge.view.HomeFragmentView

/**
 * Created by fqzhang on 2018/4/7.
 */
class HomeFragmentPresenterImpl(private val homeFragmentView: HomeFragmentView) :HomePresenter,HomePresenter.OnHomeListListener,HomePresenter.OnBannerListener{
    override fun getHomeBanner() {
        homeModel.getHomeBanner(this)
    }

    override fun getBannerSuccess(result: BannerResponse) {
        if (result.errorCode != 0) {
            homeFragmentView.getBannerFailed(result.errorMsg)
        }else {
            result.data ?: let {

                homeFragmentView.getBannerZero()
                return
            }
            homeFragmentView.getBannerSuccess(result)
        }

    }

    override fun getBannerFailed(errorMessage: String) {
        homeFragmentView.getBannerFailed(errorMessage)
    }

    private val homeModel: HomeModel = HomeModelImpl()

    override fun getHomeList(page: Int) {
        homeModel.getHomeList(this,page)
    }

    override fun getHomeListSuccess(result: HomeListResponse) {
        if (result.errorCode != 0) {
            homeFragmentView.getHomeListFailed(result.errorMsg)
        } else {
            val total = result.data.total
            if (total == 0) {
                homeFragmentView.getHomeListZero()
                return
            }
            if (total < result.data.size) {
                homeFragmentView.getHomeListSmall(result)
                return
            }
            homeFragmentView.getHomeListSuccess(result)
        }

    }

    override fun getHomeListFailed(errorMessage: String) {
        homeFragmentView.getHomeListFailed(errorMessage)
    }

}