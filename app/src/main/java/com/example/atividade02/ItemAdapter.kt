package com.example.atividade02

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.atividade02.entities.Discipline

class ItemAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private val listOfDisciplines: MutableList<Discipline> = ArrayList()

  fun addItem(item: Discipline) {
    listOfDisciplines.add(item)
    notifyItemInserted(listOfDisciplines.size-1)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val card = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.message_card, parent, false)
    return MessageViewHolder(card)
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val currentItem = listOfDisciplines[position]
    if (holder is MessageViewHolder) {
      holder.messageCodeTextView.text = "Código: ${currentItem.code.toString()}"
      holder.messageDisciplineTextView.text = "Disciplina: ${currentItem.name}"
      holder.messageProfesorTextView.text = "Professor: ${currentItem.profesor}"
      holder.messageWorkloadTextView.text = "Carga Horária: ${currentItem.workload.toString()}"
    }
  }

  override fun getItemCount(): Int {
    return listOfDisciplines.size
  }

  fun getItem(code: Int): Discipline {
    return listOfDisciplines[code]
  }

  fun setItem(discipline: Discipline) {
    listOfDisciplines[discipline.code] = discipline
    notifyItemChanged(discipline.code)
  }

  class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val messageCodeTextView: TextView = itemView.findViewById(R.id.messageCode)
    val messageDisciplineTextView: TextView = itemView.findViewById(R.id.messageDiscipline)
    val messageProfesorTextView: TextView = itemView.findViewById(R.id.messageProfesor)
    val messageWorkloadTextView: TextView = itemView.findViewById(R.id.messageWorkload)
  }
}