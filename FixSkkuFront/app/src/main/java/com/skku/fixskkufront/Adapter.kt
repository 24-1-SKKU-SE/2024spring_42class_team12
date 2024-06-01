package com.skku.fixskkufront


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import java.util.Locale
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class AdminRoomAdapter (var data: ArrayList<AdminRoom>, val context: Context): BaseAdapter(),
    Filterable {
    private lateinit var dialog: Dialog

    init {
        AdminActivity.items = AdminActivity.item_init
        clearSharedPreferences()
    }
    fun updateList(newList: List<AdminRoom>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
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

        if(data[p0].status == "반려") { textViewStatus.setTextColor(Color.RED) }

        textViewTime.text = data[p0].time
        textViewStatus.text = data[p0].status
        imageViewThumbnail.setImageResource(data[p0].thumbnail)

        btnDetail.setOnClickListener {
            showPopupDialog(p0, data[p0].name, data[p0].time ,data[p0].text, data[p0].status, data[p0].pos, data[p0].thumbnail, data[p0].photo)
        }

        return generatedView
    }

    private fun showPopupDialog(p0: Int, name: String, day: String, text: String, status:String ,pos: String, thumbnail: Int, photo: String) {
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

        val imageThumnail = dialog.findViewById<ImageView>(R.id.imageViewDetailThum)
        val textDetailName = dialog.findViewById<TextView>(R.id.textViewDetailName)
        val textDetailDay = dialog.findViewById<TextView>(R.id.textViewDetailDay)
        val textDetailText = dialog.findViewById<TextView>(R.id.textViewDetailText)
        val textDetailPos = dialog.findViewById<TextView>(R.id.textViewDetailPos)
        val textDetailStatus = dialog.findViewById<TextView>(R.id.textViewDetailStatus)
        val imagePhoto = dialog.findViewById<ImageView>(R.id.imagePhoto)
        imageThumnail.setImageResource(thumbnail)
        textDetailStatus.text = status
        textDetailName.text = name
        textDetailDay.text = day
        textDetailText.text = "'" + text + "'"
        textDetailPos.text = pos
        if (photo.isEmpty()) {
            imagePhoto.setImageResource(R.drawable.baseline_image_not_supported_24)
        }
        else {
            Glide.with(context)
                .load(photo)
                //.placeholder(R.drawable.placeholder) // 로딩 중에 표시할 이미지 (선택 사항)
                .error(R.drawable.baseline_error_outline_24) // 에러가 발생할 경우 표시할 이미지 (선택 사항)
                .into(imagePhoto)
        }

        val textreject = dialog.findViewById<EditText>(R.id.textViewDetailReject)
        val savedComment = getSavedComment(p0)
        textreject.setText(savedComment) // 저장된 내용 불러와서 설정

        var stat = status

        val btnClose = dialog.findViewById<ImageButton>(R.id.imageButtonClose)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        val btnOkay = dialog.findViewById<Button>(R.id.btn_okay)
        btnOkay.setOnClickListener {
            val comment = textreject.text.toString() // 에딧텍스트에 입력된 텍스트 가져오기
            saveComment(p0, comment) // 입력된 내용 저장
            data[p0].status = stat
            notifyDataSetChanged()
            /* 서버로 변경사항 내용 JSON 파일로 보내기. */
            val client = OkHttpClient()
            val path = "http://13.124.89.169:8080/"
//            data class Changed_Report(var reportStatus: String ?= null, var rejectionReason: String ?= null)
//            val json = Gson().toJson(Changed_Report(stat, comment))
//            val mediaType = "application/json; charset=utf-8".toMediaType()

            val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("reportStatus", stat)
                .addFormDataPart("rejectionReason", comment)
            val reqBody = builder.build()


            val req = Request.Builder().url(path).post(reqBody).build()
            client.newCall(req).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    response.use{
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    }
                }
            })

            dialog.dismiss()

        }

        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener {
            val originalIndex = AdminActivity.item_init.indexOf(data[p0]) // 원본 인덱스를 찾음
            // 인덱스가 유효한지
            if (originalIndex != -1 && originalIndex < AdminActivity.item_init.size) {
                data.removeAt(p0)
                notifyDataSetChanged()
                AdminActivity.item_init.removeAt(originalIndex)
            } else {
                Log.e("AdminRoomAdapter", "Invalid index: $originalIndex for item: $data[p0]")
            }
            dialog.dismiss()
        }

        val btnRight = dialog.findViewById<ImageButton>(R.id.imageButtonRight)
        btnRight.setOnClickListener{
            if(stat == "수리 접수") {
                stat = "수리 중"
                textDetailStatus.text = stat }
            else if(stat == "수리 중") {
                stat = "수리 완료"
                textDetailStatus.text = stat }
            else if(stat == "수리 완료") {
                stat = "반려"
                textDetailStatus.text = stat }
            else if(stat == "반려") {
                stat = "수리 접수"
                textDetailStatus.text = stat }
        }
        val btnLeft = dialog.findViewById<ImageButton>(R.id.imageButtonLeft)
        btnLeft.setOnClickListener{
            if(stat == "수리 접수") {
                stat = "반려"
                textDetailStatus.text = stat }
            else if(stat == "수리 중") {
                stat = "수리 접수"
                textDetailStatus.text = stat }
            else if(stat == "수리 완료") {
                stat = "수리 중"
                textDetailStatus.text = stat }
            else if(stat == "반려") {
                stat = "수리 완료"
                textDetailStatus.text = stat }
        }

        textDetailStatus.text = status
        dialog.show()
    }

    private fun saveComment(itemId: Int, comment: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("comment_$itemId", comment)
        editor.apply()
    }

    private fun getSavedComment(itemId: Int): String {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("comment_$itemId", "") ?: ""
    }

    private fun clearSharedPreferences() {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }


}

