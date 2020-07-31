package com.doiliomatsinhe.mymovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doiliomatsinhe.mymovies.databinding.TrailersItemBinding
import com.doiliomatsinhe.mymovies.model.MovieTrailer

class TrailerViewHolder(private val binding: TrailersItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MovieTrailer?) {
        binding.trailer = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): TrailerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrailersItemBinding.inflate(inflater, parent, false)

            return TrailerViewHolder(binding)
        }
    }

}