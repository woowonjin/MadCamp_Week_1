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
        val mAdapter= PhotoPagerAdapter(requireContext(),PhotoArray) // make adapter
        val mViewPager : ViewPager = view.findViewById(R.id.photo_view_pager) // make viewpager object
        mViewPager.adapter = mAdapter // connect viewpager adapter
        mViewPager.setCurrentItem(PhotoPosition) // set current page of view pager

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

        requireDialog().window?.setLayout(1000,1000) // Set dialog size
        super.onResume()

    }

}