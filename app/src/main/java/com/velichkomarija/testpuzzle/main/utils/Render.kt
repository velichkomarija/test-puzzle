package com.velichkomarija.testpuzzle.main.utils

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import com.velichkomarija.testpuzzle.main.data.ImageSource
import com.velichkomarija.testpuzzle.main.App
import java.io.InputStream

/**
 * Класс, отвечающий за создание изображения.
 */
object Render {

    private lateinit var resultBitmap: Bitmap
    private lateinit var canvas: Canvas
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * Метод, загрузки и составления картинки из массива элементов.
     *
     * @param
     *
     * @return
     */
    fun loadBitmapFromAssets(items: List<ImageSource>): Bitmap {

        val assetManager: AssetManager = App.applicationContext().assets

        resultBitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
        canvas = Canvas(
            resultBitmap
        )

        for (item in items) {
            val stream: InputStream = assetManager.open("img/" + item.sourceString)
            val bitmap = BitmapFactory.decodeStream(stream)
            canvas.drawBitmap(bitmap, 0f, 0f,
                paint
            )
        }
        return resultBitmap
    }

    /**
     * Метод, возвращающий составленное изображение.
     *
     * @return объект класса Bitmap.
     */
    fun getBitmap(): Bitmap =
        resultBitmap
}