package com.sparta.imagesearch.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.databinding.ItemSearchBinding
import com.sparta.imagesearch.preference.PreferenceUtils

class SaveListAdapter : RecyclerView.Adapter<SaveListAdapter.SaveViewHolder>() {
    private val _list = arrayListOf<IntegratedModel>()
    val list: List<IntegratedModel>
        get() = _list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveViewHolder =
        SaveViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SaveViewHolder, position: Int) {
        holder.bind(_list[position])
    }

    override fun getItemCount(): Int = _list.size

    fun addItems(items: List<IntegratedModel>?) {
        if(items == null) {
            return
        }
        _list.clear()
        _list.addAll(items)
        notifyDataSetChanged()
    }
    fun clearItems() {
        _list.clear()
        notifyDataSetChanged()
    }

    inner class SaveViewHolder(
        private val binding: ItemSearchBinding,
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
                val prefs = PreferenceUtils(context).getModel(model.thumbnailUrl!!)
                isChecked = prefs != null
            }
        }
    }
}