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
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ContactsViewHolder>(){
    private var items: List<ContactModel> = emptyList()

    fun bindItem(items: List<ContactModel>){
        this.items = items
        notifyDataSetChanged()
    }

        inner class ContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private val userPhoto = itemView.findViewById<ImageView>(R.id.userimg)
            private val userName = itemView.findViewById<TextView>(R.id.userNameTxt)
            private val userPay = itemView.findViewById<TextView>(R.id.payTxt)
            private val userAddress: TextView = itemView.findViewById<TextView>(R.id.addressTxt)

            fun bindItem(contactModel: ContactModel) {
//                if(contactModel.photoUri!= ""){
//                val resourceId = context.resources.getIdentifier(contactModel.photoUri, "drawble", context.packageName)
//
//                    if (resourceId > 0) {
//                        userPhoto.setImageResource(resourceId)
//                    } else {
//                        userPhoto.setImageResource(R.mipmap.ic_launcher_round)
//                    }
//                } else {
//                        userPhoto.setImageResource(R.mipmap.ic_launcher_round)
//                }

                userName.text = contactModel.fullName

                userPay.visibility =
                    if (contactModel.phoneNumbers.isEmpty()) View.GONE else View.VISIBLE
                userPay.text = composePhoneNumbersText(contactModel.phoneNumbers)

                //val resourceId = context.resources.getIdentifier(contactModel.photoUri)

                 if (contactModel.photoUri.isNullOrEmpty()) {
                        userPhoto.setImageResource(R.mipmap.ic_launcher_round)
                    } else {
                     userPhoto.visibility = View.VISIBLE
                     userPhoto.setImageURI(Uri.parse(contactModel.photoUri))
                 }


            }

            private fun composePhoneNumbersText(phoneNumbers: Set<String>): String =
                phoneNumbers.joinToString(separator = "\n")


//                userPay.text = dataVo.pay.toString()
//                userAddress.text = dataVo.address
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_layout, parent, false)
        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}