package com.example.fqzhang.androidknowledge

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fqzhang.androidknowledge.constant.Constant
import com.just.agentweb.AgentWeb
import com.just.agentweb.ChromeClientCallbackManager
import kotlinx.coroutines.experimental.Deferred

/**
 * Created by fqzhang on 2018/3/28.
 */

/**
 * 打印异常信息
 */
fun loge(tag:String,content:String?){
    Log.e(tag,content?:tag)
}

fun String.getAgentWeb(
        activity: Activity, webContent: ViewGroup,
        layoutParams: ViewGroup.LayoutParams,
        receivedTitleCallback: ChromeClientCallbackManager.ReceivedTitleCallback?
) = AgentWeb.with(activity)
        .setAgentWebParent(webContent,layoutParams)
        .useDefaultIndicator()
        .defaultProgressBarColor()
        .setReceivedTitleCallback(receivedTitleCallback)
        .createAgentWeb()
        .ready()
        .go(this)
fun Deferred<Any>?.cancelByActive() = this?.run {
    tryCatch {
        if (isActive){
            cancel()
        }
    }
}
fun Context.toast(content:String?){
    Constant.showToast?.apply {
        setText(content)
        show()
    } ?:let {
        Toast.makeText(this.applicationContext,content,Toast.LENGTH_LONG).apply {
            Constant.showToast = this
        }.show()
    }
}
fun Context.inflate(layoutId:Int): View {
    return LayoutInflater.from(this).inflate(layoutId,null)
}
fun tryCatch(catchBlock:(Throwable)-> Unit = {},tryBlock:() -> Unit){
    try {
        tryBlock()
    }catch (e:Throwable) {
        catchBlock(e)
    }

}
