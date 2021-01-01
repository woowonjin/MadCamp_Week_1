package com.example.tabtest.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tabtest.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.appcompat.widget.SearchView


class AFragment : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var contactsHelper: ContactsHelper
    private var disposable = Disposables.empty()
    private val mAdapter = CustomAdapter()


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_a, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)
        val swipeRefreshLayout: SwipeRefreshLayout = root.findViewById(R.id.srl_main)


        val searchView: SearchView = root.findViewById(R.id.searchV)
        //val button: Button = root.findViewById(R.id.button)
        searchView.setOnQueryTextListener(this)
        Log.d("check", "search")

//        button.setOnClickListener{
//            onClick()
//        }



//        var userList = arrayListOf<DataVo>(
//                DataVo("박해철","tesdid","카이스트","10000","user_img_01"),
//                DataVo("박해철","tesdid","카이스트","10000","user_img_01")
//        )

        contactsHelper = ContactsHelper(requireContext().contentResolver)

        val recyler_view: RecyclerView = root.findViewById(R.id.recycler_view)
        recyler_view.adapter = mAdapter

        val layout = LinearLayoutManager(requireContext())
        recyler_view.layoutManager = layout
        //recyler_view.setHasFixedSize(true)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSION_READ_CONTACTS
            )
        } else {
            loadContacts()
        }


        swipeRefreshLayout.setOnRefreshListener {
            contactsHelper = ContactsHelper(requireContext().contentResolver)

            val recyler_view: RecyclerView = root.findViewById(R.id.recycler_view)
            recyler_view.adapter = mAdapter

            val layout = LinearLayoutManager(requireContext())
            recyler_view.layoutManager = layout
            //recyler_view.setHasFixedSize(true)

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    PERMISSION_READ_CONTACTS
                )
            } else {
                loadContacts()
            }
            swipeRefreshLayout.isRefreshing = false
        }
        Log.d("check", "here")
//        swipeRefreshLayout.isRefreshing = false
        return root
    }


    private fun loadContacts() {
        disposable.dispose()
        disposable = contactsHelper.getAllContacts().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mAdapter.bindItem(it.values.toList()) //as MutableList<ContactModel>)
            }, { Log.e("ContactHelper", it.message, it) })
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_READ_CONTACTS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    loadContacts()
                }
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    companion object {
        private const val PERMISSION_READ_CONTACTS = 1
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //Log.d("Text", "text is " + newText!!)
        search(newText)

        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("Text", "text is " + query!!)
        search(query)
        return false
    }

    private fun search(s: String?) {
        mAdapter.search(s) {
            // update UI on nothing found
            Toast.makeText(context, "Nothing Found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClick(){
        Log.d("Text", "click")
    }



//    override fun onBackPressed() {
//        // close search view on back button pressed
//        if (!searchView!!.isIconified) {
//            searchView!!.isIconified = true
//            return
//        }
//        super.onBackPressed()
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        setHasOptionsMenu(true)
//        super.onCreate(savedInstanceState)
//
//    }

//    val searchView: SearchView = searchItem.getActionView() as SearchView
//    searchView.setImeOptions(EditorInfo.IME_ACTION_DONE)
//    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//        override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                mAdapter.getFilter()?.filter(newText)
//                return false
//            }
//        })


}



