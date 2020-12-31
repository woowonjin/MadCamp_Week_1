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

class GridRecyclerAdapter(private val context: Context, private val dataList: ArrayList<GridItem>) : RecyclerView.Adapter<GridRecyclerAdapter.ItemViewHolder>() {

    var mPosition = 0

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

        fun bind(gridItem: GridItem, context: Context){
            if(gridItem.photo != null){
                image.setImageBitmap(gridItem.photo)
                Log.d("Success", "Success to find Image view")
            }
            else{
                Log.d("Error", "Cannot find Image view")
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridRecyclerAdapter.ItemViewHolder {
        Log.d("position", "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_image, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: GridRecyclerAdapter.ItemViewHolder, position: Int) {
        holder.bind(dataList[position], context)

        holder.itemView.setOnClickListener { view ->
            setPosition(position)
            Toast.makeText(view.context, "$position 아이템 클릭!", Toast.LENGTH_SHORT).show()

        }
    }


}