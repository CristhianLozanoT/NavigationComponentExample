package com.example.navigationcomponentexample.viewmodels

import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {
    private val photoUris = mutableListOf<String>()

    val currentPhotoUri: String?
        get() = photoUris.lastOrNull()

    fun addPhoto(uri: String) {
        photoUris.add(uri)
    }

    fun deleteCurrentPhoto() {
        if (photoUris.isNotEmpty()) {
            photoUris.removeLast()
        }
    }
}
