package tech.ctawave.exoApp.ui.mediadetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.ctawave.exoApp.data.entities.StreamingFormat
import tech.ctawave.exoApp.databinding.PlaybackFragmentBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class PlaybackFragment : Fragment() {

    private var _binding: PlaybackFragmentBinding? = null
    private val binding get() = _binding!!

    private var exoPlayer: SimpleExoPlayer? = null
    private var mediaUri: String? = null
    private val viewModel: PlaybackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = PlaybackFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("media")?.let {
            println("$$$ PlaybackFragment > uri=$it")
            mediaUri = it
        }
        arguments?.getString("id")?.let {
            println("$$$ PlaybackFragment > id=$it")
            viewModel.contentId = it
        }
        arguments?.getString("format")?.let {
            println("$$$ PlaybackFragment > format=$it")
            viewModel.streamingFormat = when (it) {
                StreamingFormat.HLS.toString() -> StreamingFormat.HLS
                StreamingFormat.MPEG_DASH.toString() -> StreamingFormat.MPEG_DASH
                StreamingFormat.SMOOTH_STREAMING.toString() -> StreamingFormat.SMOOTH_STREAMING
                else -> null
            }
        }
        setupObserver()
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.currentBitrate.collect {
                binding.playerStateContainer.currentBitrateTxt.text = it
            }
            viewModel.currentFramerate.collect {
                binding.playerStateContainer.currentFramerateTxt.text = it
            }
            viewModel.currentPlaylistUri.collect {
                binding.playerStateContainer.currentPlaylistUriTxt.text = it
            }
            viewModel.currentPlayhead.collect {
                binding.playerStateContainer.currentPlayheadTxt.text = it
            }
        }
    }

    private fun initializePlayer() {
        if (this.exoPlayer == null) {
            // init CMCD
            viewModel.initCMCD()

            exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
            mediaUri?.let {
                MediaItem.fromUri(it)
            }?.let {
                exoPlayer?.setMediaItem(it)
            }
            binding.playerView.player = exoPlayer
            exoPlayer?.let { viewModel.setPlayer(it) }
            exoPlayer?.addListener(viewModel)
            exoPlayer?.addAnalyticsListener(viewModel)
            exoPlayer?.playWhenReady = true
            exoPlayer?.prepare()
            viewModel.poll()
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        initializePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        exoPlayer?.let {
            it.removeListener(viewModel)
            it.removeAnalyticsListener(viewModel)
            it.release()
        }
        exoPlayer = null
    }
}
