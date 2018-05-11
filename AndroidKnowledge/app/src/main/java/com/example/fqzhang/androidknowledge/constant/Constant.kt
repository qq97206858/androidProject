package com.example.fqzhang.androidknowledge.constant

import android.widget.Toast

/**
 * Created by fqzhang on 2018/3/28.
 */
object Constant {//单例
    const val GANK_BASE_URL= "http://gank.io/"
    /**
     * baseUrl
     */
    const val REQUEST_BASE_URL = "http://wanandroid.com/"
    /**
     * Toast
     */
    @JvmField //该注解修饰的成员没有set\get 方法
    var showToast: Toast? = null

    const val SHARED_NAME = "_preferences"
    const val RESULT_NULL = "result null!"
    val CONTENT_URL_KEY: String = "url"
    val CONTENT_ID_KEY: String = "id"
    val CONTENT_TITLE_KEY: String = "title"
    val CONTENT_CHILDREN_DATA_KEY = "childrenData"
}
