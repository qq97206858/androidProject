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
import com.example.fqzhang.androidknowledge.adapter.TypeAdapter
import com.example.fqzhang.androidknowledge.bean.KnowledgeData
import com.example.fqzhang.androidknowledge.bean.KnowledgeTreeResponse
import com.example.fqzhang.androidknowledge.constant.Constant
import com.example.fqzhang.androidknowledge.presenter.TypePresenterImpl
import com.example.fqzhang.androidknowledge.toast
import com.example.fqzhang.androidknowledge.ui.activity.TypeContentActivity
import kotlinx.android.synthetic.main.fragment_home.*

class TypeFragment : Fragment() {
    private var mParam1: String? = null
    private var mParam2: String? = null
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }
    private var typeDatas = mutableListOf<KnowledgeData>()
    private val typeAdatper:TypeAdapter by lazy {
        TypeAdapter(context,typeDatas)
    }
    private val typePresenter:TypePresenterImpl by lazy {
        TypePresenterImpl(this)
    }
    private var refreshListener = SwipeRefreshLayout.OnRefreshListener {
        typePresenter.getTypeList()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        swipeRefreshLayout.run {
            isRefreshing = false
            setOnRefreshListener(refreshListener)
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            //adapter = typeAdatper
        }
        typeAdatper.run {
            bindToRecyclerView(recyclerView)
            onItemClickListener = this@TypeFragment.onItemClickListener
        }
        typePresenter.getTypeList()
    }
    fun getTypeListSuccess(response:KnowledgeTreeResponse){
        response.data.let {
            typeAdatper.replaceData(it)
        }
        swipeRefreshLayout.isRefreshing = false
    }
    fun getTypeListFailed(errorMsg:String){
        activity.toast(errorMsg)
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener{
        _,_,position ->
        if (typeDatas.size != 0) {
            Intent(activity,TypeContentActivity::class.java).run {
                putExtra(Constant.CONTENT_TITLE_KEY,typeDatas[position].name)
                putExtra(Constant.CONTENT_CHILDREN_DATA_KEY,typeDatas[position])
                startActivity(this)
            }
        }
    }
    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): TypeFragment {
            val fragment = TypeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}
