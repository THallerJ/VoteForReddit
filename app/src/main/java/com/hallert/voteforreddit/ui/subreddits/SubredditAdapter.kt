package com.hallert.voteforreddit.ui.subreddits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import kotlinx.android.synthetic.main.subreddit_item.view.*
import net.dean.jraw.models.Subreddit

class SubredditAdapter constructor(
    private val clickListener: SubredditClickListener,
    private val showPrefix: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<Subreddit> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SubredditViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.subreddit_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SubredditViewHolder -> {
                holder.bind(data[position], showPrefix, clickListener)
            }

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class SubredditViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.subreddit_name

        fun bind(subreddit: Subreddit, showSuffix: Boolean, listener: SubredditClickListener) {
            if (showSuffix) {
                name.text = String.format(
                    RedditApp.appContext.resources.getString(R.string.subreddit_name),
                    "r/",
                    subreddit.name
                )
            } else {
                name.text = String.format(
                    RedditApp.appContext.resources.getString(R.string.subreddit_name),
                    "",
                    subreddit.name
                )
            }

            itemView.setOnClickListener {
                listener.onItemClick(subreddit, adapterPosition)
            }
        }
    }
}


interface SubredditClickListener {
    fun onItemClick(subreddit: Subreddit, position: Int)
}