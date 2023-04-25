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

    fun String.addCharAtIndex(char:Char,index: Int) = StringBuilder(this).apply { insert(index,char) }.toString()

    private fun cleanTitle(oldtitle : String) : Pair<String,String> {
        val temp = oldtitle.split("_").toTypedArray()
        val date = temp[0].addCharAtIndex('-',2)
        val cleanDate = date.addCharAtIndex('-',5)

        val time = temp[1].addCharAtIndex(':',2)
        val cleanTime = time.addCharAtIndex(':',5)

        val result = Pair(cleanDate,cleanTime)

        return result



    }
}