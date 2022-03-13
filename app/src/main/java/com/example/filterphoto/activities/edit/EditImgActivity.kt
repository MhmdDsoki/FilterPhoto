package com.example.filterphoto.activities.edit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.filterphoto.R
import com.example.filterphoto.activities.main.MainActivity
import com.example.filterphoto.adapter.ImageFiltersAdapter
import com.example.filterphoto.data.ImageFilter
import com.example.filterphoto.listener.ImageFilterListener
import com.example.filterphoto.utilities.displayToast
import com.example.filterphoto.utilities.show
import com.example.filterphoto.viewmodels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import kotlinx.android.synthetic.main.edit_img.*
import kotlinx.android.synthetic.main.item_container_filter.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImgActivity : AppCompatActivity(),ImageFilterListener {
    private val viewModel:EditImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage
    //ImageBitmap
    private lateinit var originalBitmap: Bitmap
    val filteredBitmap =MutableLiveData<Bitmap>()
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
                //for the first time "filteredImage =originalImage"
                originalBitmap =bitmap
                filteredBitmap.value=bitmap
                with(originalBitmap){
                    gpuImage.setImage(this)
                    imagePreview.show()
                    viewModel.loadImageFilters(this)
                }
               // imagePreview.setImageBitmap(bitmap)

            }?:kotlin.run { dataState.error?.let { error->
                displayToast(error)
            } }
        })
        viewModel.imageFiltersUiState.observe(this,{
            val imageFiltersDataState =it?:return@observe
            imageFilterProgress.visibility=
                if (imageFiltersDataState.isLoading == true)View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilter?.let {imageFilters ->
                    ImageFiltersAdapter(imageFilters,this).also { adapter ->
                    filterRecyclerView.adapter =adapter
                } }?: kotlin.run { imageFiltersDataState.error?.let {error->
                    displayToast(error)
            } }
        })
        filteredBitmap.observe(this,{bitmap ->
            imagePreview.setImageBitmap(bitmap)
        })
    }

    private fun prepareImagePreview(){
        gpuImage = GPUImage(applicationContext)
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
            //showing original image when we longclick the imageViewuntil we release clicked
            //so we can see differences between original image and filtered image
        }
        imagePreview.setOnLongClickListener{
            imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        imagePreview.setOnClickListener {
            imagePreview.setImageBitmap(filteredBitmap.value)
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                filteredBitmap.value =bitmapWithFilterApplied
            }
        }

    }
}