package com.sparta.imagesearch.view.save

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sparta.imagesearch.databinding.FragmentSaveBinding

class SaveFragment : Fragment() {
    private var _binding: FragmentSaveBinding? = null
    private val binding: FragmentSaveBinding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "SAVE_FRAGMENT"
        fun newInstance() = SaveFragment()

    }
}