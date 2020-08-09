package com.velichkomarija.testpuzzle.main.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.velichkomarija.testpuzzle.main.data.ImageSource
import com.velichkomarija.testpuzzle.main.repository.Repository
import com.velichkomarija.testpuzzle.main.ui.adapter.ImageAdapterRecyclerView

/**
 * Класс, реализующий базовый класс ViewModel.
 */
class MainViewModel : ViewModel() {

    private val listImages: List<ImageSource> = Repository.loadImages()
    private var images: MutableLiveData<List<ImageSource>> = MutableLiveData()
    private val adapter: ImageAdapterRecyclerView

    init {
        images.value = listImages.filter {
            it.isCheck
        }

        adapter =
            ImageAdapterRecyclerView(
                listImages
            ) { handleSelectedItem(it.sourceString) }
    }

    /**
     * Метод, возвращающий адаптер списка.
     */
    fun getAdapter(): ImageAdapterRecyclerView = adapter

    /**
     * Метод, возвращающий список, выбранных изображений.
     */
    fun getImages(): LiveData<List<ImageSource>> = images

    /**
     * Метод, сбрасывающий выбранные элементы.
     * Очищает список выбранных элементов.
     */
    fun handleInvalidate() {
        for (item in listImages) {
            item.isCheck = false
        }

        images.value = listOf()
        adapter.notifyDataSetChanged()
    }

    /**
     * Метод, выбирающий случайный элемент из списка.
     */
    fun handleSelectRandomItem() {

        for (item in listImages.shuffled()) {
            if (!item.isCheck) {
                item.isCheck = true
                break
            }
        }

        images.value = listImages.filter {
            it.isCheck
        }

        adapter.notifyDataSetChanged()
    }

    /**
     * Метод, оповещающий о выборе элемента.
     *
     * @param sourcePath название части изображения.
     */
    private fun handleSelectedItem(sourcePath: String) {
        for (item in listImages) {
            if (item.sourceString == sourcePath) {
                item.isCheck = !item.isCheck
                break
            }
        }

        images.value = listImages.filter {
            it.isCheck
        }
    }

}
