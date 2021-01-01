package com.example.tabtest.ui.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tabtest.R

class GridRecyclerAdapter : RecyclerView.Adapter<GridRecyclerAdapter.ItemViewHolder>() {

    var dataList = ArrayList<GridItem>()

    var mPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridRecyclerAdapter.ItemViewHolder {
        Log.d("position", "onCreateViewHolder")
        val holder = ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.grid_image, parent, false))
        holder.itemView.setOnClickListener {
            setPosition(holder.adapterPosition)
            Toast.makeText(parent.context, "${holder.adapterPosition} 아이템 클릭!", Toast.LENGTH_SHORT).show()

        }
        return holder
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount", "size : " + dataList.size)
        return dataList.size
    }

    override fun onBindViewHolder(holder: GridRecyclerAdapter.ItemViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun getPosition():Int{
        return mPosition
    }

    private fun setPosition(position: Int){
        mPosition = position
    }

    fun addItem(gridItem: GridItem){
        dataList.add(gridItem)
        //갱신처리 해야함
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        if(position > 0){
            dataList.removeAt(position)
            //갱신처리해야함
            notifyDataSetChanged()
        }
    }

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val image = itemView.findViewById<ImageView>(R.id.grid_image)

        fun bind(gridItem: GridItem){
            image.setImageBitmap(gridItem.photo)
//                notifyDataSetChanged()
            Log.d("Success", "Success to find Image view")
        }
    }

}