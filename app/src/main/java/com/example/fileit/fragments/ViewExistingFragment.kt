package com.example.fileit.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageButton
import androidx.fragment.app.activityViewModels
import com.example.fileit.R
import com.example.fileit.storage.ExpandableAdapter
import com.example.fileit.storage.FirestoreViewModel


class ViewExistingFragment : Fragment() {

private val viewModel : FirestoreViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        userEmail = FirebaseAuth.getInstance().currentUser!!.email.toString()
        val view = inflater.inflate(R.layout.fragment_view_existing, container, false)
        val helpbutton = view.findViewById<ImageButton>(R.id.help_existing)
        helpbutton.setOnClickListener{
            AlertDialog.Builder(requireContext())
                .setTitle("Help center")
                .setMessage("Press on individual item to download them to your local device.")
                .setPositiveButton("Okay"){_,_ ->}
                .show()
        }


        var expandableListView = view.findViewById<ExpandableListView>(R.id.expandableListView)

        val expandableAdapter = ExpandableAdapter(requireContext(),view)
        expandableListView.setAdapter(expandableAdapter)


        viewModel.getListTitle().observe(viewLifecycleOwner){
            expandableAdapter.setTitleData(it)
            expandableAdapter.notifyDataSetChanged()
        }

        viewModel.getListDetail().observe(viewLifecycleOwner){
            expandableAdapter.setDetailData(it)
            expandableAdapter.notifyDataSetChanged()
        }


        return view
    }


    override fun onStop() {
//        viewModel.stopListener()
        super.onStop()
    }
}