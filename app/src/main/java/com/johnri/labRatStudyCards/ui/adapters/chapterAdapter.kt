package com.johnri.labRatStudyCards.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.entity.chapterEntity
import com.johnri.labRatStudyCards.viewmodel.chapter.chapterWithCount

class chapterAdapter(
    private var chapters: List<chapterWithCount>,
    private val onClick: (chapterEntity) -> Unit,
    private val onLongClick: (chapterEntity) -> Unit
) : RecyclerView.Adapter<chapterAdapter.ChapterViewHolder>() {

    class ChapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvChapterName)
        val tvCount: TextView = view.findViewById(R.id.tvChapterCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chapter, parent, false)
        return ChapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {

        val item = chapters[position]

        holder.tvName.text = item.chapter.name
        holder.tvCount.text = "${item.cardCount} cartas"

        holder.itemView.setOnClickListener {
            onClick(item.chapter)
        }

        holder.itemView.setOnLongClickListener {

            onLongClick(item.chapter)

            true
        }
    }

    override fun getItemCount(): Int = chapters.size

    fun updateData(newChapters: List<chapterWithCount>) {
        chapters = newChapters
        notifyDataSetChanged()
    }
}