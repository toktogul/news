package com.digital.nomads.ui.articles

open class AdapterItem
class NetworkErrorItem : AdapterItem()

class ArticleItem(
    val title: String,
    val description: String,
    val date: String,
    val image: String,
    val link: String
) : AdapterItem()