package com.digital.nomads.ui.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.digital.nomads.R
import com.squareup.picasso.Picasso

class MainAdapter(private val listener: NewsClickListener) : RecyclerView.Adapter<MainViewHolder>() {
    private val ERROR_ITEM = 10

    private val items = mutableListOf<AdapterItem>()

    override fun getItemViewType(position: Int) = if (items[position] is NetworkErrorItem) ERROR_ITEM else 0

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        if (viewType == ERROR_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_error, parent, false)
            val vh = ErrorViewHolder(view)
            vh.itemView.setOnClickListener { listener.onOfflineButtonClick() }
            return vh
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
            val vh = ArticleViewHolder(view)
            vh.itemView.setOnClickListener {
                if (vh.adapterPosition != RecyclerView.NO_POSITION) {
                    val url = (items[vh.adapterPosition] as ArticleItem).link
                    listener.onItemClick(url)
                }
            }
            return vh
        }

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        when (holder) {
            is ArticleViewHolder -> {
                val item = items[position] as ArticleItem
                with(holder) {
                    title.text = "${position}_${item.title}"
                    desc.text = item.description
                    createdAt.text = item.date
                    if (item.image.isEmpty()) {
                        image.visibility = View.GONE
                    } else {
                        image.visibility = View.VISIBLE
                        Picasso.get()
                            .load(item.image)
                            .into(image)
                    }
                }
            }
            is ErrorViewHolder -> {

            }
        }

        if (itemCount == position + 5) listener.loadMore()
    }

    fun addItems(list: List<AdapterItem>?) {
        val startPosition = items.size
        items.addAll(list!!)
        notifyItemRangeInserted(startPosition, list.size)
    }

    fun removeOfflineMode() {
        val position = items.indexOfFirst { it is NetworkErrorItem }
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addedOfflineMode() {
        items.add(NetworkErrorItem())
        notifyItemInserted(items.size - 1)
    }
}

interface NewsClickListener {
    fun onItemClick(url: String)
    fun loadMore()
    fun onOfflineButtonClick()
}