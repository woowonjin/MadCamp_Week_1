package com.example.tabtest.ui.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tabtest.R
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

class BFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_b, container, false)
//        val textView: TextView = root.findViewById(R.id.section_label)
        var button: ImageButton = root.findViewById(R.id.add_btn)
        button.setOnClickListener {
            Toast.makeText(requireContext(), "테스트", Toast.LENGTH_SHORT ).show()
        }
        return root
    }





}