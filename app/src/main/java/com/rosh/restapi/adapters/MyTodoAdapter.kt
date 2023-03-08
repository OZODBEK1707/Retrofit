package com.rosh.restapi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rosh.restapi.MainActivity
import com.rosh.restapi.databinding.ItemRvBinding
import com.rosh.restapi.models.MyTodo


class MyTodoAdapter(val rvClick: RvClick, val list: ArrayList<MyTodo> = ArrayList()): RecyclerView.Adapter<MyTodoAdapter.Vh>() {

    inner class Vh(var itemRvBinding: ItemRvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(myTodo: MyTodo, position: Int) {
            itemRvBinding.tvName.text = myTodo.sarlavha
            itemRvBinding.tvDeadline.text = myTodo.oxirgi_muddat
            itemRvBinding.tvStatus.text = myTodo.holat
            itemRvBinding.tvText.text = myTodo.matn

            itemRvBinding.root.setOnLongClickListener {
                rvClick.deleteTodo(myTodo)
                true
            }
            itemRvBinding.tvName.setOnClickListener {
                rvClick.updateTodo(myTodo)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface RvClick{
        fun deleteTodo(myTodo: MyTodo)
        fun updateTodo(myTodo: MyTodo)
    }
}
