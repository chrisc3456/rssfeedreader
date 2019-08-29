package com.examples.rssfeedreader.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.examples.rssfeedreader.model.RssFeedModel
import com.examples.rssfeedreader.utils.XmlFetcher
import com.examples.rssfeedreader.utils.XmlRssArticleParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    val rssFeedLiveData = MutableLiveData<RssFeedModel.RssFeed>()

    fun getRssFeedArticles(url: String) {

        GlobalScope.launch(Dispatchers.IO) {
            val xml = withContext(Dispatchers.Default) {
                XmlFetcher.fetchXml(url)
            }

            val rssArticles: MutableList<RssFeedModel.RssArticle> = mutableListOf()
            XmlRssArticleParser.getArticlesFromXml(rssArticles, xml)

            rssFeedLiveData.postValue(RssFeedModel.RssFeed((rssArticles)))
        }
    }
}