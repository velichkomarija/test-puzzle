package com.velichkomarija.testpuzzle.main.ui.adapter

import android.graphics.PorterDuff
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.velichkomarija.testpuzzle.R
import com.velichkomarija.testpuzzle.main.data.ImageSource
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.image_item.view.*

/**
 * Класс-адаптер.
 * Предназначен для отображения составных частей изображения.
 *
 * @property itemList список объектов-частей картинки.
 * @property clickListener обработчик нажатия на элемент списка.
 */
class ImageAdapterRecyclerView(
    private var itemList: List<ImageSource>,
    private val clickListener: (ImageSource) -> Unit
) :
    RecyclerView.Adapter<ImageAdapterRecyclerView.ViewHolder>() {

    /**
     * Константы класса.
     */
    companion object {
        const val ASSERT_PATH = "file:///android_asset/img/"
    }

    /**
     * Внутренний класс, реализующий  класс RecyclerView.ViewHolder, интерфейс LayoutContainer.
     *
     */
    class ViewHolder(convertView: View) :
        RecyclerView.ViewHolder(convertView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        /**
         * Метод привязки элементов.
         *
         * @param item объект класса ImageSource.
         * @param clickListener обработчик нажатия на элемент списка.
         */
        fun bind(item: ImageSource, clickListener: (ImageSource) -> Unit) {

            itemView.image.clipToOutline = true
            configImage(itemView.image, item.isCheck)

            Picasso.get()
                .load(Uri.parse(ASSERT_PATH + item.sourceString))
                .placeholder(R.drawable.ic_image_black_108dp)
                .error(R.drawable.ic_broken_image_black_108dp)
                .fit()
                .into(itemView.image)

            itemView.image?.setOnClickListener {
                clickListener.invoke(item)
                configImage(itemView.image, item.isCheck)
            }
        }

        /**
         * Метод, конфигурирующий вид элемента.
         * Выбрано/невыбрано.
         */
        private fun configImage(itemView: ImageView, flag: Boolean) {
            if (flag) {
                itemView.image?.setColorFilter(0x50000000, PorterDuff.Mode.DARKEN)
                itemView.image?.foreground =
                    itemView.context.getDrawable(R.drawable.ic_check_black_24dp)
                itemView.image?.foregroundGravity = Gravity.CENTER
            } else {
                itemView.image?.colorFilter = null
                itemView.image?.foreground = null
            }
        }
    }

    /**
     * Реализация метода onCreateViewHolder.
     * Вызывается, когда RecyclerView нуждается в новом RecyclerView.
     * Хранитель вида данного типа для представления элемента.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(R.layout.image_item, parent, false)
        )
    }

    /**
     * Реализация метода getItemCount.
     * Возвращает количество элементов списка.
     */
    override fun getItemCount(): Int {
        return itemList.size
    }

    /**
     * Реализация метода onBindViewHolder.
     * Вызывается RecyclerView для отображения данных в указанной позиции.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], clickListener)
    }
}