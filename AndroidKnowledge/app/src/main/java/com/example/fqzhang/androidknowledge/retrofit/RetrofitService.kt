package com.example.fqzhang.androidknowledge.retrofit

import com.example.fqzhang.androidknowledge.bean.*
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by fqzhang on 2018/4/7.
 * retrofit请求API
 */
interface RetrofitService {
    /**
     * 首页数据
     * http://www.wanandroid.com/article/list/0/json
     * @param page page
     */
    @GET("/article/list/{page}/json")
    fun getHomeList(@Path("page")page:Int):Deferred<HomeListResponse>

    /**
     * 首页Banner
     * @return BannerResponse
     */
    @GET("/banner/json")
    fun getBanner(): Deferred<BannerResponse>

    @GET("/tree/json")
    fun getKnowledgeTree():Deferred<KnowledgeTreeResponse>
    /**
     * 知识体系下的文章
     * http://www.wanandroid.com/article/list/0/json?cid=168
     * @param page page
     * @param cid cid
     */
    @GET("/article/list/{page}/json")
    fun getArticleList(
            @Path("page")page:Int,
            @Query("cid")cid:Int
    ):Deferred<ArticleListResponse>
    /**
     * 常用网站
     * http://www.wanandroid.com/friend/json
     */
    @GET("/friend/json")
    fun getFriendList(): Deferred<FriendListResponse>

    //域名下的第一 “/”可以在baseUrl末尾以及这里都加，如果不是则会有问题
    @GET("/api/data/福利/20/{page}")
    fun getWelfareList( @Path("page")page:Int): Deferred<WelfareListResponse>
}