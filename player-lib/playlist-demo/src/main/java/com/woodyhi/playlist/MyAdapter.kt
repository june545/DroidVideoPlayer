package com.woodyhi.playlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.woodyhi.playlist.model.Trailer

/**
 * @auth June
 * @date 2019/06/17
 */
class MyAdapter(private val listData: List<Trailer>) : RecyclerView.Adapter<MyAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_play_list, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Glide.with(holder.imageView.context).load(listData[position].coverImg).into(holder.imageView)
        holder.title.text = listData[position].videoTitle
        holder.rating.text = "${listData[position].rating}"
        holder.movieName.text = listData[position].movieName
        holder.type.text = listData[position].type?.joinToString()
        holder.itemView.setOnClickListener {
            var ctx = holder.itemView.context
            var intent = Intent(ctx, VideoPlayerActivity::class.java)
            intent.data = Uri.parse(listData[position].url)
            ctx.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var title: TextView
        var rating: TextView
        var movieName: TextView
        var type: TextView

        init {
            imageView = itemView.findViewById(R.id.imageView)
            title = itemView.findViewById(R.id.title)
            rating = itemView.findViewById(R.id.rating)
            movieName = itemView.findViewById(R.id.movieName)
            type = itemView.findViewById(R.id.type)
        }
    }
}
