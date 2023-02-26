package com.contactproject


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class PersonAdapter(private val context: Context, private val arrayList: java.util.ArrayList<Person>) : BaseAdapter() {


    private lateinit var prenomNom: TextView
    private lateinit var num: TextView
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
        prenomNom.text = arrayList[position].firstName + " " + arrayList[position].lastName
        num.text = arrayList[position].num
        return viewCov
    }
}