package com.example.filterphoto.listener

import com.example.filterphoto.data.ImageFilter

interface ImageFilterListener {
    fun onFilterSelected(imageFilter: ImageFilter)
}