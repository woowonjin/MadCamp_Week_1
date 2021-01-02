package com.example.tabtest.ui.main

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.example.tabtest.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoDialog.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoDialog : DialogFragment() {


    var PhotoPosition: Int = 0
    var PhotoArray = ArrayList<GridItem>()


    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): PhotoDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = PhotoDialog()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

//        val root = inflater.inflate(R.layout.fragment_photo_dialog,container, false)
//        val mAdapter= PhotoPagerAdapter(requireContext(),PhotoArray)
//        val mViewPager : ViewPager = root.findViewById(R.id.photo_view_pager)
//        mViewPager.adapter = mAdapter

        println(PhotoPosition)
        println(PhotoArray)

        return inflater.inflate(R.layout.fragment_photo_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        val mAdapter= PhotoPagerAdapter(requireContext(),PhotoArray)
        val mViewPager : ViewPager = view.findViewById(R.id.photo_view_pager)
        mViewPager.adapter = mAdapter
        mViewPager.setCurrentItem(PhotoPosition)



    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {
//        view.tvTitle.text = arguments?.getString(KEY_TITLE)
//        view.tvSubTitle.text = arguments?.getString(KEY_SUBTITLE)


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val root = inflater.inflate(R.layout.fragment_photo_dialog,container, false)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onResume() {

        requireDialog().window?.setLayout(1000,1000)
        super.onResume()

    }


//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onStart() {
//        super.onStart()
//        dialog?.window?.setLayout(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.WRAP_CONTENT
//        )
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//
//        return inflater.inflate(R.layout.fragment_photo_dialog, container, false)
//    }
//
////    companion object {
////        /**
////         * Use this factory method to create a new instance of
////         * this fragment using the provided parameters.
////         *
////         * @param param1 Parameter 1.
////         * @param param2 Parameter 2.
////         * @return A new instance of fragment PhotoDialog.
////         */
////        // TODO: Rename and change types and number of parameters
////        @JvmStatic
////        fun newInstance(param1: String, param2: String) =
////                PhotoDialog().apply {
////                    arguments = Bundle().apply {
////                        putString(ARG_PARAM1, param1)
////                        putString(ARG_PARAM2, param2)
////                    }
////                }
////    }
}