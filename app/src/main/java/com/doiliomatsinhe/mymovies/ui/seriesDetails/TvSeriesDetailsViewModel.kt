package com.doiliomatsinhe.mymovies.ui.seriesDetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doiliomatsinhe.mymovies.data.Repository
import com.doiliomatsinhe.mymovies.model.*
import com.doiliomatsinhe.mymovies.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TvSeriesDetailsViewModel @ViewModelInject
constructor(private val repository: Repository) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _listOfGenres = MutableLiveData<List<TvGenres>>()
    val listOfGenres: LiveData<List<TvGenres>>
        get() = _listOfGenres


    init {
        uiScope.launch {
            _listOfGenres.value = when (val tvGenres = repository.getTvGenres()) {
                is Result.Success -> tvGenres.data
                is Result.Error -> null
            }
        }
    }

    fun getTvReview(tvId: Int): LiveData<List<TvReview>> {
        val reviewList = MutableLiveData<List<TvReview>>()

        uiScope.launch {
            reviewList.value = when (val tvReviews = repository.getTvReviews(tvId)) {
                is Result.Success -> tvReviews.data
                is Result.Error -> null
            }
        }
        return reviewList
    }

    fun getTvTrailers(tvId: Int): LiveData<List<TvTrailer>> {
        val trailers = MutableLiveData<List<TvTrailer>>()

        uiScope.launch {
            trailers.value = when (val result = repository.getTvTrailer(tvId)) {
                is Result.Success -> result.data
                is Result.Error -> null
            }
        }
        return trailers
    }

    fun getTvCast(tvId: Int): LiveData<List<TvCast>> {
        val castMembers = MutableLiveData<List<TvCast>>()

        uiScope.launch {
            castMembers.value = when (val result = repository.getTvCast(tvId)) {
                is Result.Success -> result.data.filter { it.profile_path != null }
                is Result.Error -> null
            }
        }
        return castMembers
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}