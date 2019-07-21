package com.digital.nomads.ui.articles

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.digital.nomads.R
import com.digital.nomads.ui.WebViewActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), NewsClickListener {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("MainActivity", "onCreate: $viewModel")
        viewModel.firstLoad()
        initAdapter()
    }

    private fun initAdapter() {
        MainAdapter(this).also {
            root.layoutManager = LinearLayoutManager(this)
            root.adapter = it
            adapter = it
        }
        viewModel.articles.observe(this, Observer(adapter::addItems))

        viewModel.networkState.observe(this, Observer {
            when (it!!) {
                NetworkState.OFFLINE -> adapter.addedOfflineMode()
                NetworkState.ONLINE -> adapter.removeOfflineMode()
            }
        })
    }

    override fun onItemClick(url: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("article_page", url)
        startActivity(intent)
    }

    override fun loadMore() {
        viewModel.loadMore()
    }

    override fun onOfflineButtonClick() {
        viewModel.reload()
    }
}
