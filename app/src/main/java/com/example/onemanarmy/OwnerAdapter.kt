package com.example.onemanarmy

import android.app.Activity
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text
import java.net.URL
import java.security.acl.Owner
class OwnerAdapter(private val context: Activity, arrayList: ArrayList<OwnerModel>): ArrayAdapter<OwnerModel>(context, R.layout.search_item,arrayList), Filterable {

    private var filteredOwners = arrayList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.search_item,null)

        val imageView : ImageView = view.findViewById(R.id.profile_pic)
        val name : TextView = view.findViewById(R.id.person_name)
        val descrip : TextView = view.findViewById(R.id.person_description)

        // Check if the profilePictureBitmap is already set
        if (filteredOwners[position].profilePictureBitmap != null) {
            imageView.setImageBitmap(filteredOwners[position].profilePictureBitmap)
        } else {
            // Download the image from the URL and set it to the ImageView
            val profilePic = filteredOwners[position].profilePicture
            Log.d("ProfilePic", profilePic.toString())
            val imageReference = profilePic?.let { Firebase.storage.getReferenceFromUrl(it) }

            val ONE_MEGABYTE: Long = 1024 * 1024

            if (imageReference!=null){
                imageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                    val bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                    imageView.setImageBitmap(bmp)
                }.addOnFailureListener{
                    Log.d("Exception", it.toString())
                }
            }else{
                imageView.setImageResource(R.drawable.profile_icon)
            }
        }

        name.text = filteredOwners[position].firstName
        descrip.text = filteredOwners[position].serviceProvided

        return view
    }

    fun update(results: ArrayList<OwnerModel>) {
        filteredOwners.clear()
        notifyDataSetChanged()
        filteredOwners.addAll(results)
        notifyDataSetChanged()
    }
}