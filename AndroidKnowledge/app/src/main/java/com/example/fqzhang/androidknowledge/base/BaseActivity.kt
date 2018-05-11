package com.example.fqzhang.androidknowledge.base

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gyf.barlibrary.ImmersionBar
@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {
    protected lateinit  var immersionBar: ImmersionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayoutId())
        initImmersionBar()
    }
    open protected fun initImmersionBar() {
        //在BaseActivity里初始化
        immersionBar = ImmersionBar.with(this).fitsSystemWindows(false)
        immersionBar.init()
    }
    abstract fun setLayoutId():Int
    override fun onDestroy() {
        super.onDestroy()
        immersionBar?.destroy()
    }
}
