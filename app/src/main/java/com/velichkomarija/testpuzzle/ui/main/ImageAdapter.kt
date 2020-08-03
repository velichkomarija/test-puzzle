package com.velichkomarija.testpuzzle.ui.main

import android.content.Context
import android.graphics.PorterDuff
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.velichkomarija.testpuzzle.ImageSource
import com.velichkomarija.testpuzzle.R

class ImageAdapter(
    private val context: Context,
    private var itemList: ArrayList<ImageSource>,
    private val clickListener: (ImageSource) -> Unit
) : BaseAdapter(), TopMenuInterface {

    private class ViewHolder(view: View?) {
        var image: ImageView? = null

        init {
            this.image = view?.findViewById(R.id.image)
        }
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val convertView: View
        val viewHolder: ViewHolder

        if (view == null) {
            val inflator =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflator.inflate(R.layout.image_item, null)
            viewHolder = ViewHolder(convertView)
            convertView.tag = viewHolder
        } else {
            convertView = view
            viewHolder = view.tag as ViewHolder
        }

        val element = itemList[position]
        viewHolder.image?.clipToOutline = true

        Picasso.get()
            .load(Uri.parse("file:///android_asset/img/" + element.sourceString))
            .placeholder(R.drawable.ic_image_black_108dp)
            .error(R.drawable.ic_broken_image_black_108dp)
            .fit()
            .into(viewHolder.image)

        viewHolder.image?.setOnClickListener {
            clickListener.invoke(element)

            if (element.isCheck) {
                viewHolder.image?.setColorFilter(0x50000000, PorterDuff.Mode.DARKEN)
                viewHolder.image?.foreground = context.getDrawable(R.drawable.ic_check_black_24dp)
                viewHolder.image?.foregroundGravity = Gravity.CENTER
            } else {
                viewHolder.image?.colorFilter = null
                viewHolder.image?.foreground = null
            }
        }

        return convertView
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun selectRandomItem() {
        for (item in itemList) {
            if (!item.isCheck) {
                clickListener.invoke(item)
                break
            }
        }
    }
}