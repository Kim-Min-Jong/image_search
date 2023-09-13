package com.sparta.imagesearch.view.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.databinding.ItemSearchBinding
import com.sparta.imagesearch.extension.LocalDateExtension.dateToString
import com.sparta.imagesearch.preference.PreferenceUtils

@RequiresApi(Build.VERSION_CODES.O)
class SearchListAdapter : RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>() {
    private val _list = arrayListOf<IntegratedModel>()
    val list: List<IntegratedModel>
        get() = _list

    fun addItems(items: List<IntegratedModel>) {
        _list.clear()
        _list.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(_list[position])
    }

    override fun getItemCount(): Int = _list.size
    private fun isLikedResources(isLiked: Boolean, checkBox: CheckBox) {
        when (isLiked) {
            true -> checkBox.setBackgroundResource(R.drawable.ic_star_favorite)
            false -> checkBox.setBackgroundResource(R.drawable.ic_star)
        }
    }
    inner class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: IntegratedModel) = with(binding) {
            Glide.with(root)
                .load(model.thumbnailUrl)
                .override(model.width, model.height)
                .fitCenter()
                .into(thumbnailImageView)

            titleTextView.text = model.title
            timeTextView.text = model.dateTime.dateToString()
            likedCheckBox.run {
                isLikedResources(model.isLiked, this)
                setOnCheckedChangeListener { _, isChecked ->
                    model.isLiked = isChecked
                    isLikedResources(isChecked, this)
                    when(isChecked) {
                        true -> {
                            PreferenceUtils(context).setModel(model.thumbnailUrl!! , model)
                        }
                        false -> {

                        }
                    }
                }
            }

        }
    }
}