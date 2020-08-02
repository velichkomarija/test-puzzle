package com.velichkomarija.testpuzzle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.velichkomarija.testpuzzle.ui.main.MainViewModel


class MainViewModelFactory( private val images: List<ImageSource>) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(images) as T
    }
}