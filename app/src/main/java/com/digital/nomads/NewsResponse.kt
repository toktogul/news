package com.digital.nomads

import java.util.*

class NewsResponse(val status: String, val articles: List<Article>) {
    companion object {
        val STATUS_OK = "ok"
    }
}

class Article(
    val title: String? = null,
    val description: String? = null,
    val publishedAt: Date? = null,
    val urlToImage: String? = null,
    val url: String? = null
)