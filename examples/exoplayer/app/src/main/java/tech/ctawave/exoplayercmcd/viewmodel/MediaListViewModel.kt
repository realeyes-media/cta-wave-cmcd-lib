package tech.ctawave.exoplayercmcd.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tech.ctawave.exoplayercmcd.vo.Media
import tech.ctawave.exoplayercmcd.repository.MediaListRepository
import tech.ctawave.exoplayercmcd.vo.Resource
import javax.inject.Inject

class MediaListViewModel @Inject constructor(repository: MediaListRepository): ViewModel() {
    val media: LiveData<Resource<List<Media>>> = repository.loadAllMedia("example_id")
}
