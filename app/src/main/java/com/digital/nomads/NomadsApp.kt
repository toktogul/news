package com.digital.nomads

import android.app.Application
import com.digital.nomads.ui.articles.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class NomadsApp : Application() {


    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            modules(module {
                single { ApiManager.createApi() }
                single { applicationContext }
                viewModel { MainViewModel(get(), ApiManager.API_KEY, get()) }
            })
        }
    }
}