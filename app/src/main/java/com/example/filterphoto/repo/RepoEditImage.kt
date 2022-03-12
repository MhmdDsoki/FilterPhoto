package com.example.filterphoto.repo

import android.graphics.Bitmap
import android.net.Uri
import com.example.filterphoto.data.ImageFilter

interface RepoEditImage {
    suspend fun prepareImageView(imageUri: Uri):Bitmap?
}