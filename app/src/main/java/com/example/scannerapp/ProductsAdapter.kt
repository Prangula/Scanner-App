package com.example.scannerapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scannerapp.databinding.ItemBinding

class ProductsAdapter(private val items:ArrayList<ScannedProducts>)
    :RecyclerView.Adapter<ProductsAdapter.ViewHolder>(){

        inner class ViewHolder(binding:ItemBinding)
            :RecyclerView.ViewHolder(binding.root){

            val title = binding.itemTitle
            val brand = binding.itemBrand
            val image = binding.itemImage

            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = items[position]


        holder.title.text = item.title
        holder.brand.text = item.brand
        Glide
            .with(holder.itemView.context)
            .load(item.image[0])
            .into(holder.image)


    }

}