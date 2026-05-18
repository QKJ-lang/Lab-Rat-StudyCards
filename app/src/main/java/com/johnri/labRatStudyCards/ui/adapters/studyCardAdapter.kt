package com.johnri.labRatStudyCards.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johnri.labRatStudyCards.R
import com.johnri.labRatStudyCards.data.entity.studyCardEntity

class studyCardAdapter(
    private var cards: List<studyCardEntity>
) : RecyclerView.Adapter<studyCardAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val question: TextView = itemView.findViewById(R.id.txtQuestion)
        val answer: TextView = itemView.findViewById(R.id.txtAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_study_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]

        holder.question.text = card.question
        holder.answer.text = card.answer

        // ocultar respuesta por defecto (modo estudio tipo Anki básico)
        holder.answer.visibility = View.GONE

        holder.itemView.setOnClickListener {
            holder.answer.visibility =
                if (holder.answer.visibility == View.GONE) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount(): Int = cards.size

    fun updateData(newCards: List<studyCardEntity>) {
        cards = newCards
        notifyDataSetChanged()
    }
}