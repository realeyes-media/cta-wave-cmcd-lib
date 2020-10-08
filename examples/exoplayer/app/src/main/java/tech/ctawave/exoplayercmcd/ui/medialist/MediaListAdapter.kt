package tech.ctawave.exoplayercmcd.ui.medialist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.media_layout.view.*
import tech.ctawave.exoplayercmcd.data.entities.Media
import tech.ctawave.exoplayercmcd.R

class MediaListAdapter(private val listener: MediaListListener): RecyclerView.Adapter<MediaListAdapter.DataViewHolder>() {

    interface MediaListListener {
        fun onMediaClick(id: String)
    }

    class DataViewHolder(itemView: View, private val listener: MediaListListener): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private lateinit var media: Media

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onMediaClick(media.id)
        }

        fun bind(media: Media) {
            println("$$$ bind > media=$media")
            this.media = media
            itemView.mediaTitle.text = media.title
            itemView.mediaUri.text = media.uri
        }
    }

    private val media = arrayListOf<Media>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder = DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.media_layout, parent, false), listener)

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(media[position])
    }

    override fun getItemCount(): Int = media.size

    fun setMedia(media: List<Media>) {
        this.media.clear()
        this.media.addAll(media)
        notifyDataSetChanged()
    }

}
