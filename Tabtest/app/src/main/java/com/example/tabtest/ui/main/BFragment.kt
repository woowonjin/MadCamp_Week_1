package com.example.tabtest.ui.main


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

class BFragment : Fragment() {
    private val OPEN_GALLERY = 1
    private var imageTest : ImageView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_b, container, false)
//        val textView: TextView = root.findViewById(R.id.section_label)
        var button: ImageButton = root.findViewById(R.id.add_btn)
        imageTest  = root.findViewById(R.id.image)
        button.setOnClickListener {
            openGallery()
        }
        return root
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

                    imageTest?.setImageBitmap(bitmap)
                    Log.d("Success", "Photo Success")
                }
                else{
                    Log.d("Error", "Something Wrong")
                }
            }
        }
    }



}