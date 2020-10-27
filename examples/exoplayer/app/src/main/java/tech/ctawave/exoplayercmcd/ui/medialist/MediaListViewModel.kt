package tech.ctawave.exoplayercmcd.ui.medialist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tech.ctawave.exoplayercmcd.data.entities.Media
import tech.ctawave.exoplayercmcd.data.repository.MediaRepository
import tech.ctawave.exoplayercmcd.util.Resource

class MediaListViewModel @ViewModelInject constructor(repository: MediaRepository): ViewModel() {
    val media: LiveData<Resource<List<Media>>> = repository.loadMedia("example_id")
}
