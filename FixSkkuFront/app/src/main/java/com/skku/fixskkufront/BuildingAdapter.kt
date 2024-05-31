package com.skku.fixskkufront

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class BuildingAdapter(private val buildings: List<Building>, private val activity: FragmentActivity) : RecyclerView.Adapter<BuildingAdapter.BuildingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return BuildingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        val building = buildings[position]
        holder.textView.text = building.name
        holder.imageView.setImageResource(building.imageResId)
        holder.itemView.setOnClickListener {
            val fragment = SeatFragment()
            val bundle = Bundle()
            bundle.putString("building_name", building.name)
            fragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return buildings.size
    }

    class BuildingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
