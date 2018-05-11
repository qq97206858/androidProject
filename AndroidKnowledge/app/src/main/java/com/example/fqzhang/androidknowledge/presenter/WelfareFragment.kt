package com.example.fqzhang.androidknowledge.presenter


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter

import com.example.fqzhang.androidknowledge.R
import com.example.fqzhang.androidknowledge.adapter.WelfareAdapter
import com.example.fqzhang.androidknowledge.bean.WelfareListResponse
import com.example.fqzhang.androidknowledge.toast
import kotlinx.android.synthetic.main.fragment_welfare.*


/**
 * A simple [Fragment] subclass.
 * Use the [WelfareFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelfareFragment : Fragment() {

    private var presenter = WelfarePresenter(this)
    private var datas:MutableList<WelfareListResponse.Data> = mutableListOf()
    private val welfareAdapter:WelfareAdapter by lazy {
        WelfareAdapter(context,datas)
    }
    private var loadMoreListener = BaseQuickAdapter.RequestLoadMoreListener{
        var page = datas.size / 20 + 1
        presenter.getWelfareList(page)
    }
    private var refreshListener = SwipeRefreshLayout.OnRefreshListener{
        swipeRefreshLayout.isRefreshing = true
        presenter.getWelfareList(0)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_welfare, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        swipeRefreshLayout.run {
            isRefreshing = false
            setOnRefreshListener(refreshListener)
        }
        recyclerView.run {
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            adapter = welfareAdapter
            //addItemDecoration(GridSpacingItemDecoration(2, 10, true))
        }
        welfareAdapter.run {
            setEnableLoadMore(true)
            setOnLoadMoreListener(loadMoreListener,recyclerView)
        }
        presenter.getWelfareList(0)
    }
    fun getWelfareListFailed(){
        activity.toast("数据获取失败！")
        swipeRefreshLayout.isRefreshing = false
    }
    fun getWelfareListSuccess(results: List<WelfareListResponse.Data>){
        welfareAdapter.run {
            if (results.isNotEmpty()) {
                if (swipeRefreshLayout.isRefreshing) {
                    replaceData(results)
                } else {
                    addData(results)
                }
            }
            loadMoreComplete()
        }
        swipeRefreshLayout.isRefreshing = false
    }

}
