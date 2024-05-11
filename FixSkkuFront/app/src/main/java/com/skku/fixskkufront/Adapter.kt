package com.skku.fixskkufront


import android.app.Dialog
import android.content.Context
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
        val textViewPos = generatedView.findViewById<TextView>(R.id.textViewDetail)
        val textViewStatus = generatedView.findViewById<TextView>(R.id.textViewStatus)
        val textViewTime = generatedView.findViewById<TextView>(R.id.textViewTime)
        val imageViewThumbnail = generatedView.findViewById<ImageView>(R.id.imageViewProfile)
        val btnDetail = generatedView.findViewById<Button>(R.id.buttonDetail)

        textViewName.text = data[p0].name
        textViewPos.text = data[p0].pos


        if(data[p0].status == "반려") {
            textViewStatus.setTextColor(Color.RED)
        }

        textViewTime.text = data[p0].time
        textViewStatus.text = data[p0].status
        imageViewThumbnail.setImageResource(data[p0].thumbnail)


        btnDetail.setOnClickListener {
            showPopupDialog(data[p0].name, data[p0].time ,data[p0].text, data[p0].status, data[p0].pos, data[p0].thumbnail)
        }

        return generatedView
    }

    private fun showPopupDialog(name: String, day: String, text: String, status:String ,pos: String, thumbnail: Int) {
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

        /* 디테일한 내용 전달 */

        val imageThumnail = dialog.findViewById<ImageView>(R.id.imageViewDetailThum)
        val textDetailName = dialog.findViewById<TextView>(R.id.textViewDetailName)
        val textDetailDay = dialog.findViewById<TextView>(R.id.textViewDetailDay)
        val textDetailText = dialog.findViewById<TextView>(R.id.textViewDetailText)
        val textDetailPos = dialog.findViewById<TextView>(R.id.textViewDetailPos)
        val textDetailStatus = dialog.findViewById<TextView>(R.id.textViewDetailStatus)
        imageThumnail.setImageResource(thumbnail)
        textDetailStatus.text = status
        textDetailName.text = name
        textDetailDay.text = day
        textDetailText.text = "'" + text + "'"
        textDetailPos.text = pos

        // Okay 버튼 누를 때 변경 사항 저장.
        val btnOkay = dialog.findViewById<Button>(R.id.btn_okay)
        btnOkay.setOnClickListener {
            dialog.dismiss()
        }
        // 변경 사항 취소
        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        // 다이얼로그 표시
        dialog.show()

    }

}
