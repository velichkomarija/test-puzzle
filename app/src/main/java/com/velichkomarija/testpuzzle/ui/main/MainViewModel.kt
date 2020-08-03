package com.velichkomarija.testpuzzle.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.velichkomarija.testpuzzle.ImageSource

class MainViewModel(list: List<ImageSource>) : ViewModel() {

    private val listImages: MutableLiveData<List<ImageSource>> = MutableLiveData()
    private var images = Transformations.map(listImages) { images ->
        images.filter { it.isCheck }
    }

    init {
        listImages.postValue(list)
    }

    fun getImages(): LiveData<List<ImageSource>> = images

    fun handleInvalidate() {

        for (item in listImages.value!!){
            item.isCheck = false
        }

//        listImages.value?.forEach {
//            it.isCheck = false
//        }
//        return listImages.value?.map { it }
    }

    fun handleSelectedItem(sourcePath: String) {
        listImages.value = listImages.value?.map {
            if (it.sourceString == sourcePath) {
                it.isCheck = !it.isCheck
            }
            it
        }
    }

}
