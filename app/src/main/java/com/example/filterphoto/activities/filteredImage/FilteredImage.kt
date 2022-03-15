package com.example.filterphoto.activities.filteredImage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.filterphoto.R
import com.example.filterphoto.activities.edit.EditImgActivity
import com.example.filterphoto.databinding.ActivityFilteredImageBinding
import com.example.filterphoto.databinding.EditImgBinding
import kotlinx.android.synthetic.main.activity_filtered_image.*
import kotlinx.android.synthetic.main.edit_img.*
import java.net.URI

class FilteredImage : AppCompatActivity() {
    private lateinit var fileUri: Uri
    private lateinit var binding:ActivityFilteredImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dispalyFilteredImage()
        setListeners()
    }

    private fun setListeners() {
        fabShare.setOnClickListener {
            with(Intent(Intent.ACTION_SEND)){
                putExtra(Intent.EXTRA_STREAM,fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }
    }

    private fun dispalyFilteredImage() {
        intent.getParcelableExtra<Uri>(EditImgActivity.KEY_FILTERED_IMAGE_URI)?.let { imageUri->
            fileUri =imageUri
            imageFilterImage.setImageURI(imageUri)
        }
    }
}