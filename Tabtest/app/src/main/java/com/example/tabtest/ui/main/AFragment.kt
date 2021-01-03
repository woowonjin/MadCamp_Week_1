package com.example.tabtest.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.tabtest.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import android.os.SystemClock.sleep
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class AFragment : Fragment(), SearchView.OnQueryTextListener, FragmentLifecycle {
    private lateinit var contactsHelper: ContactsHelper
    private var disposable = Disposables.empty()
    private val mAdapter = CustomAdapter()
    private var SaveQuery: String? = ""
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.main_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        println("ONCREATEVIEW")

        val root = inflater.inflate(R.layout.fragment_a, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)
        val swipeRefreshLayout: SwipeRefreshLayout = root.findViewById(R.id.srl_main)


        val searchView: SearchView = root.findViewById(R.id.searchV)
        //val button: Button = root.findViewById(R.id.button)
        searchView.setOnQueryTextListener(this) //modify
        Log.d("check", "search")

//        val frame: View = requireActivity().findViewById(R.id.frame)
//        frame.setOnClickListener(object: View.OnClickListener{
//            override fun onClick(v: View) {
//                Log.d("frame","touch")
//            }
//        })

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
//            contactsHelper = ContactsHelper(requireContext().contentResolver)
//
//            val recyler_view: RecyclerView = root.findViewById(R.id.recycler_view)
//            recyler_view.adapter = mAdapter
//
//            val layout = LinearLayoutManager(requireContext())
//            recyler_view.layoutManager = layout
            //recyler_view.setHasFixedSize(true)

            if(!searchView.isIconified()){
                searchView.onActionViewCollapsed()
            }


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
//                sleep(1000)
                println(SaveQuery)
//                if(SaveQuery!=null){ this.search(SaveQuery)}
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
                println("LOAD LIST, ${it.values.toList()}")

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
        Log.d("Text", "text is " + newText!!)
        search(newText)
        SaveQuery = newText

        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("Text", "text is " + query!!)
        search(query)
        SaveQuery = query
        return false
    }

    private fun search (s: String?) {
        println("SEARCH")
        mAdapter.search(s) {
            // update UI on nothing found
            Toast.makeText(context, "Nothing Found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClick(){
        Log.d("Text", "click")
    }

    override fun onPauseFragment() {
        Log.d("tab","pauseA")
        val searchView: SearchView = requireView().findViewById(R.id.searchV)
        searchView.clearFocus()


//        onStop()
    }

    override fun onResumeFragment() {
        Log.d("tab","resumeA")

//        onStart()
    }

//    override fun onResume() {
//        println("RESUME")
//        super.onResume()
//    }
//
//    override fun onPause() {
//        println("PAUSE")
//        super.onPause()
//    }


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



