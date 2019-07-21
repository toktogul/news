package com.digital.nomads.ui.articles

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.digital.nomads.R

sealed class MainViewHolder(view: View) : RecyclerView.ViewHolder(view)

class ArticleViewHolder(view: View) : MainViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.title)!!
    val desc = view.findViewById<TextView>(R.id.description)!!
    val createdAt = view.findViewById<TextView>(R.id.createdAt)!!
    val image = view.findViewById<ImageView>(R.id.imageView)
}


class ErrorViewHolder(view: View) : MainViewHolder(view)