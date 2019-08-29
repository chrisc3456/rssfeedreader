package com.examples.rssfeedreader.view

import android.app.Activity
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer
import com.examples.rssfeedreader.R
import com.examples.rssfeedreader.viewmodel.MainActivityViewModel
import androidx.lifecycle.ViewModelProviders


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private val rssFeedListAdapter = RssFeedListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        addObservers()

        setContentView(R.layout.activity_main)
        setupRecycler()

        // Trigger the fetching of feed data on button press and swipe-to-refresh
        buttonGo.setOnClickListener { fetchFeed() }
        refreshLayoutMain.setOnRefreshListener { fetchFeed() }
    }

    private fun setupRecycler() {
        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this,R.drawable.divider_horizontal_trans)!!)
        recyclerArticles.addItemDecoration(itemDecorator)

        recyclerArticles.adapter = rssFeedListAdapter
        recyclerArticles.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun addObservers() {
        viewModel.rssFeedLiveData.observe(this, Observer { rssFeed ->
            rssFeedListAdapter.setRssFeedArticles(rssFeed.rssArticles)
        })
    }

    private fun fetchFeed() {
        var feedUrl: String = textInputFeed.text.toString()
        if (!feedUrl.startsWith("http://") && !feedUrl.startsWith("https://")) {
            feedUrl = "https://$feedUrl"
        }

        viewModel.getRssFeedArticles(feedUrl)

        // Stops the progress wheel spinning and closes the keyboard
        refreshLayoutMain.isRefreshing = false
        val manager = applicationContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(textInputFeed.windowToken, 0)
    }
}
