package com.example.fqzhang.androidknowledge.ui.fragment
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.fqzhang.androidknowledge.R
import com.example.fqzhang.androidknowledge.adapter.TypeArticleAdapter
import com.example.fqzhang.androidknowledge.bean.ArticleListResponse
import com.example.fqzhang.androidknowledge.bean.Datas
import com.example.fqzhang.androidknowledge.constant.Constant
import com.example.fqzhang.androidknowledge.presenter.TypeArticlePresenterImpl
import com.example.fqzhang.androidknowledge.toast
import com.example.fqzhang.androidknowledge.ui.activity.ContentActivity
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 * Use the [TypeArticleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TypeArticleFragment : Fragment() {

    private var cid: Int = 0
    private val typeArticlePresenter: TypeArticlePresenterImpl by lazy {
        TypeArticlePresenterImpl(this)
    }
    private var datas = mutableListOf<Datas>()
    private val typeArticleAdapter:TypeArticleAdapter by lazy {
        TypeArticleAdapter(context,datas)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            cid = arguments.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_type_article, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        swipeRefreshLayout.run {
            isRefreshing = false
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
          layoutManager = LinearLayoutManager(activity)
          adapter = typeArticleAdapter
        }
        typeArticleAdapter.run {
            setOnLoadMoreListener(onRequestLoadMoreListener,recyclerView)
            onItemClickListener = this@TypeArticleFragment.onItemClickListener
            onItemChildClickListener = this@TypeArticleFragment.onItemChildClickListener
        }
        typeArticlePresenter.getTypeArticleList(cid = cid)
    }
    fun getTypeArticleListFailed(errorMsg:String){
        typeArticleAdapter.loadMoreFail()
        typeArticleAdapter.setEnableLoadMore(false)
        activity.toast("数据加载失败！！"+errorMsg)
        swipeRefreshLayout.isRefreshing = false
    }

    fun getTypeArticleListSuccess(result: ArticleListResponse){
        result.data.datas?.let {
            typeArticleAdapter.run {
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
    fun getTypeArticleListZero() {
        activity.toast("数据总数为0")
        swipeRefreshLayout.isRefreshing = false
    }
    fun getTypeArticleListSmall(result: ArticleListResponse){
        result.data.datas?.let {
            typeArticleAdapter.run {
                replaceData(it)
                loadMoreComplete()
                loadMoreEnd()
                setEnableLoadMore(false)
            }
        }
        swipeRefreshLayout.isRefreshing = false
    }
    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        val page = typeArticleAdapter.data.size / 20 + 1
        typeArticlePresenter.getTypeArticleList(page, cid)
    }
    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        swipeRefreshLayout.isRefreshing = true
        typeArticleAdapter.setEnableLoadMore(false)
        typeArticlePresenter.getTypeArticleList(cid = cid)
    }
    private val onItemClickListener = object:BaseQuickAdapter.OnItemClickListener{
        override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            if (datas.size != 0) {
                Intent(activity, ContentActivity::class.java).run {
                    putExtra(Constant.CONTENT_URL_KEY, datas[position].link)
                    putExtra(Constant.CONTENT_ID_KEY, datas[position].id)
                    putExtra(Constant.CONTENT_TITLE_KEY, datas[position].title)
                    startActivity(this)
                }
            }
        }
    }
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener{//具体到每个item的子view的点击
        _,_,_->
        activity.toast("收藏点击，目前没做")
    }
    companion object {
        private val ARG_PARAM1 = "param1"

        fun newInstance(cid: Int): TypeArticleFragment {
            val fragment = TypeArticleFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM1, cid)
            fragment.arguments = args
            return fragment
        }
    }
}
