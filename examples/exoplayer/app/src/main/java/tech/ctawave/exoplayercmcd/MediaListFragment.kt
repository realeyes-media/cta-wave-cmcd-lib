package tech.ctawave.exoplayercmcd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.viewModels
import tech.ctawave.exoplayercmcd.viewmodel.MediaListViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MediaListFragment : Fragment() {

    private val viewModel: MediaListViewModel by viewModels()
    private lateinit var mediaListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.media_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaListView = view.findViewById(R.id.listview_media)

        viewModel.media.observe(viewLifecycleOwner) {
            // update mediaListView
        }
    }
}
