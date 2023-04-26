package com.example.onemanarmy

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ReceiptScreenAdapter(private val context: Activity,private val arrayList: ArrayList<Pair<String,String>>): ArrayAdapter<Pair<String, String>>(context,R.layout.item_receipt_list,arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_receipt_list,null)

        val title : TextView = view.findViewById(R.id.receiptTitle)
        val date : TextView = view.findViewById(R.id.receiptDate)
        val url : TextView = view.findViewById(R.id.hidden)

        val clean = cleanTitle(arrayList[position].first)


        title.text = clean.first
        date.text = clean.second
        url.text = arrayList[position].second

        return view

    }
    private fun cleanTitle(oldtitle : String) : Pair<String,String> {
        val temp = oldtitle.split("_").toTypedArray()
        val date = temp[0]
        val s1 = date.substring(0,2)
        val s2 = date.substring(2,4)
        val s3 = date.substring(4)

        val cleanDate = "$s1-$s2-$s3"

        val time = temp[1]
        val t1 = time.substring(0,2)
        val t2 = time.substring(2,4)
        val t3 = time.substring(4)

        val cleanTime = "$t1:$t2:$t3"

        val result = Pair(cleanDate,cleanTime)
        //val result = Pair(date,time) For debugging

        return result



    }
}