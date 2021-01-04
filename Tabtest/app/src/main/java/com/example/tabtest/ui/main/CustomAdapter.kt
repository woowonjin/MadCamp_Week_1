package com.example.tabtest.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tabtest.R
import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.locks.ReentrantLock


class CustomAdapter(val ContactClickListner: ContactClickListner): RecyclerView.Adapter<CustomAdapter.ContactsViewHolder>(),Filterable{
    private var items: List<ContactModel> = emptyList()
    private var searchList = mutableListOf<ContactModel>()
    val mutex = Mutex()
    val lock = ReentrantLock()

    @Synchronized
    fun bindItem(items: List<ContactModel>){

        this.items = items
        this.searchList = ArrayList(items)

        for(s in items){
            Log.d("list", "element : " + s)
        }
        notifyDataSetChanged()
    }

    inner class ContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val userPhoto = itemView.findViewById<ImageView>(R.id.userimg)
        private val userName = itemView.findViewById<TextView>(R.id.userNameTxt)
        private val userPay = itemView.findViewById<TextView>(R.id.payTxt)
        private val userAddress: TextView = itemView.findViewById<TextView>(R.id.addressTxt)
        private val call = itemView.findViewById<ImageButton>(R.id.call_btn)

        override fun onClick(v: View?) {
            ContactClickListner.onContactClickListner(composePhoneNumbersText(items[adapterPosition].phoneNumbers))
//            println("TOUCH")

        }



        @Synchronized fun bindItem(contactModel: ContactModel, ContactClickListner: ContactClickListner) {
            lock.lock()
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

            call.setOnClickListener(this)

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

            lock.unlock()
        }

        private fun composePhoneNumbersText(phoneNumbers: Set<String>): String =
            phoneNumbers.joinToString(separator = "\n")


//                userPay.text = dataVo.pay.toString()
//                userAddress.text = dataVo.address
    }
    @Synchronized
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_layout, parent, false)
//        val holder = GridRecyclerAdapter.ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.grid_image, parent, false))
//        view.itemView.setOnClickListener { //set listner by CellClicklistner that from BFragment
//            ContactClickListner.onContactClickListner() // get cellClickListner object and do onCellClickListner method when Click the itemView
//
//        }
        return ContactsViewHolder(view)
    }
    @Synchronized
    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindItem(items[position], ContactClickListner)
    }
    @Synchronized
    override fun getItemCount(): Int {
        return items.size
    }


    private val originalList = ArrayList(items)
    // a method-body to invoke when search returns nothing. It can be null.
    private var onNothingFound: (() -> Unit)? = null

    /**
     * Searches a specific item in the list and updates adapter.
     * if the search returns empty then onNothingFound callback is invoked if provided which can be used to update UI
     * @param s the search query or text. It can be null.
     * @param onNothingFound a method-body to invoke when search returns nothing. It can be null.
     */
    @Synchronized
    fun search(s: String?, onNothingFound: (() -> Unit)?) {
        this.onNothingFound = onNothingFound
        filter.filter(s)

    }


    @Synchronized
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    println("CHECK EMPTY")
                    items = searchList
                } else {
                    val filteredList = ArrayList<ContactModel>()
                    //이부분에서 원하는 데이터를 검색할 수 있음
                    for (row in searchList) {
                        if (  !row.fullName.isNullOrEmpty() && row.fullName.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    items = filteredList
                    println("$items")
                }
                val filterResults = FilterResults()
                filterResults.values = items
                return filterResults
            }
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                items = filterResults.values as ArrayList<ContactModel>
                println("SEARCH SUCCESS")
                println("$items")
                notifyDataSetChanged()
            }
        }
    }



//    override fun getFilter(): Filter {
//        return object : Filter() {
//            private val filterResults = FilterResults()
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                items.clear()
//                if (constraint.isNullOrBlank()) {
//                    items.addAll(originalList)
//                } else {
//                    val searchResults = originalList.filter { it.getSearchCriteria().contains(constraint) }
//                    items.addAll(searchResults)
//                }
//                return filterResults.also {
//                    it.values = items
//                }
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                // no need to use "results" filtered list provided by this method.
//                if (items.isNullOrEmpty())
//                    onNothingFound?.invoke()
//                notifyDataSetChanged()
//
//            }
//        }
//    }
//
//    interface Searchable {
//        /** This method will allow to specify a search string to compare against
//        your search this can be anything depending on your use case.
//         */
//        fun getSearchCriteria(): String
//    }

//    fun getFilter(): Filter? {
//        return exampleFilter
//    }
//
//    private val exampleFilter: Filter = object : Filter() {
//        override fun performFiltering(constraint: CharSequence?): FilterResults? {
//            val filteredList: MutableList<ContactModel> = ArrayList()
//            if (constraint == null || constraint.length == 0) {
//                filteredList.addAll(searchList)
//            } else {
//                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
//                for (item in searchList) {
//                    if ( !item.fullName.isNullOrEmpty() && item.fullName.toLowerCase().contains(filterPattern)) {
//                        filteredList.add(item)
//                    }
//                }
//            }
//            val results = FilterResults()
//            results.values = filteredList
//            return results
//        }
//
//        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
//            searchList.clear()
//            searchList.addAll(results.values as List<ContactModel>)
//            notifyDataSetChanged()
//        }
//    }


}