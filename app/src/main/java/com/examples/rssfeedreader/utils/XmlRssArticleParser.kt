package com.examples.rssfeedreader.utils

import com.examples.rssfeedreader.model.RssFeedModel
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader
import java.util.*
import java.util.regex.Pattern

object XmlRssArticleParser {

    @Throws(XmlPullParserException::class, IOException::class)
    fun getArticlesFromXml(articleList: MutableList<RssFeedModel.RssArticle>, xml: String) {

        val parser = getXmlParser(xml)
        var eventType = parser.eventType
        var article = RssFeedModel.RssArticle()

        // Indicator to mark when an item is reached within the XML from which to build article data
        var foundItem = false

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (foundItem) {
                    updateArticleData(article, parser)
                }
                else {
                    foundItem = parser.name.toLowerCase(Locale.ENGLISH) == RssKeywords.RSS_ITEM
                }
            }
            // When we've reached the end of an item tag, add to the list and prepare for the next one
            else if (eventType == XmlPullParser.END_TAG && parser.name.equals("item", ignoreCase = true)) {
                articleList.add(article)
                article = RssFeedModel.RssArticle()
                foundItem = false
            }

            eventType = parser.next()
        }
    }

    private fun updateArticleData(article: RssFeedModel.RssArticle, parser: XmlPullParser) {

        val elementName = parser.name.toLowerCase(Locale.ENGLISH)
        val elementValue = parser.nextText().trim()

        when (elementName) {
            RssKeywords.RSS_ITEM_AUTHOR -> article.author = elementValue
            RssKeywords.RSS_ITEM_CATEGORY -> article.addCategory(elementValue)
            RssKeywords.RSS_ITEM_CONTENT -> article.content = elementValue
            RssKeywords.RSS_ITEM_DESCRIPTION -> article.description = elementValue
            RssKeywords.RSS_ITEM_GUID -> article.guid = elementValue
            RssKeywords.RSS_ITEM_LINK -> article.link = elementValue
            RssKeywords.RSS_ITEM_PUB_DATE -> article.publishedDate = elementValue
            RssKeywords.RSS_ITEM_THUMBNAIL -> article.imageUrl = parser.getAttributeValue(null, RssKeywords.RSS_ITEM_URL)
            RssKeywords.RSS_ITEM_TITLE -> article.title = elementValue
        }
    }

    private fun getXmlParser(xml: String): XmlPullParser {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = false

        val parser = factory.newPullParser()
        parser.setInput(StringReader(xml))

        return parser
    }
}