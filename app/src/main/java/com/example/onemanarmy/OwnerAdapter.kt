package com.example.onemanarmy

import android.app.Activity
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text

class OwnerAdapter(private val context: Activity, private val arrayList: ArrayList<OwnerModel>):ArrayAdapter<OwnerModel>(context, R.layout.search_item,arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View =  inflater.inflate(R.layout.search_item,null)

        val imageView : ImageView = view.findViewById(R.id.profile_pic)
        val name : TextView = view.findViewById(R.id.person_name)
        val descrip : TextView = view.findViewById(R.id.person_description)

        arrayList[position].profilePic?.let { imageView.setImageResource(it) }
        name.text = arrayList[position].firstName
        descrip.text = arrayList[position].serviceProvided

        return view
    }
}