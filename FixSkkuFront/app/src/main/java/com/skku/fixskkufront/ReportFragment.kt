package com.skku.fixskkufront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

class ReportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_report, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val insaBtn: View = view.findViewById<View>(R.id.insaView)
        val jayeonBtn: View = view.findViewById<View>(R.id.jayeonView)

        insaBtn.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            Toast.makeText(requireContext(), "Insa clicked!", Toast.LENGTH_SHORT).show()
            openBuildingFragment(0)

        }

        jayeonBtn.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            Toast.makeText(requireContext(), "Jayeon clicked!", Toast.LENGTH_SHORT).show()
            openBuildingFragment(1)

        }
    }
    private fun openBuildingFragment(campusType : Int) {
        val buildingFragment = BuildingFragment()
        val args = Bundle()
        args.putInt("campusType", campusType)
        buildingFragment.arguments = args

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container, buildingFragment) // 수정된 부분: 올바른 인스턴스 사용
        transaction.addToBackStack(null)
        transaction.commit()
    }


}