package tech.ctawave.exoApp.ui.mediadetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import dagger.hilt.android.AndroidEntryPoint
import tech.ctawave.exoApp.data.entities.StreamingFormat
import tech.ctawave.exoApp.databinding.PlaybackFragmentBinding
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.ResolvingDataSource
import kotlinx.coroutines.flow.collect

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
            viewModel.streamingFormat = StreamingFormat.fromString(it)
        }

        addObservers()
    }

    private fun initializePlayer() {
        if (this.exoPlayer == null) {
            // init CMCD
            viewModel.initCMCD()

            val httpDataSourceFactory = DefaultHttpDataSourceFactory()
            val dataSourceFactory: DataSource.Factory = ResolvingDataSource.Factory(
                httpDataSourceFactory,  // Provide just-in-time URI resolution logic.
                { dataSpec: DataSpec -> dataSpec.withUri(viewModel.createCMCDCompliantUri(dataSpec)) })
            val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)

            exoPlayer = SimpleExoPlayer.Builder(requireContext()).setMediaSourceFactory(mediaSourceFactory).build()
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
            viewModel.pollPlayhead()
        }
    }

    private fun addObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.lastUri.collect {
                binding.playerStateContainer.lastUri.text = it
            }
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
        println("$$$ fragment > cancel")
        releasePlayer()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        println("$$$ fragment > cancel")
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
