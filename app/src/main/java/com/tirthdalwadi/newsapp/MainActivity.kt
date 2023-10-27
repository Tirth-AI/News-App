package com.tirthdalwadi.newsapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter
    private var pageValue: String? = null
    private var pageNumberArray = arrayOfNulls<String>(100)
    private var currentIndex = -1
    private var insertIndex = 0
    private var flag = true


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "Activity Created")
        Log.d("MainActivity", "Before fetchData()")
        fetchData()
        Log.d("MainActivity", "After fetchData()")
        mAdapter = NewsListAdapter(this)
        val recyclerView: RecyclerView = findViewById(R.id.rv_newsRecyclerView)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.adapter = mAdapter

        val btnNext: Button = findViewById(R.id.btn_next)
        btnNext.setOnClickListener {
            flag = true
            fetchData()
        }

        val btnPrevious: Button = findViewById(R.id.btn_previous)
         btnPrevious.setOnClickListener {
            flag = false
             if(currentIndex > 0) {
                 currentIndex -= 1
                 insertIndex = currentIndex
                 fetchData()
             }
             else
                 Toast.makeText(this, "You are on the 1st page", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchData(){
        Log.d("MainActivity", "fetchData() entered")
        val url2 = "https://newsdata.io/api/1/news?apikey=pub_208640fdfece4ce5cc8286d8a99f5b10c087b&language=en&country=in"
        Log.d("MainActivity", "$insertIndex")
        var newURL = ""
        newURL = if(insertIndex == 0) {
            url2
        }else {
            url2 + "&page=" + pageNumberArray[insertIndex]
        }
        Log.d("MainActivity", "${newURL}")

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            newURL,
            null,
            { response ->
                Log.d("MainActivity", "Request successful")

                pageValue = response.getString("nextPage")
                Log.d("MainActivity", pageValue!!)

                if(flag)
                {
                    currentIndex += 1
                    insertIndex = currentIndex + 1
                    pageNumberArray[insertIndex] = pageValue!!.toString()
                }
                else
                    insertIndex++

                Log.d("MainActivity", currentIndex.toString())
                Log.d("MainActivity", pageNumberArray.get(insertIndex).toString())

                val jsonArray = response.getJSONArray("results")
                val newsArray = ArrayList<News>()
                for (i in 0 until jsonArray.length()) {
                    val jsonSingleObject = jsonArray.getJSONObject(i)
                    val currentNews = News(
                        jsonSingleObject.getString("title"),
                        jsonSingleObject.getString("link"),
                        jsonSingleObject.getString("image_url"),
                        jsonSingleObject.getString("pubDate"),
                        jsonSingleObject.getString("source_id")
                    )
                    newsArray.add(currentNews)
                }
                mAdapter.updateNews(newsArray)
            },
            {
                Log.d("MainActivity", "It is an error : $it")
            }
        )
        Log.d("MainActivity", "Request Object Created")
        MySingleton.getInstance(this.applicationContext).addToRequestQueue(jsonObjectRequest)
        Log.d("MainActivity", "Request Object added to the queue")
    }

    override fun onItemClicked(item: News) {
        Log.d("NewsURL", item.newsURL)
        val singleNewsIntent = Intent(this, WebNewsActivity::class.java)
        singleNewsIntent.putExtra("newsURL", item.newsURL)
        startActivity(singleNewsIntent)
    }
}

class MySingleton (context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: MySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MySingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}