package com.example.filterphoto.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filterphoto.repo.RepoEditImage
import com.example.filterphoto.utilities.Coroutines

class EditImageViewModel(private val editImageRepo :RepoEditImage):ViewModel() {
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
}