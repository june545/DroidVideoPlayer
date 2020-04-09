package com.woodyhi.playlist

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.bumptech.glide.request.RequestOptions
import com.woodyhi.player.vlc.DefaultVlcPlayerActivity
import com.woodyhi.playlist.databinding.ItemPlayListBinding
import com.woodyhi.playlist.model.Trailer
import kotlinx.android.synthetic.main.item_play_list.view.*


/**
 * @auth June
 * @date 2019/06/17
 */
class MyAdapter(private val listData: List<Trailer>) : androidx.recyclerview.widget.RecyclerView.Adapter<MyAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val viewBinding = ItemPlayListBinding.inflate(LayoutInflater.from(parent.context));
        return VH(viewBinding, viewBinding.root)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val trailer = listData[position]
        holder.itemView.apply {
            Glide.with(imageView.context)
                    .load(trailer.coverImg)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20))
                            .placeholder(R.drawable.placeholder))
                    .into(imageView)
            title.text = trailer.videoTitle
            type.text = trailer.type.joinToString()
            this.setOnClickListener {
                var ctx = it.context
                var intent = Intent(ctx, DefaultVlcPlayerActivity::class.java)
                intent.data = Uri.parse(listData[position].url)
                ctx.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class VH(var viewBinding: ItemPlayListBinding, itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    }
}
