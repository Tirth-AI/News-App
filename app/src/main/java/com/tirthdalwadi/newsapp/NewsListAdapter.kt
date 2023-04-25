package com.tirthdalwadi.newsapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class NewsListAdapter(private val listener: NewsItemClicked): RecyclerView.Adapter<NewsViewHolder>() {

    private val items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_items, parent, false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        holder.source.text = currentItem.source
        holder.updatedTime.text = currentItem.publishedDate
        Log.d("NewsAdapter","Before Glide: ${currentItem.imageURL}")
        if(currentItem.imageURL != null.toString())
        {
            Glide.with(holder.itemView).load(currentItem.imageURL).listener(object: RequestListener<Drawable>{
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("NewsAdapter","Glide successful")
                return false
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("NewsAdapter","Glide unsuccessful")
                return false
            }


        }).into(holder.itemView.findViewById(R.id.iv_newsImage))
        }
        Log.d("NewsAdapter", "After Glide")
    }

    fun updateNews(updatedNews: ArrayList<News>)
    {
        items.clear()
        items.addAll(updatedNews)
        notifyDataSetChanged()
    }
}

class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val titleView: TextView = itemView.findViewById(R.id.tv_newsFeed)
    val source: TextView = itemView.findViewById(R.id.tv_source)
    val updatedTime: TextView = itemView.findViewById(R.id.tv_updatedTime)
    val newsImage: ImageView = itemView.findViewById(R.id.iv_newsImage)
}

interface NewsItemClicked{
    fun onItemClicked(item: News)
}