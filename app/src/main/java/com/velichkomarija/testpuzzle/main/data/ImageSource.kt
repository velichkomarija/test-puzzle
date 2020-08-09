package com.velichkomarija.testpuzzle.main.data

/**
 * Дата-класс, описывающий сущность детали изображения.
 *
 * @property sourceString строка названия части изображения.
 * @property isCheck признак выбора части изображения.
 */
data class ImageSource(val sourceString: String, var isCheck: Boolean = false) {
}