package com.example.tabtest.ui.main


import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabtest.R

//public var photoposition = 0
//public var photoArray = ArrayList<GridItem>()

class BFragment : Fragment(), FragmentLifecycle, CellClickListner {

    private val OPEN_GALLERY = 1
    var imageList : ArrayList<GridItem> = ArrayList<GridItem>()
    private val mAdapter = GridRecyclerAdapter(this)
    var isIn = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_b, container, false)
        view.findViewById<RecyclerView>(R.id.recycler_view_grid).apply{
            this.adapter = mAdapter
            layoutManager = GridLayoutManager(context,3)
        }
        val button: ImageButton = view.findViewById(R.id.add_btn)
        button.setOnClickListener {
            openGallery()
        }
        return view
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
                    Log.d("Success", "Add Image Success")
//                    mAdapter.addItem(GridItem(mAdapter.itemCount, bitmap))
                    isIn = false
                    for(s in mAdapter.dataList){
                        if(s.data == data?.data){
                            isIn = true
                            break
                        }
                    }
                    if(!isIn) {
                        mAdapter.addItem(GridItem(mAdapter.itemCount, bitmap, data?.data)) // add photo to recyclerview
                    }
                }
                else{
                    Log.d("Error", "Something Wrong")
                }
            }
            else{
                Log.d("Error", "Something Wrong")
            }
        }
        else{
            Log.d("Error", "Something Wrong")
        }
    }


    override fun onPauseFragment() {
        Log.d("tab","pauseB")
        this.onDestroyView()
    }

    override fun onResumeFragment() {
        Log.d("tab","resumeB")
        this.onResume()
    }

    override fun onCellClickListner(currentposition: Int, photolist: ArrayList<GridItem>) {
        Toast.makeText(requireContext(),"Cell clicked", Toast.LENGTH_SHORT).show()
//        photoposition = currentposition
//        photoArray = photolist

        val mDialog = PhotoDialog()
        mDialog.show(requireFragmentManager(), "PHOTO")
        mDialog.PhotoPosition = currentposition
        mDialog.PhotoArray = photolist


//        println(photoposition)
//        val intent = Intent(requireContext(), PhotoView::class.java)
//
//        intent.putExtra("key", 3)
//        requireContext().startActivity(intent)

    }

//    fun sendcurrentpostion(): Int {
//        return photoposition
//    }



}