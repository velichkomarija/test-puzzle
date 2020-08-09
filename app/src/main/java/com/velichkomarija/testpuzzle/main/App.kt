package com.velichkomarija.testpuzzle.main

import android.app.Application
import android.content.Context

/**
 * Надстройка приложения.
 */
class App : Application() {

    /**
     * Объект, предоставляющий контекст.
     */
    companion object {
        private var instance: App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    /**
     * Инициализатор класса.
     */
    init {
        instance = this
    }
}