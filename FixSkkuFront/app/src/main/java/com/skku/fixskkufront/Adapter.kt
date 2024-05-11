package com.skku.fixskkufront


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


class AdminRoomAdapter (val data: ArrayList<AdminRoom>, val context: Context): BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val generatedView = inflater.inflate(R.layout.item_report, null)

        val textViewName = generatedView.findViewById<TextView>(R.id.textViewName)
        val textViewDetail = generatedView.findViewById<TextView>(R.id.textViewDetail)
        val textViewStatus = generatedView.findViewById<TextView>(R.id.textViewStatus)
        val textViewTime = generatedView.findViewById<TextView>(R.id.textViewTime)
        val imageViewThumbnail = generatedView.findViewById<ImageView>(R.id.imageViewProfile)
        val btnDetail = generatedView.findViewById<Button>(R.id.buttonDetail)

        textViewName.text = data[p0].name
        textViewDetail.text = data[p0].detail


        if(data[p0].status == "반려") {
            textViewStatus.setTextColor(Color.RED)
        }

        textViewTime.text = data[p0].time
        textViewStatus.text = data[p0].status
        imageViewThumbnail.setImageResource(data[p0].thumbnail)


        btnDetail.setOnClickListener {
        }

        return generatedView
    }
}
