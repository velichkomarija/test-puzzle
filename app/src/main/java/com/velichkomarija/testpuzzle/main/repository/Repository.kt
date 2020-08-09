package com.velichkomarija.testpuzzle.main.repository

import android.util.Log
import com.velichkomarija.testpuzzle.main.App
import com.velichkomarija.testpuzzle.main.data.ImageSource
import java.io.IOException

/***
 * Класс-репозиторий, отвечающий за загрузку данных.
 */
object Repository {

    const val IMG_PATH = "img"

    /**
     * Метод загрузки изображений из assets.
     *
     * @return список объектов типа ImageSource.
     */
    fun loadImages(): List<ImageSource> {

        val list = mutableListOf<ImageSource>()

        try {
            val listAssets = App.applicationContext()
                .assets.list(IMG_PATH)

            if (listAssets != null) {
                for (item in listAssets) {
                    list.add(
                        ImageSource(
                            item
                        )
                    )
                }
            }
        } catch (e: IOException) {
            Log.e("MainFragment", "IO Exception")
        }

        return list
    }

}