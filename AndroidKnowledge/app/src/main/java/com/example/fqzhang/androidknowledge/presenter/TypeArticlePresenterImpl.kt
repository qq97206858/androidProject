package com.example.fqzhang.androidknowledge.presenter
import com.example.fqzhang.androidknowledge.bean.ArticleListResponse
import com.example.fqzhang.androidknowledge.model.TypeArticleModel
import com.example.fqzhang.androidknowledge.ui.fragment.TypeArticleFragment

/**
 * Created by fqzhang on 2018/4/21.
 */
class TypeArticlePresenterImpl(var typeArticleFragment: TypeArticleFragment): TypeArticlePresenter {//typeArticleFragment 传入有点大 ，可以通过接口，其操作限制
    override fun getTypeArticleList(page: Int , cid: Int) {
       model.getTypeArticleList(this,page,cid)
    }

    private val model = TypeArticleModel()


    override fun getTypeArticleListSuccess(result: ArticleListResponse) {//
        if (result.errorCode != 0) {
            typeArticleFragment.getTypeArticleListFailed(result.errorMsg)
        }
        var total = result.data.total
        if (total == 0) {
            typeArticleFragment.getTypeArticleListZero()
            return
        }
        if (result.data.curPage >= result.data.pageCount) {
            //已经没有文章了
        }
        if (total <= result.data.size) {//文章总数小于20条
            typeArticleFragment.getTypeArticleListSmall(result)
            return
        }
        typeArticleFragment.getTypeArticleListSuccess(result)
    }

    override fun getTypeArticleListFailed(errorMsg: String) {
        typeArticleFragment.getTypeArticleListFailed(errorMsg)
    }
}