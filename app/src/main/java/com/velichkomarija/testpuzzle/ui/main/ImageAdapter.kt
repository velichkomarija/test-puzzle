package com.velichkomarija.testpuzzle.ui.main

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.squareup.picasso.Picasso
import com.velichkomarija.testpuzzle.ImageSource
import com.velichkomarija.testpuzzle.R
import kotlinx.android.synthetic.main.image_item.view.*

class ImageAdapter(private val context: Context, private val itemList: List<ImageSource>) :
    BaseAdapter() {

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val element = itemList[position]
        var inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView = inflator.inflate(R.layout.image_item, null)
        itemView.image.clipToOutline = true

        Picasso.get()
            .load(Uri.parse("file:///android_asset/img/" + element.sourceString))
            .placeholder(R.drawable.ic_image_black_108dp)
            .error(R.drawable.ic_broken_image_black_108dp)
            .fit()
            .into(itemView.image)

        itemView.setOnClickListener {
            element.isCheck= !element.isCheck
        }

        return itemView
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
}