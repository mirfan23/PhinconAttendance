package com.example.phinconproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phinconproject.databinding.SliderItemBinding
import com.example.phinconproject.models.SliderItemModel

class SliderAdapter(private val items: List<SliderItemModel>) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    inner class  SliderViewHolder(itemView: SliderItemBinding): RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView
        fun bind(data: SliderItemModel) {
            with(binding) {
                imageView.setImageResource(data.imageResId)
                titleText.text = data.title
                descText.text = data.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SliderItemBinding.inflate(inflater, parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = items.size
}
