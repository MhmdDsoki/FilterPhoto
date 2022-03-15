package com.example.filterphoto.repo

import android.graphics.Bitmap
import android.net.Uri
import com.example.filterphoto.data.ImageFilter

interface RepoEditImage {
    suspend fun prepareImageView(imageUri: Uri):Bitmap?
    suspend fun getImageFilters(image:Bitmap):List<ImageFilter>
    suspend fun saveFilteredImage(filteredBitmap:Bitmap):Uri?
}