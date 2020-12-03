package tech.ctawave.exoApp.ui.medialist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.ctawave.exoApp.data.entities.Media
import tech.ctawave.exoApp.data.repository.MediaRepository
import tech.ctawave.exoApp.util.Resource

class MediaListViewModel @ViewModelInject constructor(private val repository: MediaRepository): ViewModel() {

    private val media = MutableStateFlow<Resource<List<Media>>>(Resource.loading(null))

    fun fetchMedia() {
        viewModelScope.launch {
            repository.loadMedia("example_id")
                .catch { e ->
                    media.value = (Resource.error(e.toString(), null))
                }
                .collect {
                    media.value = it
                }
        }
    }

    fun getMedia(): StateFlow<Resource<List<Media>>> {
        return media
    }
}
