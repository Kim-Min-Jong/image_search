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

    private fun isLikedResources(isLiked: Boolean, checkBox: CheckBox) {
        when (isLiked) {
            true -> checkBox.setBackgroundResource(R.drawable.ic_star_favorite)
            false -> checkBox.setBackgroundResource(R.drawable.ic_star)
        }
    }


    inner class SaveViewHolder(
        private val binding: ItemSearchBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: IntegratedModel) = with(binding) {
            Glide.with(root)
                .load(model.thumbnailUrl)
                .override(model.width, model.height)
                .fitCenter()
                .into(thumbnailImageView)

            titleTextView.text = model.title
            timeTextView.text = model.dateTime
            likedCheckBox.run {
                val prefs = PreferenceUtils(context).getModel(model.thumbnailUrl!!)
                isChecked = prefs != null
                isLikedResources(isChecked, this)
                setOnCheckedChangeListener { _, isChecked ->
                    isLikedResources(isChecked, this)
                    when(isChecked) {
                        true -> {
//                            PreferenceUtils(context).setModel(model.thumbnailUrl , model)
                        }
                        false -> {
//                            PreferenceUtils(context).removeModel(model.thumbnailUrl)
                        }
                    }
                }
            }
        }
    }
}