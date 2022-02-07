package com.ineedyourcode.groovymovie.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.model.db.ItemHistory
import com.squareup.picasso.Picasso

class HistoryAdapter :
    RecyclerView.Adapter<HistoryAdapter.HistoryListViewHolder>() {

    private lateinit var historyList: List<ItemHistory>
    private val mainPosterPath = "https://image.tmdb.org/t/p/"
    private val posterSize = "w185/"

    fun setAdapterData(historyList: List<ItemHistory>) {
        notifyDataSetChanged()
        this.historyList = historyList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryListViewHolder, position: Int) {
        with(holder) {
            movieTitle.text = "\"${historyList[position].movieTitle}\""
            historyTime.text = "${historyList[position].time} / ${historyList[position].date}"

            Picasso.get()
                .load("${mainPosterPath}${posterSize}${historyList[position].posterPath}")
                .into(moviePoster)
        }
    }

    override fun getItemCount() = historyList.size

    class HistoryListViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        val movieTitle: TextView = itemView.findViewById(R.id.txt_history_movie_title)
        val moviePoster: ImageView = itemView.findViewById(R.id.history_poster)
        val historyTime: TextView = itemView.findViewById(R.id.history_time)
    }
}