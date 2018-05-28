package com.example.fqzhang.imageload

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import com.example.fqzhang.imageload.R.layout.item
import com.example.fqzhang.imageload.adapter.MAdapter
import com.example.fqzhang.imageload.util.ImageLoad
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var datas = arrayListOf(
                "https://ws1.sinaimg.cn/large/610dc034ly1foowtrkpvkj20sg0izdkx.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fp9qm6nv50j20u00miacg.jpg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180208080314_FhzuAJ_Screenshot.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180129074038_O3ydq4_Screenshot.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180122090204_A4hNiG_Screenshot.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180115085556_8AeReR_taeyeon_ss_15_1_2018_7_58_51_833.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180109085038_4A7atU_rakukoo_9_1_2018_8_50_25_276.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20171228085004_5yEHju_Screenshot.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20171227115959_lmlLZ3_Screenshot.jpeg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fp9qm6nv50j20u00miacg.jpg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180208080314_FhzuAJ_Screenshot.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180129074038_O3ydq4_Screenshot.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180122090204_A4hNiG_Screenshot.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180115085556_8AeReR_taeyeon_ss_15_1_2018_7_58_51_833.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180109085038_4A7atU_rakukoo_9_1_2018_8_50_25_276.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20171228085004_5yEHju_Screenshot.jpeg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fp9qm6nv50j20u00miacg.jpg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180208080314_FhzuAJ_Screenshot.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180129074038_O3ydq4_Screenshot.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180122090204_A4hNiG_Screenshot.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180115085556_8AeReR_taeyeon_ss_15_1_2018_7_58_51_833.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20180109085038_4A7atU_rakukoo_9_1_2018_8_50_25_276.jpeg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/20171228085004_5yEHju_Screenshot.jpeg"

        )
        recycleView.layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        recycleView.adapter = MyAdapter(datas,this)
        val mAdapter = MAdapter(datas, this)
/*        gridview.adapter = mAdapter
        gridview.setOnScrollListener(ScrollListener(mAdapter))*/
    }

    class ScrollListener(var adapter:MAdapter) : AbsListView.OnScrollListener {
        override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onScrollStateChanged(p0: AbsListView?, scrollState: Int) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                //adapter.setmIsGridViewIdle(true)
                adapter.notifyDataSetChanged()
            } else {
                //adapter.setmIsGridViewIdle(false)
            }
        }
    }

    class MyViewHolder(private val iteView: View) : RecyclerView.ViewHolder(iteView) {

        var imageView: ImageView ?= null
        init {
            imageView = iteView.findViewById(R.id.imageview)
        }
    }

    class MyAdapter(var datas:ArrayList<String>,context: Context): RecyclerView.Adapter<MyViewHolder>(){
        var mImageLoad:ImageLoad ?= null
        init {
            mImageLoad = ImageLoad.build(context)
        }
        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
            var tag = holder?.imageView?.tag
            if (datas[position] != tag){
                holder?.imageView?.setImageResource(R.mipmap.ic_launcher)
            }
            if (true) {
                holder?.imageView?.tag = datas[position]
                mImageLoad?.bindImage(holder?.imageView, datas[position], 400, 400)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
            var item = LayoutInflater.from(parent?.context).inflate(R.layout.item,parent,false)
            return MyViewHolder(item)
        }
    }



}
