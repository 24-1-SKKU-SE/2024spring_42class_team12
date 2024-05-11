package com.skku.fixskkufront


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat


class AdminRoomAdapter (val data: ArrayList<AdminRoom>, val context: Context): BaseAdapter() {
    private lateinit var dialog: Dialog

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

        val item = data[p0].time

        btnDetail.setOnClickListener {
            showPopupDialog(item)
        }

        return generatedView
    }

    private fun showPopupDialog(item: String) {
        // 다이얼로그 생성
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀 바 숨기기 (optional)

        // 커스텀 다이얼로그 레이아웃 설정
        dialog.setContentView(R.layout.report_detail_view)

        // 다이얼로그 배경 설정 (optional)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.custom_dialog_background))
        }

        // 다이얼로그 크기 설정
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // 다이얼로그 외부를 터치했을 때 다이얼로그가 닫히지 않도록 설정 (optional)
        dialog.setCancelable(true)

        // 다이얼로그 표시
        dialog.show()

    }

}
