package com.johnri.labRatStudyCards.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.ui.intro.introSlide
import android.widget.TextView
import android.widget.ImageView


class introAdapter(
    private val slides: List<introSlide>
) : RecyclerView.Adapter<introAdapter.SlideViewHolder>() {

    class SlideViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slide, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        val slide = slides[position]
        holder.view.findViewById<TextView>(R.id.tvTitle).text = slide.title
        val imageView = holder.view.findViewById<ImageView>(R.id.imgSlide)
        imageView.setImageResource(slide.imageRes)
    }

    override fun getItemCount() = slides.size
}