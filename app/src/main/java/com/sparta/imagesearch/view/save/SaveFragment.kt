package com.sparta.imagesearch.view.save

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sparta.imagesearch.R

class SaveFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_save, container, false)
    }

    companion object {
        const val TAG = "SAVE_FRAGMENT"
        fun newInstance() = SaveFragment()

    }
}