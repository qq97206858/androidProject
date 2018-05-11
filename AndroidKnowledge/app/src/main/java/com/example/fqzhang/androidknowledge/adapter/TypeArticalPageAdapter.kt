package com.example.fqzhang.androidknowledge.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.example.fqzhang.androidknowledge.bean.KnowledgeData
import com.example.fqzhang.androidknowledge.ui.fragment.TypeArticleFragment

/**
 * Created by fqzhang on 2018/4/21.
 */
class TypeArticalPageAdapter(val list:List<KnowledgeData.Children>,fm: FragmentManager) :FragmentStatePagerAdapter(fm){
    private val listFragment = mutableListOf<Fragment>()
    init {
        list.forEach { listFragment.add(TypeArticleFragment.newInstance(it.id))}

    }
    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence {
        return list[position].name
    }

}