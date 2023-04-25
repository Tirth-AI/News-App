package com.tirthdalwadi.newsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible


class WebNewsActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_news)
//        val progressBar: ProgressBar = findViewById(R.id.progressBar_WebNews)
//        progressBar.isVisible = true

        val browseIntent: Intent = intent
        val newsURL = browseIntent.getStringExtra("newsURL")
        if (newsURL != null) {
            Log.d("WebNewsActivity", newsURL)
            val webNewsView: WebView = findViewById(R.id.webView_WebNews)
            val settings: WebSettings = webNewsView.settings
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webNewsView.loadUrl(newsURL.toString())
        } else {
            Toast.makeText(this, "Website not available!!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}