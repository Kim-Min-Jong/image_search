package com.sparta.imagesearch.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.databinding.ItemSearchBinding
import com.sparta.imagesearch.extension.DateExtension.dateToString
import com.sparta.imagesearch.view.App

class SearchListViewAdapter(
    private val onStarChecked: (IntegratedModel) -> Unit
) : ListAdapter<IntegratedModel, SearchListViewAdapter.SearchViewHolder>(IntegratedModel.DIFF_CALLBACK) {
    // 보관함 저장 순서를 담는 값
    private var prefsId = App.prefs.id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onStarChecked
        )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    inner class SearchViewHolder(
        private val binding: ItemSearchBinding,
        private val onStarChecked: (IntegratedModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: IntegratedModel) = with(binding) {
            searchModel = model
            executePendingBindings()
        }
    }
}