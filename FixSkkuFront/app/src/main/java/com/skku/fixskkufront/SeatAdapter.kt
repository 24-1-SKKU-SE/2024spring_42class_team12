package com.skku.fixskkufront

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
class SeatAdapter(private val seats: List<Seat>, private val activity: FragmentActivity) : RecyclerView.Adapter<SeatAdapter.SeatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_seat, parent, false)
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val seat = seats[position]
        if (seat.isEmpty) {
            holder.imageView.visibility = View.INVISIBLE // Hide the empty seat
        } else {
            holder.imageView.visibility = View.VISIBLE
            seat.imageResId?.let {
                holder.imageView.setImageResource(it)
            }
            holder.itemView.setOnClickListener {
                // Handle seat item click
                val intent = Intent(activity, ReportActivity::class.java)
                intent.putExtra("seat_image_res_id", seat.imageResId ?: 0)
                activity.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return seats.size
    }

    class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.seatImageView)
    }
}
