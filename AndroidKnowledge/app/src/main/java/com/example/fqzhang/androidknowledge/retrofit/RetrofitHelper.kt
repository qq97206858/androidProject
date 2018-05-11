package com.example.fqzhang.androidknowledge.retrofit

import com.example.fqzhang.androidknowledge.constant.Constant
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by fqzhang on 2018/4/7.
 */
object RetrofitHelper {
    private val CONNECT_TIMEOUT = 30L
    private val READ_TIMEOUT = 10L
    val retrofitService: RetrofitService = getService(Constant.REQUEST_BASE_URL,RetrofitService::class.java)
    val gankService: RetrofitService = getService(Constant.GANK_BASE_URL,RetrofitService::class.java)
    private fun create(url:String):Retrofit{
        var okHttpClientBuilder = OkHttpClient().newBuilder()
        okHttpClientBuilder.connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(READ_TIMEOUT,TimeUnit.SECONDS)
/*        okHttpClientBuilder.addInterceptor{
            val request = it.request()
            val requestUrl = request.url().toString()
            val domain = request.url().host()

        }*/
        //先不添加拦截器
        return Retrofit.Builder().apply {
            baseUrl(url)
            client(okHttpClientBuilder.build())
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }.build()
    }
    private fun <T> getService(url:String,service:Class<T>): T = create(url).create(service)
}
