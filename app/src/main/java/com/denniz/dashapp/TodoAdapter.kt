package com.denniz.dashapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoAdapter(private var items: List<Todo>, private val todoDao: TodoDao, private val todoListFrag: TodoListFrag):
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

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
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        holder.titleTextView.text = item.title

        holder.deleteButton.setOnClickListener{
            GlobalScope.launch {
                deleteItem(todoDao, item)
                todoListFrag.updateView()
                db.collection("task_items").whereEqualTo("Task", item.title)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot){
                            val taskId = document.id
                            db.collection("task_items").document(taskId).delete()
                        }
                    }
            }
        }
    }
    suspend fun deleteItem(todoDao: TodoDao, item: Todo) {
        withContext(Dispatchers.IO) {
            todoDao.delete(item)
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }
    fun updateItems(newItems: List<Todo>){
        items = newItems
        notifyDataSetChanged()
    }
}