package tech.ctawave.exoApp.ui.medialist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.ctawave.exoApp.R
import tech.ctawave.exoApp.data.entities.Media
import tech.ctawave.exoApp.data.entities.StreamingFormat
import tech.ctawave.exoApp.databinding.MediaListFragmentBinding
import tech.ctawave.exoApp.util.Resource

@AndroidEntryPoint
class MediaListFragment : Fragment(), MediaListAdapter.MediaListListener {

    private var _binding: MediaListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MediaListViewModel by viewModels()
    private lateinit var adapter: MediaListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = MediaListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchMedia()

        setupUI()
        setupObserver()
    }

    override fun onMediaClick(uri: String, id: String, format: String) {
        findNavController().navigate(R.id.action_MediaListFragment_to_PlaybackFragment, bundleOf("media" to uri, "id" to id, "format" to format))
    }

    private fun setupUI() {
        val recyclerView = binding.mediaListRecyclerView
        adapter = MediaListAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, (recyclerView.layoutManager as LinearLayoutManager).orientation))
        recyclerView.adapter = adapter
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            val media = viewModel.getMedia()
            media.collect {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        binding.mediaListProgressBar.visibility = View.GONE
                        it.data?.let { media -> renderList(media) }
                        binding.mediaListRecyclerView.visibility = View.VISIBLE
                    }
                    Resource.Status.LOADING -> {
                        binding.mediaListProgressBar.visibility = View.VISIBLE
                        binding.mediaListRecyclerView.visibility = View.GONE
                    }
                    Resource.Status.ERROR -> {
                        binding.mediaListProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun renderList(media: List<Media>) {
        adapter.setMedia(media)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
