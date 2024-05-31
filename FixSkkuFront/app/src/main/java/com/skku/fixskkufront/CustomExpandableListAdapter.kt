package com.skku.fixskkufront

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

class CustomExpandableListAdapter(private val context: Context, private val listData: List<Pair<String, List<String>>>) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return listData.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listData[groupPosition].second.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return listData[groupPosition].first
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return listData[groupPosition].second[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false)
        }
        val textView = convertView?.findViewById<TextView>(android.R.id.text1)
        textView?.fontFeatureSettings = "@font/pretendard_variable"
        textView?.text = getGroup(groupPosition) as String
        return convertView!!
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_2, parent, false)
        }
        val textView = convertView?.findViewById<TextView>(android.R.id.text1)
        textView?.fontFeatureSettings = "@font/pretendard_variable"
        textView?.text = getChild(groupPosition, childPosition) as String
        return convertView!!
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
