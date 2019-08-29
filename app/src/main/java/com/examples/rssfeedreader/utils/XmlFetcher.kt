package com.examples.rssfeedreader.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception

object XmlFetcher {

    @Throws(Exception::class)
    fun fetchXml(url: String): String {
        val httpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        val response = httpClient.newCall(request).execute()
        return response.body!!.string()
    }
}