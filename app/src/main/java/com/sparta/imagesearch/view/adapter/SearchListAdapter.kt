package com.sparta.imagesearch.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.databinding.ItemSearchBinding
import com.sparta.imagesearch.preference.PreferenceUtils
import com.sparta.imagesearch.view.App
import java.util.concurrent.atomic.AtomicLong

class SearchListAdapter(
    private val onStarChecked: (IntegratedModel) -> Unit,
    private val getModels: (String) -> Unit
) : RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>() {
    private val _list = arrayListOf<IntegratedModel>()
    val list: List<IntegratedModel>
        get() = _list
    private var prefsId = App.prefs.getId()
    private var isListEmpty = false
    fun addItems(items: List<IntegratedModel>) {
        _list.clear()
        _list.addAll(items)
        notifyDataSetChanged()
    }

    fun updateItems(items: List<IntegratedModel>) {
        _list.addAll(items)
        notifyItemInserted(itemCount - 1)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onStarChecked,
            getModels
        )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(_list[position])
    }

    override fun getItemCount(): Int = _list.size

    fun notifyModel(b: Boolean) {
        isListEmpty = b
    }

    inner class SearchViewHolder(
        private val binding: ItemSearchBinding,
        private val onStarChecked: (IntegratedModel) -> Unit,
        private val getModels: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: IntegratedModel) = with(binding) {
            Glide.with(root)
                .load(model.thumbnailUrl)
                .override(model.width, if(model.height > 1000) model.height / 2 else model.height)
                .fitCenter()
                .into(thumbnailImageView)

            titleTextView.text = model.title
            timeTextView.text = model.dateTime
            likedCheckBox.run {
                setOnCheckedChangeListener { _, isChecked ->
                    if (model.isLiked != isChecked) {
                        App.prefs.setId(++prefsId)
                        onStarChecked(
                            model.copy(
                                isLiked = isChecked,
                                ordering = prefsId
                            )
                        )
                    }
                }
            }
        }
    }
}