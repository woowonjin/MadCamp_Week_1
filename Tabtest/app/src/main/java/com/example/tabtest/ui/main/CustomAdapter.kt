package com.example.tabtest.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tabtest.R
import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter (private val context: Context, private val dataList: ArrayList<DataVo>):
        RecyclerView.Adapter<CustomAdapter.ItemViewHolder>(){
        inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private val userPhoto = itemView.findViewById<ImageView>(R.id.userimg)
            private val userName = itemView.findViewById<TextView>(R.id.userNameTxt)
            private val userPay = itemView.findViewById<TextView>(R.id.payTxt)
            private val userAddress: TextView = itemView.findViewById<TextView>(R.id.addressTxt)

            fun bind(dataVo: DataVo, context: Context) {
                if(dataVo.photo != ""){
                val resourceId = context.resources.getIdentifier(dataVo.photo, "drawble", context.packageName)

                if (resourceId > 0) {
                    userPhoto.setImageResource(resourceId)
                } else {
                    userPhoto.setImageResource(R.mipmap.ic_launcher_round)
                }
            } else {
                    userPhoto.setImageResource(R.mipmap.ic_launcher_round)
                }

                userName.text = dataVo.name
                userPay.text = dataVo.pay.toString()
                userAddress.text = dataVo.address
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], context)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
        }