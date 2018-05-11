package com.example.fqzhang.androidknowledge.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.example.fqzhang.androidknowledge.R
import com.example.fqzhang.androidknowledge.base.BaseActivity
import com.example.fqzhang.androidknowledge.constant.Constant
import com.example.fqzhang.androidknowledge.getAgentWeb
import com.just.agentweb.AgentWeb
import com.just.agentweb.ChromeClientCallbackManager
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : BaseActivity() {
    private lateinit var agentWeb:AgentWeb
    override fun setLayoutId(): Int = R.layout.activity_content

    private var shareId: Int = 0
    private lateinit var shareUrl :String

    private lateinit var shareTitle :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolBar.run {
            title = "正在加载中。。。"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        intent.extras?.let {
            shareId = it.getInt(Constant.CONTENT_ID_KEY, 0)
            shareUrl = it.getString(Constant.CONTENT_URL_KEY)
            shareTitle = it.getString(Constant.CONTENT_TITLE_KEY)
            agentWeb = shareUrl.getAgentWeb(
                    this,
                    webContent,
                    LinearLayout.LayoutParams(-1,-1),
                    receivedTitleCallback
            )
        }
    }

    override fun onResume() {
        super.onResume()
        agentWeb.webLifeCycle.onResume()
    }

    override fun onPause() {
        super.onPause()
        agentWeb.webLifeCycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb.webLifeCycle.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (agentWeb.handleKeyEvent(keyCode,event)) {
            true
        } else {
            finish()
            super.onKeyDown(keyCode, event)
        }

    }
    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.toolBar).init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home-> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private val receivedTitleCallback =
            ChromeClientCallbackManager.ReceivedTitleCallback { _, title ->
                title?.let {
                    toolBar.title = it
                }
            }
}
