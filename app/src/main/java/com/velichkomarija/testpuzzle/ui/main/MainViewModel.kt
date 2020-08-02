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

    fun handleSelectedItem(sourcePath: String) {

//        if(listImages.value != null) {
//            for (item in listImages.value!!) {
//                if (item.sourceString == sourcePath) {
//                    item.isCheck != item.isCheck
//                }
//            }
//        }

        listImages.value = listImages.value?.map {
            if (it.sourceString == sourcePath) {
                it.isCheck = !it.isCheck
            }
            it
        }
    }

}
