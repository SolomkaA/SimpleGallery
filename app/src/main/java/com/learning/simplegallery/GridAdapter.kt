package com.learning.simplegallery

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


class GridAdapter(val context: Context, val imagesList: MutableList<MainActivity.Image>) : BaseAdapter() {

    override fun getCount(): Int {
        return imagesList.size
    }

    override fun getItem(p0: Int): Any {
        return imagesList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var v1ew: View
        var viewHolder: ViewHolder
        if (p1 == null) {
            v1ew = View.inflate(context, R.layout.grid_item, null)
            viewHolder = ViewHolder(v1ew)
            v1ew.setTag(viewHolder)
        }
        else {
            viewHolder = p1.getTag() as ViewHolder
            v1ew = p1
        }


        var text = viewHolder.textView
        text.text = imagesList[p0].name

        var imageView = viewHolder.imageView

        Glide
                .with(context)
                .load(imagesList[p0].uri)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //.placeholder(R.drawable.loading_spinner)
                .into(imageView)
        return v1ew
    }

    }

    class ViewHolder(view: View) {
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val textView = view.findViewById<TextView>(R.id.textView)
    }

