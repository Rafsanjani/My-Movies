package com.doiliomatsinhe.mymovies.adapter.cast

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doiliomatsinhe.mymovies.adapter.movie.MovieViewHolder
import com.doiliomatsinhe.mymovies.model.Movie
import com.doiliomatsinhe.mymovies.model.MovieCast
import com.doiliomatsinhe.mymovies.model.TvCast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val MOVIE_VIEW_TYPE = 0
private const val SERIES_VIEW_TYPE = 1

class CastAdapter : ListAdapter<DataItem, RecyclerView.ViewHolder>(CastDiffUtillCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MOVIE_VIEW_TYPE -> MovieCastViewHolder.from(parent)
            SERIES_VIEW_TYPE -> SeriesCastViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieCastViewHolder -> {
                val movieCast = getItem(position) as DataItem.MovieCastItem
                holder.bind(movieCast.movieCast)
            }
            is SeriesCastViewHolder -> {
                val seriesCast = getItem(position) as DataItem.SeriesCastItem
                holder.bind(seriesCast.seriesCast)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.MovieCastItem -> MOVIE_VIEW_TYPE
            is DataItem.SeriesCastItem -> SERIES_VIEW_TYPE
        }
    }

    fun submitMovieCastList(list: List<MovieCast>) {
        adapterScope.launch {

            val castMembers = list.map { DataItem.MovieCastItem(it) }

            withContext(Dispatchers.Main) {
                submitList(castMembers)
            }
        }
    }

    fun submitSeriesCastList(list: List<TvCast>) {
        adapterScope.launch {

            val castMembers = list.map { DataItem.SeriesCastItem(it) }

            withContext(Dispatchers.Main) {
                submitList(castMembers)
            }
        }
    }

}

class CastDiffUtillCallback : DiffUtil.ItemCallback<DataItem>() {

    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    data class MovieCastItem(val movieCast: MovieCast) : DataItem() {
        override val id: Int
            get() = movieCast.id
    }

    data class SeriesCastItem(val seriesCast: TvCast) : DataItem() {
        override val id: Int
            get() = seriesCast.id
    }

    abstract val id: Int
}