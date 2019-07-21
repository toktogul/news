package com.digital.nomads.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.digital.nomads.R
import com.digital.nomads.ui.articles.MainActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Handler().postDelayed({
            launchMainScreen()

        }, 1000)
    }

    private fun launchMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
