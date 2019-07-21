package com.digital.nomads.ui.articles

import android.content.Context
import android.net.ConnectivityManager
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.digital.nomads.Article
import com.digital.nomads.NewsApi
import com.digital.nomads.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val api: NewsApi,
    private val apiKey: String,
    private val context: Context
) : ViewModel() {

    private val totalPages = 5
    private var page = 1
    private var isLoading = false
    private var allItemsLoaded = false
    private val _articles: MutableLiveData<List<AdapterItem>> = MutableLiveData()
    private val _networkState: MutableLiveData<NetworkState> = MutableLiveData()
    private val wholeList = mutableListOf<AdapterItem>()

    val articles: LiveData<List<AdapterItem>> = _articles
    val networkState: LiveData<NetworkState> get() = _networkState

    fun firstLoad() {
        if (wholeList.isEmpty()) {
            load()
        } else {
            _articles.value = wholeList
        }
    }

    private fun load() {
        val map = buildQueryParams()
        api.getNews(map).enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
                t.printStackTrace()
                isLoading = false
                _networkState.value = NetworkState.OFFLINE
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                Log.e("MainViewModel", "onResponse: ${response.isSuccessful}")
                page++
                if (response.isSuccessful) {
                    val body = response.body()!!
                    if (body.status == NewsResponse.STATUS_OK) {
                        val mappedList = body.articles.map {
                            convertData(it)
                        }
                        _articles.value = mappedList
                        wholeList.addAll(mappedList)

                    } else {
                        // report unknown error
                    }
                } else {
                    Log.e("MainViewModel", "onResponse: ${response.message()}")
                }
                isLoading = false
                if (page > totalPages) allItemsLoaded = true
            }

        })
    }

    private fun convertData(it: Article): ArticleItem {
        val date = if (it.publishedAt != null) {
            DateUtils.getRelativeTimeSpanString(
                it.publishedAt.time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
            )
        } else ""

        return ArticleItem(
            it.title ?: "",
            it.description ?: "",
            date.toString(),
            it.urlToImage ?: "",
            it.url ?: ""
        )
    }

    fun loadMore() {
        if (allItemsLoaded || isLoading) return
        isLoading = true
        load()
    }

    private fun buildQueryParams(): Map<String, Any> {
        return mapOf(
            "q" to "android",
            "from" to "2019-04-00",
            "sortBy" to "publishedAt",
            "apiKey" to apiKey,
            "page" to page
        )
    }

    fun reload() {
        if (hasInternet()) {
            _networkState.value = NetworkState.ONLINE
            load()
        }
    }

    private fun hasInternet(): Boolean {
        var isConnected = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

}


enum class NetworkState {
    OFFLINE, ONLINE
}