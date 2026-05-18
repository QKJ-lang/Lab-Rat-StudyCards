package com.johnri.labRatStudyCards.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.entity.deckEntity

class deckAdapter(
    private var decks: List<deckEntity>,
    private val onClick: (deckEntity) -> Unit,
    private val onDeleteClick: (deckEntity) -> Unit
) : RecyclerView.Adapter<deckAdapter.DeckViewHolder>() {

    class DeckViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deck, parent, false)
        return DeckViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val deck = decks[position]

        val name = holder.view.findViewById<TextView>(R.id.tvDeckName)
        name.text = deck.name

        holder.view.setOnClickListener {
            onClick(deck)
        }
        holder.itemView.setOnLongClickListener {
            onDeleteClick(deck)
            true
        }
    }

    override fun getItemCount() = decks.size

    fun updateData(newDecks: List<deckEntity>) {
        decks = newDecks
        notifyDataSetChanged()
    }
}