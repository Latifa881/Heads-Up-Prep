package com.example.headsupprep

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class RecyclerViewAdapter(
    val details: ArrayList<CelebrityData>
) :
    RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = details[position]

        holder.itemView.apply {
            tvName.text = data.name
            tvTaboo1.text = data.taboo1
            tvTaboo2.text = data.taboo2
            tvTaboo3.text = data.taboo3

        }
    }
    override fun getItemCount() = details.size

}
