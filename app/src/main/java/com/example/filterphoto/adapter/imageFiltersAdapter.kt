package com.example.filterphoto.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.filterphoto.R
import com.example.filterphoto.data.ImageFilter
import com.example.filterphoto.databinding.ItemContainerFilterBinding
import com.example.filterphoto.listener.ImageFilterListener
import com.example.filterphoto.viewmodels.EditImageViewModel

class ImageFiltersAdapter(private val imageFilter:List<ImageFilter>
                         ,private val imageFilterListener: ImageFilterListener) :
    RecyclerView.Adapter<ImageFiltersAdapter.imageFilterViewHolder>() {
    private var selectedFilterPosition = 0
    private var previouslySelectedPosition =0
    //item... generated based on our item container layout
    //for recyclerview which is item_container_filter""
    inner class imageFilterViewHolder(val binding: ItemContainerFilterBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): imageFilterViewHolder {
            val binding =ItemContainerFilterBinding.inflate(
                LayoutInflater.from(parent.context),parent,false)
            return imageFilterViewHolder(binding)
        }

    override fun onBindViewHolder(holder: imageFilterViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(imageFilter[position]){
                binding.filterName.text =name
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.root.setOnClickListener{
                    if (position!=selectedFilterPosition)
                    imageFilterListener.onFilterSelected(this)
                    previouslySelectedPosition =selectedFilterPosition
                    selectedFilterPosition =position
                    with(this@ImageFiltersAdapter)
                    {
                        notifyItemChanged(previouslySelectedPosition,Unit)
                        notifyItemChanged(selectedFilterPosition,Unit)
                    }
                }

            }
            binding.filterName.setTextColor(
                ContextCompat.getColor(binding.filterName.context,
                    if (selectedFilterPosition==position)
                        R.color.primaryDark
                else
                        R.color.primaryText
                        ))
            }

    }

    override fun getItemCount()=imageFilter.size

}