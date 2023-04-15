package com.denniz.dashapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoAdapter(private var items: List<TodoItem>):
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.taskName)
        val deleteButton: ImageButton = itemView.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_todo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        lateinit var todoDao: TodoDao


        holder.titleTextView.text = item.title

        holder.deleteButton.setOnClickListener{
            GlobalScope.launch {
                deleteItem(todoDao, item)
            }
        }
    }
    suspend fun deleteItem(todoDao: TodoDao, item: TodoItem) {
        withContext(Dispatchers.IO) {
            todoDao.delete(item)
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }
    fun updateItems(newItems: List<TodoItem>){
        items = newItems
        notifyDataSetChanged()
    }
}