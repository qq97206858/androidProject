package com.example.fqzhang.androidknowledge.ui.activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.Menu
import android.view.MenuItem
import com.example.fqzhang.androidknowledge.R
import com.example.fqzhang.androidknowledge.adapter.TypeArticalPageAdapter
import com.example.fqzhang.androidknowledge.base.BaseActivity
import com.example.fqzhang.androidknowledge.bean.KnowledgeData
import com.example.fqzhang.androidknowledge.constant.Constant
import com.example.fqzhang.androidknowledge.toast
import kotlinx.android.synthetic.main.activity_type_content.*

class TypeContentActivity : BaseActivity() {
    private val list = mutableListOf<KnowledgeData.Children>()
    private val typeArticleAdapter:TypeArticalPageAdapter by lazy {
        TypeArticalPageAdapter(list,supportFragmentManager)
    }
    override fun setLayoutId() = R.layout.activity_type_content
    var titleStr = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras.let { extras ->
            titleStr = extras.getString(Constant.CONTENT_TITLE_KEY)
            extras.getSerializable(Constant.CONTENT_CHILDREN_DATA_KEY).let { //目前只是分类跳转，首页类别跳转暂时没做
                val data = it as KnowledgeData
                data.children.let {
                    list.addAll(it)
                }
            }
        }
        toolBar.run {
            title = titleStr
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        typeSecondViewPager.run {
            adapter = typeArticleAdapter
        }
        typeSecondTabs.run {
            setupWithViewPager(typeSecondViewPager)
        }
        typeSecondViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(typeSecondTabs))
        typeSecondTabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(typeSecondViewPager))
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.toolBar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.menu_type_content,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
                return true
            }
            R.id.menuSearch->{
                toast("这里是搜索")
                return true
            }
            R.id.menuShare->{
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(
                            Intent.EXTRA_TEXT,
                            getString(
                                    R.string.share_type_url,
                                    getString(R.string.app_name),
                                    list[typeSecondTabs.selectedTabPosition].name,
                                    list[typeSecondTabs.selectedTabPosition].id
                            )
                        )
                    type = "text/plain"
                    startActivity(Intent.createChooser(this,"分享"))
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
