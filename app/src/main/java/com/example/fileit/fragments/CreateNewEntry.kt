package com.example.fileit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fileit.R
import com.example.fileit.storage.DocumentAdapter
import com.example.fileit.storage.DocumentModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_create_new_entry.*
import org.apache.commons.lang3.ObjectUtils
import java.util.*


class CreateNewEntry : Fragment(){

    private var currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private var bundle  = Bundle()


    //startofFirebase
    private val db = FirebaseFirestore.getInstance()
    val userUid = FirebaseAuth.getInstance().uid
    private val docRef = db.collection("users").document(userUid!!).collection("documents")
    var documentAdapter : DocumentAdapter? =null
    //endOfFirebase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  view =  inflater.inflate(R.layout.fragment_create_new_entry, container, false)

        val spinner = view.findViewById<Spinner>(R.id.createNewEntrySpinner)
//        val years = resources.getStringArray(R.array.spinner_placeholder)
//        val adapter = ArrayAdapter.createFromResource(requireActivity().applicationContext,R.array.spinner_placeholder,android.R.layout.simple_spinner_dropdown_item)
        val arraylist = populateArray(currentYear)

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_dropdown_item, arraylist
        )
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
//                if (parent != null) {
//                    Log.e("Spinner",parent.getItemAtPosition(pos).toString())
//                }else Log.e("Spinner","Null parent")
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//
//            }
//        }

        bundle.putString("Year",null)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.e("Spinner",p0!!.getItemAtPosition(p2).toString())
                bundle.putString("Year",p0!!.getItemAtPosition(p2).toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.e("Spinner","Nothing Selected")
                bundle.putString("Year",null)
            }

        }

        setupRecyclerView()

        return view
    }

    private fun setupRecyclerView() {
        val query: Query = docRef
        val firestoreRecycler : FirestoreRecyclerOptions<DocumentModel> = FirestoreRecyclerOptions.Builder<DocumentModel>()
            .setQuery(query,DocumentModel::class.java)
            .build()

         documentAdapter = DocumentAdapter(firestoreRecycler)
        new_entry_recyclerview.layoutManager = LinearLayoutManager(this.context)
        new_entry_recyclerview.adapter = documentAdapter
    }

    override fun onStart() {
        super.onStart()
        documentAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        documentAdapter!!.stopListening()
    }

    fun populateArray(year: Int): Array<String>{
       var array = Array(20) { i -> (year +5 - i).toString() }
        return array
    }


}