package com.examples.rssfeedreader.model

import androidx.databinding.BaseObservable

object RssFeedModel {

    data class RssFeed(
        var rssArticles: MutableList<RssArticle>
    ): BaseObservable()

    data class RssArticle(
        var author: String? = null,
        var content: String? = null,
        var description: String? = null,
        var guid: String? = null,
        var imageUrl: String? = null,
        var link: String? = null,
        var publishedDate: String? = null,
        var title: String? = null,
        private var _categories: MutableList<String> = mutableListOf()
    ) {

        val categories: MutableList<String>
            get() = _categories

        fun addCategory(category: String) {
            _categories.add(category)
        }
    }
}