package edu.uoc.pac3.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.uoc.pac3.R
import edu.uoc.pac3.data.streams.Stream
import kotlinx.android.synthetic.main.item_stream.view.*


class StreamsRecyclerAdapter(val streams: MutableList<Stream>): RecyclerView.Adapter<StreamsRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(position: Int) = itemView.run {
            val stream = streams[position]
            if (!stream.thumbnailUrl.isNullOrBlank()){
                val newThumbnailUrl: String = stream.thumbnailUrl.replace("{width}", "640").replace("{height}", "480")
                Picasso.get().load(newThumbnailUrl).into(imageViewStream)
            }
            if (!stream.title.isNullOrBlank()){
                textViewTitle.text = stream.title
            }
            if (!stream.userName.isNullOrBlank()){
                textViewUsername.text = stream.userName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_stream, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = streams.size

    fun updateStreams(streams: List<Stream>){
        this.streams.clear()
        this.streams.addAll(streams)
        notifyDataSetChanged()
    }

    fun addStreams(streams: List<Stream>){
        val previousSize = this.streams.size
        this.streams.addAll(streams)
        notifyItemRangeInserted(previousSize, streams.size)
    }

}