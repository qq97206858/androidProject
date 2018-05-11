package com.example.fqzhang.androidknowledge.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter

import com.example.fqzhang.androidknowledge.R
import com.example.fqzhang.androidknowledge.adapter.BannerAdapter
import com.example.fqzhang.androidknowledge.adapter.HomeAdapter
import com.example.fqzhang.androidknowledge.bean.BannerResponse
import com.example.fqzhang.androidknowledge.bean.Datas
import com.example.fqzhang.androidknowledge.bean.HomeListResponse
import com.example.fqzhang.androidknowledge.constant.Constant
import com.example.fqzhang.androidknowledge.loge
import com.example.fqzhang.androidknowledge.presenter.HomeFragmentPresenterImpl
import com.example.fqzhang.androidknowledge.toast
import com.example.fqzhang.androidknowledge.ui.activity.ContentActivity
import com.example.fqzhang.androidknowledge.ui.view.HorizontalRecyclerView
import com.example.fqzhang.androidknowledge.view.HomeFragmentView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_banner.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), HomeFragmentView {
    private val homeFragmentPresenter: HomeFragmentPresenterImpl by lazy {
        HomeFragmentPresenterImpl(this)
    }
    private val datas = mutableListOf<Datas>()
    private val bannerData = mutableListOf<BannerResponse.Data>()
    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter(this.context, datas)
    }
    private lateinit var homeBanner:HorizontalRecyclerView
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }
    private val listLinearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this.context)
    }
    /**
     * Banner PagerSnapHelper
     */
    private val bannerPagerSnap: PagerSnapHelper by lazy {
        PagerSnapHelper()
    }
    private val bannerAdapter: BannerAdapter by lazy{
        BannerAdapter(context,bannerData)
    }
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        val page = homeAdapter.data.size / 20 + 1
        homeFragmentPresenter.getHomeList(page)
    }
    private var bannerSwitchJob: Job? = null
    /**
     * save current index
     */
    private var currentIndex = 0
    private val onTouchListener = View.OnTouchListener{_, event->
        when(event.action) {
            MotionEvent.ACTION_MOVE ->{
                cancelSwitchJob()
            }
        }
        false
    }
    private val onScrollListener = object :RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when(newState) {
                RecyclerView.SCROLL_STATE_IDLE->{
                    currentIndex = linearLayoutManager.findFirstVisibleItemPosition()
                    startSwitchJob()
                }
            }
        }
    }
    private val onHomeListScrollListener = object :RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (listLinearLayoutManager.findFirstVisibleItemPosition() >= 1) {
                cancelSwitchJob()
            }else {
                startSwitchJob()
            }
        }
    }
    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.size != 0) {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, datas[position].link)
                putExtra(Constant.CONTENT_ID_KEY, datas[position].id)
                putExtra(Constant.CONTENT_TITLE_KEY, datas[position].title)
                startActivity(this)
            }
        }
    }
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        //TODO 处理类型以及收藏
    }
    private val onBannerItemClickListener = BaseQuickAdapter.OnItemClickListener{_,view,position->

    }
    override fun getBannerZero() {
        activity.toast("服务返回数据0条")
    }

    override fun getBannerSuccess(result: BannerResponse) {
        result.data?.let {
            bannerAdapter.replaceData(it)
            startSwitchJob()
        }
    }

    override fun getBannerFailed(errorMessage: String?) {
        errorMessage?.let {
            activity.toast("服务器下载数据失败！")
        } ?: let {
            activity.toast("数据获取失败！")
        }
    }

    override fun getHomeListFailed(errorMessage: String?) {
        homeAdapter.setEnableLoadMore(false)
        homeAdapter.loadMoreFail()
        errorMessage?.let {
            activity.toast("服务器下载数据失败！")
        } ?: let {
            activity.toast("数据获取失败！")
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getHomeListZero() {
        activity.toast("服务返回数据0条")
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getHomeListSmall(result: HomeListResponse) {
        result.data.datas?.let {
            homeAdapter.run {
                replaceData(it)
                loadMoreComplete()
                loadMoreEnd()
                setEnableLoadMore(false)
            }
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getHomeListSuccess(result: HomeListResponse) {
        result.data.datas?.let {
            homeAdapter.run {
                val total = result.data.total
                if (result.data.offset >= total || data.size >= total) {
                    loadMoreEnd()
                    return@let
                }
                if (swipeRefreshLayout.isRefreshing) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                loadMoreComplete()
                setEnableLoadMore(true)
            }

        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        homeBanner = inflater!!.inflate(R.layout.home_banner,null,false) as HorizontalRecyclerView
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = listLinearLayoutManager
            addOnScrollListener(onHomeListScrollListener)
        }

        homeAdapter.run {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@HomeFragment.onItemClickListener
            onItemChildClickListener = this@HomeFragment.onItemChildClickListener
            addHeaderView(homeBanner)
            //setEmptyView(R.layout.fragment_home_empty)
        }
        homeBanner.apply {
            layoutManager = linearLayoutManager
            requestDisallowInterceptTouchEvent(true)
            bannerPagerSnap.attachToRecyclerView(this)
            setOnTouchListener(onTouchListener)
            addOnScrollListener(onScrollListener)
        }
        bannerAdapter.run {
            bindToRecyclerView(homeBanner)
            onItemClickListener = this@HomeFragment.onBannerItemClickListener
        }
        //homeFragmentPresenter.getBanner()
        //homeFragmentPresenter.getHomeList()
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshData()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
        startSwitchJob()
}

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            cancelSwitchJob()
        }else {
            startSwitchJob()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        cancelSwitchJob()
    }
    fun refreshData() {
        swipeRefreshLayout.isRefreshing = true
        homeAdapter.setEnableLoadMore(false)
/*        homeAdapter.setEnableLoadMore(false)*/
        cancelSwitchJob()
        homeFragmentPresenter.getHomeBanner()
        homeFragmentPresenter.getHomeList()
    }
    private var flag = false//标记是正向循环还是反向
    private fun getBannerSwitchJob() = launch {
        repeat(Int.MAX_VALUE) {
            if (bannerData.size == 0) {
                return@launch
            }
            delay(5000)

           //currentIndex++

           // var index = currentIndex % bannerData.size
            if (flag) {
                currentIndex--
                if (currentIndex == 0) {
                    flag = false
                }
            } else {
                currentIndex++
                if (currentIndex == bannerData.size - 1 ) {
                    flag = true
                }
            }
            homeBanner.smoothScrollToPosition(currentIndex)
            //currentIndex = index
        }
    }
    private fun startSwitchJob() = bannerSwitchJob?.run {
        if (!isActive) {
            bannerSwitchJob = getBannerSwitchJob().apply { start() }
        }
    }?:let{
        bannerSwitchJob = getBannerSwitchJob().apply { start() }
    }
    private fun cancelSwitchJob() = bannerSwitchJob?.run {
        if (isActive) {
            cancel()
        }
    }
}
