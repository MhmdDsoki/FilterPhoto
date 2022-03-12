package com.example.filterphoto.repo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.filterphoto.data.ImageFilter
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import java.io.InputStream

class EditImageRepoImpl(private val context: Context):RepoEditImage {
    //A Uniform Resource Identifier (URI) :
    //is a unique sequence of characters that identifies
    //a logical or physical resource used by web technologies.

    //Bitmap
    //A bitmap is an array of bits that specify the color of each pixel
    //in a rectangular array of pixels

    override suspend fun prepareImageView(imageUri: Uri): Bitmap?{
            //transform bitmap into uri.
            getInputStreamFromUri(imageUri)?.let { inputStream ->
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val width = context.resources.displayMetrics.widthPixels
            val height = ((originalBitmap.height *width) / originalBitmap.width)
            return Bitmap.createScaledBitmap(originalBitmap,width,height,false)
        } ?: return null
    }



    private fun getInputStreamFromUri(uri:Uri): InputStream?{
        //content resolver
        //An extension of android.content.ContentResolver that is designed for testing.
        //This class provides applications access to the content model.
        return context.contentResolver.openInputStream(uri)
    }
}