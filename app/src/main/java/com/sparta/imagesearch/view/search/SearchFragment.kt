package com.sparta.imagesearch.view.search

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sparta.imagesearch.databinding.FragmentSearchBinding
import com.sparta.imagesearch.extension.ContextExtension.toast
import com.sparta.imagesearch.key.Key.API_KEY
import com.sparta.imagesearch.util.APIResponse
import com.sparta.imagesearch.view.adapter.SearchListAdapter


@RequiresApi(Build.VERSION_CODES.O)
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    private var page = 1
    private var searchText = ""

    //현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
    private var isEnd = true
    private val searchAdapter by lazy {
        SearchListAdapter()
    }

    //    private val searchViewModel: SearchViewModel by viewModels{ SearchViewModelFactory() }
    private val searchViewModel by lazy {
        ViewModelProvider(this, SearchViewModelFactory())[SearchViewModel::class.java]
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
        }

        searchEditText.setOnEditorActionListener { editText, actionId, keyEvent ->
            searchText(editText)
            true
        }
    }


    private fun searchText(editText: TextView) = with(binding) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            requireActivity().currentFocus?.windowToken,
            0
        )
        progressbar.isVisible = true
        requireActivity().currentFocus?.clearFocus()

        searchText = editText.text.toString()
        fetchItems(editText.text.toString(), page)
    }

    private fun fetchItems(query: String, page: Int) = with(binding) {
        searchViewModel.getDatas(AUTHORIZATION, query, page)
        searchViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is APIResponse.Error -> {
                    searchRecyclerView.isVisible = false
                    progressbar.isVisible = false
                    requireActivity().toast("오류 발생")
                    Log.e("error", it.message.toString())
                }

                is APIResponse.Loading -> {
                    progressbar.isVisible = true
                    searchRecyclerView.isVisible = false
                }

                is APIResponse.Success -> {
                    searchRecyclerView.isVisible = true
                    it.data?.let { data ->
                        searchAdapter.addItems(data.sortedByDescending { time -> time.dateTime })
                    }
                    progressbar.isVisible = false
                }
            }
        }
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