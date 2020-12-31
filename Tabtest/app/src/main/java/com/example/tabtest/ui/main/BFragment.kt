package com.example.tabtest.ui.main


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.tabtest.R
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

class BFragment : Fragment() {
    private val OPEN_GALLERY = 1
    var imageList : ArrayList<GridItem> = ArrayList<GridItem>()
    var mInflater : LayoutInflater? = null
    var mContainer : ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_b, container, false)
//        val swipeRefreshLayout: SwipeRefreshLayout = root.findViewById(R.id.srl_main)
        mInflater = inflater
        mContainer = container
        //testCode
        val mAdapter = GridRecyclerAdapter(requireContext(), imageList)
        if(mAdapter == null){
            Log.d("Error", "mAdapter is null")
        }
        else{
            Log.d("Success", "mAdapter is ok")
        }
        val recylerView: RecyclerView = root.findViewById(R.id.recycler_view_grid)
        recylerView.adapter = mAdapter
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        gridLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recylerView.layoutManager = gridLayoutManager
        Log.d("Create", "OnCreate")
        var button: ImageButton = root.findViewById(R.id.add_btn)
        button.setOnClickListener {
            openGallery()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        val root = mInflater?.inflate(R.layout.fragment_b, mContainer, false)
//        val swipeRefreshLayout: SwipeRefreshLayout = root.findViewById(R.id.srl_main)
        if(root != null) {
            val mAdapter = GridRecyclerAdapter(requireContext(), imageList)
            val recylerView: RecyclerView = root.findViewById(R.id.recycler_view_grid)
            if(recylerView == null){
                Log.d("Error", "recyclerView is null")
            }
            recylerView.adapter = mAdapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 3)
            gridLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            recylerView.layoutManager = gridLayoutManager
            for(s in imageList){
                Log.d("img", "image is " + s)
            }
            Log.d("Resume", "Refresh")
        }
        else{
            Log.d("Error", "Root is null !!")
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == OPEN_GALLERY){
                var image : Uri? = data?.data
                var cr = activity?.contentResolver
                if(cr != null && image != null) {
                    val bitmap = when {
                        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver, image
                        )
                        else -> {
                            val source = ImageDecoder.createSource(cr, image)
                            ImageDecoder.decodeBitmap(source)

                        }
                    }
                    imageList.add(GridItem(imageList.size, bitmap))
                    Log.d("Success", "Photo Success")
                }
                else{
                    Log.d("Error", "Something Wrong")
                }
            }
        }
    }



}