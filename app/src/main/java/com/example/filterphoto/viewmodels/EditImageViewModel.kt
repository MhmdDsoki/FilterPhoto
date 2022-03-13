package com.example.filterphoto.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filterphoto.data.ImageFilter
import com.example.filterphoto.repo.RepoEditImage
import com.example.filterphoto.utilities.Coroutines

class EditImageViewModel(private val editImageRepo :RepoEditImage):ViewModel() {
    //prepare image preview
    private val imagePreviewDataState =MutableLiveData<ImagePreviewDataState>()
    val  imagePreviewUiState : LiveData<ImagePreviewDataState> get() =imagePreviewDataState
    fun prepareImagePreview(imageUri:Uri){
        Coroutines.io {
            runCatching {
                emitImagePreviewUIState(isLoading = true)
                editImageRepo.prepareImageView(imageUri)
            }.onSuccess {bitmap ->
                if (bitmap !=null)
                {
                    emitImagePreviewUIState(bitmap = bitmap)
                }else {
                    emitImagePreviewUIState(error = "can't prepare this image")
                }
            }.onFailure {
                emitImagePreviewUIState(error = it.message.toString())
            }
        }
    }
    //parameter that we will emite for
    private fun emitImagePreviewUIState(
        isLoading:Boolean =false,
        bitmap: Bitmap? =null,
        error: String?=null
    ){
            val dataState=ImagePreviewDataState(isLoading,bitmap,error)
            //postValue and value
            imagePreviewDataState.postValue(dataState)
    }

    data class ImagePreviewDataState(
        val isLoading:Boolean?,
        val bitmap: Bitmap?,
        val error:String?
    )
    private val imageFiltersDataState = MutableLiveData<ImageFiltersDataState>()
    val imageFiltersUiState:LiveData<ImageFiltersDataState> get() = imageFiltersDataState

    fun loadImageFilters(originalImage: Bitmap){
        Coroutines.io {
            runCatching {
                emitImageFilterUiState(isLoading = true)
                editImageRepo.getImageFilters(getPreviewImage(originalImage))
            }.onSuccess { imageFilter->
                emitImageFilterUiState(imageFilter = imageFilter)
            }.onFailure {
                emitImageFilterUiState(error=it.message.toString()) }
        }
    }

    private fun getPreviewImage(originalImage:Bitmap):Bitmap{
        return runCatching {
            val previewWidth=150
            val previewHeight =originalImage.height *previewWidth /originalImage.width
            Bitmap.createScaledBitmap(originalImage,previewWidth,previewHeight,false)
        }.getOrDefault(originalImage)
    }

    private fun emitImageFilterUiState(
        isLoading: Boolean= false,
        imageFilter: List<ImageFilter>?=null,
        error: String? =null
    ){
        val dataState =ImageFiltersDataState(isLoading,imageFilter,error)
        imageFiltersDataState.postValue(dataState)
    }

    //Load image filter
    data class ImageFiltersDataState(
        val isLoading:Boolean?,
        val imageFilter: List<ImageFilter>?,
        val error: String?
    )
}