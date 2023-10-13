package com.sparta.imagesearch.view.save

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.databinding.FragmentSaveBinding
import com.sparta.imagesearch.extension.ContextExtension.toast
import com.sparta.imagesearch.util.APIResponse
import com.sparta.imagesearch.view.adapter.SaveListViewAdapter

class SaveFragment : Fragment() {
    private var _binding: FragmentSaveBinding? = null
    private val binding: FragmentSaveBinding
        get() = _binding!!
    private val saveAdapter by lazy {
        SaveListViewAdapter { model ->
            runDeleteDialog(model)
        }
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

    // 리사이클러뷰 및 fab 초기화
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

    // viewModel에서 sharedPrefence값을 가져오고 값을 observing
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
                    saveAdapter.submitList(data?.sortedBy { item -> item.ordering })
                }
            }
        }

    }

    // 보관함 전체삭제
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

    // 개별 항목 삭제 다이얼로그
    private fun runDeleteDialog(model: IntegratedModel) {
        AlertDialog.Builder(requireActivity())
            .setMessage("삭제하시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                saveViewModel.removeModel(model)
            }.setNegativeButton("아니오") { _, _ -> }
            .setCancelable(false)
            .create()
            .show()
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
