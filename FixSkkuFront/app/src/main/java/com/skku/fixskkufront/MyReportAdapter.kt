package com.skku.fixskkufront

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyReportAdapter(private val items: List<MyReportItem>) : RecyclerView.Adapter<MyReportAdapter.MyReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_report_item, parent, false)
        return MyReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyReportViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.titleTextView.text = item.title
        holder.subtitleTextView.text = item.subtitle
        holder.tag1.text = item.tag1
        holder.tag2.text = item.tag2
        holder.tag3.text = item.tag3
        holder.tag5.text = item.tag5

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ReportDetailActivity::class.java).apply {
                putExtra("report_id", item.reportId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MyReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.itemImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.itemTitleTextView)
        val subtitleTextView: TextView = itemView.findViewById(R.id.itemSubtitleTextView)
        val tag1: TextView = itemView.findViewById(R.id.tag1)
        val tag2: TextView = itemView.findViewById(R.id.tag2)
        val tag3: TextView = itemView.findViewById(R.id.tag3)
        val tag5: TextView = itemView.findViewById(R.id.tag5)
    }
}
