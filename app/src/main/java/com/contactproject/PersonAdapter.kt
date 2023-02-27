package com.contactproject


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

@Suppress("DEPRECATION")
class PersonAdapter(private val context: Context, private val arrayList: java.util.ArrayList<Person>) : BaseAdapter() {


    private lateinit var prenomNom: TextView
    private lateinit var num: TextView
    private lateinit var photo: ImageView


    override fun getCount(): Int {
        return arrayList.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val viewCov = LayoutInflater.from(context).inflate(R.layout.row_items, parent, false)

        prenomNom = viewCov.findViewById(R.id.prenom)
        num = viewCov.findViewById(R.id.nom)
        photo = viewCov.findViewById(R.id.item_info)
        prenomNom.text = arrayList[position].lastName + " " + arrayList[position].firstName
        num.text = arrayList[position].num
        val image = arrayList[position].imageuri
        if (image != "null" && image != "Default" && !image.startsWith("content://")){
            val bmp = photoActions.loadPhotoFromExternalStorage(image) as Bitmap
            photo.setImageBitmap(bmp)

        }


        return viewCov
    }
}