package tech.ctawave.exoApp.ui.medialist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.ctawave.exoApp.data.entities.Media
import tech.ctawave.exoApp.R
import tech.ctawave.exoApp.databinding.MediaLayoutBinding

class MediaListAdapter(private val listener: MediaListListener): RecyclerView.Adapter<MediaListAdapter.DataViewHolder>() {

    interface MediaListListener {
        fun onMediaClick(uri: String)
    }

    class DataViewHolder(itemView: View, private val listener: MediaListListener): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val binding = MediaLayoutBinding.bind(itemView)
        private lateinit var media: Media

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onMediaClick(media.uri)
        }

        fun bind(media: Media) {
            println("$$$ bind > media=$media")
            this.media = media
            with(binding) {
                mediaTitle.text = media.title
                mediaUri.text = media.uri
            }
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
