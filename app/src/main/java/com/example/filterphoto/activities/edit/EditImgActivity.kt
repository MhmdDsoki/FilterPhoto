package com.example.filterphoto.activities.edit

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.filterphoto.R
import com.example.filterphoto.activities.main.MainActivity
import com.example.filterphoto.utilities.displayToast
import com.example.filterphoto.utilities.show
import com.example.filterphoto.viewmodels.EditImageViewModel
import kotlinx.android.synthetic.main.edit_img.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImgActivity : AppCompatActivity() {
    private val viewModel:EditImageViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_img)
        setListeners()
        //displayImagePreview()
        displayImageObserver()
        prepareImagePreview()
    }

    private fun displayImageObserver(){
        viewModel.imagePreviewUiState.observe(this,{
            val dataState  =it?:return@observe
            previewProgress.visibility =
            if (dataState.isLoading == true)View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                imagePreview.setImageBitmap(bitmap)
                imagePreview.show()
            }?:kotlin.run { dataState.error?.let { error->
                displayToast(error)
            } }
        })
    }
    private fun prepareImagePreview(){
    intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
        viewModel.prepareImagePreview(imageUri)
    }
}
    private fun displayImagePreview() {
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            val inputStream =contentResolver.openInputStream(imageUri)
            val bitmap =BitmapFactory.decodeStream(inputStream)
            imagePreview.setImageBitmap(bitmap)
            imagePreview.visibility = View.VISIBLE
        }
    }
    private fun setListeners() {
        BackImage.setOnClickListener{
            onBackPressed()
        }
    }
}