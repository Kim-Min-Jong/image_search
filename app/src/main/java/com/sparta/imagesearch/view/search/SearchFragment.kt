package com.sparta.imagesearch.view.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.databinding.FragmentSearchBinding
import com.sparta.imagesearch.extension.ContextExtension.toast
import com.sparta.imagesearch.key.Key.API_KEY
import com.sparta.imagesearch.util.APIResponse
import com.sparta.imagesearch.util.ScrollConstant.SCROLL_BOTTOM
import com.sparta.imagesearch.util.ScrollConstant.SCROLL_DEFAULT
import com.sparta.imagesearch.view.adapter.SearchListAdapter


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!
    private var page = 1
    private var searchText = ""

    //현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
    private val searchAdapter by lazy {
        SearchListAdapter(
            onStarChecked = { model ->
                when (model.isLiked) {
                    true -> addModelFromPreference(model)
                    false -> removeModelFromPreference(model)
                }
            },
            getModels = { url ->
                getModelFromPreference(url)
            })
    }

    private val searchViewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModelFactory(requireActivity())
        )[SearchViewModel::class.java]
    }
    private val inputMethodManager by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val endScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.searchRecyclerView.canScrollVertically(1)) {
                    println(searchViewModel.isEndClip.toString() + searchViewModel.isEndImage.toString())
                    if (!searchViewModel.isEndClip!! && !searchViewModel.isEndImage!!) {
                        page++
                        fetchItems(binding.searchEditText.text.toString(), page, SCROLL_BOTTOM)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initViews() = with(binding) {
        searchRecyclerView.run {
            adapter = searchAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addOnScrollListener(endScrollListener)
        }

        searchButton.setOnClickListener {
            settingVirtualKeyboard()
            if (searchEditText.text.isEmpty()) {
                requireActivity().toast("검색어를 입력해주세요.")
                return@setOnClickListener
            }
            fetchItems(searchEditText.text.toString(), page, SCROLL_DEFAULT)
        }

        searchEditText.setOnEditorActionListener { editText, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                searchText(editText)
            }
            true
        }
    }

    private fun settingVirtualKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(
            requireActivity().currentFocus?.windowToken,
            0
        )
    }

    private fun searchText(editText: TextView) = with(binding) {
        // 가상 키보드 설정
        if (editText.text.isEmpty()) {
            requireActivity().toast("검색어를 입력해주세요.")
            return
        }
        settingVirtualKeyboard()
        progressbar.isVisible = true
        requireActivity().currentFocus?.clearFocus()

        searchText = editText.text.toString()
        fetchItems(editText.text.toString(), page, SCROLL_DEFAULT)
    }

    private fun fetchItems(query: String, page: Int, scrollFlag: Int) = with(binding) {
        searchViewModel.getDatas(AUTHORIZATION, query, page, scrollFlag)
        searchViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is APIResponse.Error -> {
                    searchRecyclerView.isVisible = false
                    progressbar.isVisible = false
                    updateProgressbar.isVisible = false
                    requireActivity().toast("오류 발생")
                    Log.e("error", it.message.toString())
                }

                is APIResponse.Loading -> {
                    if (it.data == null) {
                        progressbar.isVisible = false
                        updateProgressbar.isVisible = true
                        searchRecyclerView.isVisible = true
                    } else {
                        updateProgressbar.isVisible = false
                        progressbar.isVisible = true
                        searchRecyclerView.isVisible = false
                    }
                }

                is APIResponse.Success -> {
                    searchRecyclerView.isVisible = true
                    it.data?.let { data ->
//                        val sortedData = data.sortedByDescending { time -> time.dateTime }
                        when (scrollFlag) {
                            SCROLL_DEFAULT -> searchAdapter.addItems(data)
                            SCROLL_BOTTOM -> searchAdapter.updateItems(data)
                        }
                    }
                    progressbar.isVisible = false
                    updateProgressbar.isVisible = false
                }
            }
        }
    }

    private fun addModelFromPreference(model: IntegratedModel) {
        searchViewModel.addModelFromPreference(model)
    }

    private fun removeModelFromPreference(model: IntegratedModel) {
        searchViewModel.removeModelFromPreference(model)
    }

    private fun getModelFromPreference(url: String) {
        searchViewModel.getModelFromPreference(url)
        searchViewModel.prefsState.observe(viewLifecycleOwner) {
            when (it) {
                is APIResponse.Error -> { requireActivity().toast(it.message.toString())}
                is APIResponse.Loading -> {}
                is APIResponse.Success -> {
                    println(it.data)
//                    when (it.data ) {
//                        true -> searchAdapter.notifyModel(false)
//                        else -> searchAdapter.notifyModel(true)
//                    }
                }
            }
        }
//        var list: List<String> = emptyList()
//        CoroutineScope(Dispatchers.Main).launch {
//            list = searchViewModel.getModelFromPreference(url)
//        }
//        Handler(Looper.getMainLooper()).postDelayed({
//            println(list)
//        },100)
//        return list
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val AUTHORIZATION = "KakaoAK $API_KEY"
        const val TAG = "SEARCH_FRAGMENT"
        fun newInstance() = SearchFragment()
    }
}