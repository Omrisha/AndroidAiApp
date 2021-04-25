package com.shapiraomri.aiapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shapiraomri.aiapplication.R
import com.shapiraomri.aiapplication.models.Word

class DetectWordAdapter(private val words: List<Word>) : RecyclerView.Adapter<DetectWordAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val text = itemView.findViewById<TextView>(R.id.detect_word_text)
        val probability = itemView.findViewById<TextView>(R.id.detect_word_probability)

        fun bind(word: Word){
            text.text = word.text
            probability.text = "${word.probability} %"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detect_word_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }
}