package com.sparta.imagesearch.view.save

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.GsonBuilder
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.databinding.FragmentSaveBinding
import com.sparta.imagesearch.extension.ContextExtension.toast
import com.sparta.imagesearch.extension.GsonExtension.gsonToIntegrateModel
import com.sparta.imagesearch.util.APIResponse
import com.sparta.imagesearch.view.adapter.SaveListViewAdapter
import com.sparta.imagesearch.view.main.MainViewModel
import com.sparta.imagesearch.view.main.MainViewModelFactory

class SaveFragment : Fragment() {
    private var _binding: FragmentSaveBinding? = null
    private val binding: FragmentSaveBinding
        get() = _binding!!
    private val saveAdapter by lazy {
        SaveListViewAdapter()
    }
    private val saveViewModel by lazy {
        ViewModelProvider(
            this,
            SaveViewModelFactory(requireActivity())
        )[SaveViewModel::class.java]
    }
//    private val mainViewModel: MainViewModel by activityViewModels {
//        MainViewModelFactory(
//            requireActivity()
//        )
//    }

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
        fabDeleteAll.setOnClickListener {
            removeItems()
        }
        fetchItems()
    }

    private fun fetchItems() = with(binding) {
        saveViewModel.getAllModels()
        saveViewModel.modelState.observe(viewLifecycleOwner) {
            when (it) {
                is APIResponse.Error -> {
                    saveRecyclerView.isVisible = false
                    progressbar.isVisible = false
                    noticeTextView.isVisible = true
                    noticeTextView.text = it.message
                }

                is APIResponse.Loading -> {
                    saveRecyclerView.isVisible = false
                    progressbar.isVisible = true
                    noticeTextView.isVisible = false
                }

                is APIResponse.Success -> {
                    saveRecyclerView.isVisible = true
                    progressbar.isVisible = false
                    noticeTextView.isVisible = false
                    val data = it.data
                    val formattedData = arrayListOf<IntegratedModel>()
                    for (item in data?.toList()!!) {
                        val gsonData =
                            GsonBuilder().gsonToIntegrateModel(item.toString()) ?: continue
                        formattedData.add(gsonData)
                    }
                    saveAdapter.submitList(formattedData.sortedBy { item -> item.ordering })
                }
            }
        }

    }

    private fun removeItems() = with(binding) {
        saveViewModel.saveClear()
        saveViewModel.removeState.observe(viewLifecycleOwner) {
            when (it) {
                is APIResponse.Error -> {
                    noticeTextView.isVisible = false
                    requireActivity().toast(it.message.toString())
                }

                is APIResponse.Loading -> {}
                is APIResponse.Success -> {
                    saveAdapter.submitList(emptyList())
                    saveRecyclerView.isVisible = false
                    noticeTextView.isVisible = true
                    noticeTextView.text = "보관함 목록이 없습니다."
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