package com.sparta.imagesearch.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.data.repository.Repository
import com.sparta.imagesearch.databinding.FragmentSearchBinding
import com.sparta.imagesearch.extension.ContextExtension.toast
import com.sparta.imagesearch.extension.StringExtension.dateToString
import com.sparta.imagesearch.key.Key.API_KEY
import com.sparta.imagesearch.view.adapter.SearchListAdapter
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!


    private val scope = MainScope()
    private var page = 1
    private var searchText = ""

    //현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
    private var isEnd = true
    private val searchAdapter by lazy {
        SearchListAdapter()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initViews() = with(binding) {
        searchRecyclerView.run {
            adapter = searchAdapter
//            layoutManager = GridLayoutManager(requireActivity(), 2)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//                .apply {
////                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
////            }

        }
        searchEditText.setOnEditorActionListener { editText, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
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
            true
        }
    }

    private fun fetchItems(query: String, page: Int) = with(binding) {
        scope.launch {
            try {
                val list = arrayListOf<IntegratedModel>()
                val images = Repository.getImages(AUTHORIZATION, query, page)
                val clips = Repository.getClips(AUTHORIZATION, query, page)
                images?.documents?.forEach {
                    list.add(IntegratedModel(it.thumbnailUrl,"[Image]" + it.displaySitename, it.datetime.dateToString(), it.height, it.width))
                }
                clips?.documents?.forEach {
                    list.add(IntegratedModel(it?.thumbnail, "[Clip]" +it?.title, it?.datetime))
                }

                searchAdapter.addItems(list)
                progressbar.isVisible = false
            } catch (e: Exception) {
                requireActivity().toast("오류발생")
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