package com.examples.rssfeedreader.view

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.examples.rssfeedreader.R
import com.examples.rssfeedreader.databinding.ListItemArticleBinding
import com.examples.rssfeedreader.model.RssFeedModel
import org.joda.time.DateTimeComparator
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class RssFeedListAdapter: RecyclerView.Adapter<RssFeedListAdapter.ListViewHolder>() {

    private var rssArticles: MutableList<RssFeedModel.RssArticle> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemArticleBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.list_item_article, parent, false)

        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return rssArticles.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val article = rssArticles[position]

        holder.binding.title = article.title
        holder.binding.published = getDateAsText(article.publishedDate!!)
        holder.binding.categories = article.categories.joinToString(", ", "", "", 4, "...")
        holder.binding.description = Html.fromHtml(article.description, Html.FROM_HTML_MODE_COMPACT)
            .toString().take(250) + "..."
    }

    private fun getDateAsText(date: String): String {

        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        val formattedDate = Date.from(LocalDate.parse(date, formatter).atStartOfDay().toInstant(
            ZoneOffset.UTC))

        val calendar = Calendar.getInstance()
        val today = calendar.time
        calendar.add(Calendar.DATE, -1)
        val yesterday = calendar.time
        val dateTimeComparator: DateTimeComparator = DateTimeComparator.getDateOnlyInstance()

        return when {
            dateTimeComparator.compare(formattedDate, today) == 0 -> "Today"
            dateTimeComparator.compare(formattedDate, yesterday) == 0 -> "Yesterday"
            else -> SimpleDateFormat("dd MMM").format(formattedDate)
        }
    }

    fun setRssFeedArticles(articles: MutableList<RssFeedModel.RssArticle>) {
        this.rssArticles = articles
        notifyDataSetChanged()
    }

    class ListViewHolder(val binding: ListItemArticleBinding) : RecyclerView.ViewHolder(binding.root)
}