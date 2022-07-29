package com.example.fileit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.activityViewModels
import com.example.fileit.R
import com.example.fileit.storage.ExpandableAdapter
import com.example.fileit.storage.FirestoreViewModel


class ViewExistingFragment : Fragment() {
//TODO
    //search bar
    //delete  /entry details
    //select all
    //
private val viewModel : FirestoreViewModel by activityViewModels()
    //Firebase Related boilerplate code
//    private val db = FirebaseFirestore.getInstance()
//    val userUid = FirebaseAuth.getInstance().uid
//    private val docRef = db.collection("users").document(userUid!!).collection("documents")
//    private lateinit var userEmail: String
//    private lateinit var storageChildRef : StorageReference
    //End of Firebase Related

//    private lateinit var querySnapshot: QuerySnapshot
//    var query : Task<QuerySnapshot> = docRef.get()
//        .addOnSuccessListener {
//            querySnapshot = it
//        }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        userEmail = FirebaseAuth.getInstance().currentUser!!.email.toString()
        val view = inflater.inflate(R.layout.fragment_view_existing, container, false)




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