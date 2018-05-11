package com.example.fqzhang.androidknowledge.ui.activity

import android.os.Bundle
import android.view.MenuItem
import com.example.fqzhang.androidknowledge.R
import com.example.fqzhang.androidknowledge.base.BaseActivity
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseActivity() {
    override fun setLayoutId(): Int = R.layout.activity_about

    override fun initImmersionBar(){
        super.initImmersionBar()
        immersionBar.titleBar(R.id.toolBar)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolBar.run {
            title = "关于"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
