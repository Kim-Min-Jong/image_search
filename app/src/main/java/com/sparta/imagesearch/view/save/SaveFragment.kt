package com.sparta.imagesearch.view.save

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.GsonBuilder
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.databinding.FragmentSaveBinding
import com.sparta.imagesearch.extension.GsonExtension.gsonToIntegrateModel
import com.sparta.imagesearch.util.APIResponse
import com.sparta.imagesearch.view.adapter.SaveListAdapter

class SaveFragment : Fragment() {
    private var _binding: FragmentSaveBinding? = null
    private val binding: FragmentSaveBinding
        get() = _binding!!
    private val saveAdapter by lazy {
        SaveListAdapter()
    }
    private val saveViewModel by lazy {
        ViewModelProvider(
            this,
            SaveViewModelFactory(requireActivity())
        )[SaveViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        saveRecyclerView.run {
            adapter = saveAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
        fetchItems()
    }

    private fun fetchItems() = with(binding) {
        saveViewModel.getAllModels()
        saveViewModel.modelState.observe(viewLifecycleOwner) {
            when(it) {
                is APIResponse.Error -> {
                    progressbar.isVisible = false
                    noticeTextView.isVisible = true
                    noticeTextView.text = it.message
                }
                is APIResponse.Loading -> {
                    progressbar.isVisible = true
                    noticeTextView.isVisible = false
                }
                is APIResponse.Success -> {
                    progressbar.isVisible = false
                    noticeTextView.isVisible = false
                    val data = it.data
                    val formattedData = arrayListOf<IntegratedModel>()
                    for(item in data?.toList()!!) {
                        val gsonData =
                            GsonBuilder().gsonToIntegrateModel(item.toString()) ?: continue
                        formattedData.add(gsonData)
                    }
                    saveAdapter.addItems(formattedData.reversed())
                }
            }
        }

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