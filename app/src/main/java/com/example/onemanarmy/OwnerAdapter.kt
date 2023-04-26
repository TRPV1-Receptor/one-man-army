package com.example.onemanarmy

import android.app.Activity
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.w3c.dom.Text
import java.security.acl.Owner

class OwnerAdapter(private val context: Activity, arrayList: ArrayList<OwnerModel>):ArrayAdapter<OwnerModel>(context, R.layout.search_item,arrayList), Filterable {

    private var filteredOwners = arrayList
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View =  inflater.inflate(R.layout.search_item,null)

        val imageView : ImageView = view.findViewById(R.id.profile_pic)
        val name : TextView = view.findViewById(R.id.person_name)
        val descrip : TextView = view.findViewById(R.id.person_description)

        filteredOwners[position].profilePic?.let { imageView.setImageResource(it.toInt()) }
        name.text = filteredOwners[position].firstName
        descrip.text = filteredOwners[position].serviceProvided

        return view
    }

    fun update(results:ArrayList<OwnerModel>){
        Log.d("results",results.toString())
        filteredOwners.clear()
        notifyDataSetChanged()
        filteredOwners.addAll(results)
        notifyDataSetChanged()

    }



}