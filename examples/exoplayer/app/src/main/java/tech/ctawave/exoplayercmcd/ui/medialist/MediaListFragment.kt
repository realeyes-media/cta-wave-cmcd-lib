package tech.ctawave.exoplayercmcd.ui.medialist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.media_list_fragment.*
import tech.ctawave.exoplayercmcd.R
import tech.ctawave.exoplayercmcd.data.entities.Media
import tech.ctawave.exoplayercmcd.util.Resource

@AndroidEntryPoint
class MediaListFragment : Fragment(), MediaListAdapter.MediaListListener {

    private val viewModel: MediaListViewModel by viewModels()
    private lateinit var adapter: MediaListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.media_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObserver()
    }

    override fun onMediaClick(uri: String) {
        findNavController().navigate(R.id.action_MediaListFragment_to_SecondFragment, bundleOf("media" to uri))
    }

    private fun setupUI() {
        adapter = MediaListAdapter(this)
        mediaListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mediaListRecyclerView.addItemDecoration(DividerItemDecoration(mediaListRecyclerView.context, (mediaListRecyclerView.layoutManager as LinearLayoutManager).orientation))
        mediaListRecyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.media.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    mediaListProgressBar.visibility = View.GONE
                    it.data?.let { media -> renderList(media) }
                    mediaListRecyclerView.visibility = View.VISIBLE
                }
                Resource.Status.LOADING -> {
                    mediaListProgressBar.visibility = View.VISIBLE
                    mediaListRecyclerView.visibility = View.GONE
                }
                Resource.Status.ERROR -> {
                    mediaListProgressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun renderList(media: List<Media>) {
        adapter.setMedia(media)
    }
}
