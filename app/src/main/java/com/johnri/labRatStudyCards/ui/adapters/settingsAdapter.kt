package com.johnri.labRatStudyCards.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johnri.labRatStudyCards.viewmodel.settings.settingsOptions

class settingsAdapter(
    private val options: List<settingsOptions>
) : RecyclerView.Adapter<settingsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(android.R.id.text1)

        fun bind(option: settingsOptions) {
            title.text = option.title
            itemView.setOnClickListener { option.onClick() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = options.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(options[position])
    }
}