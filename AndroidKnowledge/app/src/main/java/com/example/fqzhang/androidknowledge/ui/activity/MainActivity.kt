package com.example.fqzhang.androidknowledge.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.example.fqzhang.androidknowledge.R
import com.example.fqzhang.androidknowledge.base.BaseActivity
import com.example.fqzhang.androidknowledge.presenter.WelfareFragment
import com.example.fqzhang.androidknowledge.toast
import com.example.fqzhang.androidknowledge.ui.fragment.HomeFragment
import com.example.fqzhang.androidknowledge.ui.fragment.TypeFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity: BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var lastClickTime = 0L
    override fun setLayoutId(): Int = R.layout.activity_main
    private var homeFragment: HomeFragment? = null
    private var typeFragment: TypeFragment? = null
    private var welfareFragment:WelfareFragment ?= null
    private val fragmentManager by lazy {
        supportFragmentManager
    }
    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.toolBar)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolBar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }
        drawerLayout.run {
            val toggle = ActionBarDrawerToggle(
                    this@MainActivity, drawerLayout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            addDrawerListener(toggle)
            toggle.syncState()
        }
        navigationView.setNavigationItemSelectedListener(this)
        bottomNavigation.run {
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            selectedItemId = R.id.navigation_home
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is HomeFragment -> homeFragment.let { homeFragment = fragment }
        }
    }
    override fun onResume() {
        super.onResume()
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            var currentClickTime = Date().time
            if ((currentClickTime - lastClickTime) < 1000){
                finish()
            } else {
                toast("再按一次退出")
            }
            lastClickTime = currentClickTime
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return setMenuItemClick(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return setMenuItemClick(item)
    }
    private fun setMenuItemClick(item: MenuItem):Boolean {
        if (item.itemId == R.id.menuSearch) {
            toast("搜索")
            return false
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        return when(item.itemId){
            R.id.navigation_welfare ->{
                setFragment(item.itemId)
                toast("妹子图~~")
                true
            }
            R.id.menuHot ->  {
                toast("热门")
                true
            }
            R.id.navigation_home->{
                setFragment(item.itemId)
                toast("this is home")
                true
            }
            R.id.navigation_type->{
                setFragment(item.itemId)
                toast("this is type")
                true
            }
            R.id.nav_like -> {
                toast("I lick")
                true
            }
            R.id.nav_about -> {
                var intent = Intent(this,AboutActivity::class.java)
                startActivity(intent)
                toast("about")
                true
            }
            else ->{false}
        }
    }
    private val onNavigationItemSelectedListener = OnNavigationItemSelectedListener {
        setMenuItemClick(it)
    }
    private fun setFragment(index:Int) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        fragmentManager.beginTransaction().apply {
            homeFragment ?: let {
                HomeFragment().let {
                    homeFragment = it
                    add(R.id.content,it)
                }
                typeFragment ?: let {
                    TypeFragment().let {
                        typeFragment = it
                        add(R.id.content,it)
                    }
                }
                welfareFragment?:let {
                    WelfareFragment().let {
                        welfareFragment = it
                        add(R.id.content,it)
                    }
                }
            }
            hideFragment(this)
            when (index){
                R.id.navigation_home -> {
                    toolBar.title = "android知识"
                    homeFragment.let {
                        this.show(it)
                    }
                }
                R.id.navigation_type->{
                    toolBar.title = "知识分类"
                    typeFragment.let {
                        this.show(it)
                    }
                }
                R.id.navigation_welfare->{
                    toolBar.title = "妹子图"
                    welfareFragment.let {
                        this.show(it)
                    }
                }
            }

        }.commit()
    }

    private fun hideFragment(transaction: FragmentTransaction) {
        homeFragment.let {
            transaction.hide(it)
        }
        typeFragment.let {
            transaction.hide(it)
        }
        welfareFragment.let {
            transaction.hide(it)
        }
    }
}
