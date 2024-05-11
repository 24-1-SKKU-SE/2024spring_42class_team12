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
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat


class AdminRoomAdapter (val data: ArrayList<AdminRoom>, val context: Context): BaseAdapter() {
    private lateinit var dialog: Dialog

    init {
        // 앱 시작 시 SharedPreferences 초기화
        clearSharedPreferences()
    }

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
            showPopupDialog(p0, data[p0].name, data[p0].time ,data[p0].text, data[p0].status, data[p0].pos, data[p0].thumbnail)
        }

        return generatedView
    }

    private fun showPopupDialog(p0: Int, name: String, day: String, text: String, status:String ,pos: String, thumbnail: Int) {
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

        // 초기화
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

        val textreject = dialog.findViewById<EditText>(R.id.textViewDetailReject)
        val savedComment = getSavedComment(p0)
        textreject.setText(savedComment) // 저장된 내용 불러와서 설정

        var stat = status

        val btnClose = dialog.findViewById<ImageButton>(R.id.imageButtonClose)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        // Okay 버튼 누를 때 변경 사항 저장.
        val btnOkay = dialog.findViewById<Button>(R.id.btn_okay)
        btnOkay.setOnClickListener {
            val comment = textreject.text.toString() // 에딧텍스트에 입력된 텍스트 가져오기
            saveComment(p0, comment) // 입력된 내용 저장
            data[p0].status = stat // 상태 변경
            notifyDataSetChanged() // 데이터 변경을 어댑터에 알려줍니다.
            dialog.dismiss()
        }
        // 삭제
        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener {
            data.removeAt(p0) // 리스트 삭제
            notifyDataSetChanged() // 데이터 변경을 어댑터에 알려줍니다.
            dialog.dismiss()
        }


        // 상태 변경 버튼
        val btnRight = dialog.findViewById<ImageButton>(R.id.imageButtonRight)
        btnRight.setOnClickListener{
            if(stat == "수리 접수"){
                stat = "수리 중"
                textDetailStatus.text = stat // UI 상태 변경
            }
            else if(stat == "수리 중"){
                stat = "수리 완료"
                textDetailStatus.text = stat // UI 상태 변경
            }
            else if(stat == "수리 완료"){
                stat = "반려"
                textDetailStatus.text = stat // UI 상태 변경
            }
            else if(stat == "반려"){
                stat = "수리 접수"
                textDetailStatus.text = stat // UI 상태 변경
            }
        }
        val btnLeft = dialog.findViewById<ImageButton>(R.id.imageButtonLeft)
        btnLeft.setOnClickListener{
            if(stat == "수리 접수"){
                stat = "반려"
                textDetailStatus.text = stat // UI 상태 변경
            }
            else if(stat == "수리 중"){
                stat = "수리 접수"
                textDetailStatus.text = stat // UI 상태 변경
            }
            else if(stat == "수리 완료"){
                stat = "수리 중"
                textDetailStatus.text = stat // UI 상태 변경
            }
            else if(stat == "반려"){
                stat = "수리 완료"
                textDetailStatus.text = stat // UI 상태 변경
            }


        }

        textDetailStatus.text = status

        // 다이얼로그 표시
        dialog.show()

    }

    // SharedPreferences를 이용하여 itemId에 대한 입력된 내용 저장
    private fun saveComment(itemId: Int, comment: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("comment_$itemId", comment)
        editor.apply()
    }

    // SharedPreferences에서 itemId에 대한 입력된 내용 불러오기
    private fun getSavedComment(itemId: Int): String {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("comment_$itemId", "") ?: ""
    }

    private fun clearSharedPreferences() {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}
