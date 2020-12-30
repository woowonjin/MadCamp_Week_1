package com.example.tabtest.ui.main

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.tabtest.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.lifecycle.Observer
import android.Manifest


class AFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_a, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)

        var userList = arrayListOf<DataVo>(
                DataVo("박해철","tesdid","카이스트","10000","user_img_01"),
                DataVo("박해철","tesdid","카이스트","10000","user_img_01")
        )

        val mAdapter=CustomAdapter(requireContext(), userList)
        val recyler_view: RecyclerView = root.findViewById(R.id.recycler_view)
        recyler_view.adapter = mAdapter

        val layout = LinearLayoutManager(requireContext())
        recyler_view.layoutManager=layout
        recyler_view.setHasFixedSize(true)









        return root
    }

}
