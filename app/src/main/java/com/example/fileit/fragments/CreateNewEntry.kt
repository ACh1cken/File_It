package com.example.fileit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fileit.R
import com.example.fileit.storage.DocumentAdapter
import com.example.fileit.storage.DocumentModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.fragment_create_new_entry.*
import java.util.*


class CreateNewEntry : Fragment(){

    private var currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private var bundle  = Bundle()


    //startofFirebase
    private val db = FirebaseFirestore.getInstance()
    val userUid = FirebaseAuth.getInstance().uid
    private val docRef = db.collection("users").document(userUid!!).collection("documents")
    var documentAdapter : DocumentAdapter? =null
    var queryDirection = Query.Direction.ASCENDING
    private lateinit var recyclerView: RecyclerView
    private var Ascending = true

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

                bundle.putString("Year",p0!!.getItemAtPosition(p2).toString())
                Log.e("Spinner",bundle.get("Year").toString())

                documentAdapter?.stopListening()
                setupRecyclerViewSpecific(bundle.get("Year").toString())

                documentAdapter!!.startListening()
                recyclerView.adapter!!.notifyDataSetChanged()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.e("Spinner","Nothing Selected")
                bundle.putString("Year",null)
            }

        }
        recyclerView = view.findViewById(R.id.new_entry_recyclerview)

        //used to prevent error on onStart and onDestroy
        setupRecyclerViewDefault()

        //Start of ButtonListener
        view.findViewById<FloatingActionButton>(R.id.createNewEntryFloatingButton)
            .setOnClickListener{
                fragmentContainerView2.findNavController().navigate(R.id.newEntryDetailsFragment,null,
                    navOptions {
                        anim {
                            enter = android.R.anim.slide_in_left
                            exit = android.R.anim.slide_out_right
                        }
                    })
                //requireActivity().toolbar!!.visibility = View.GONE

            }


        view.findViewById<ImageButton>(R.id.EntrySortButton)
            .setOnClickListener{
            Ascending = if(Ascending) {
                EntrySortButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                queryDirection = Query.Direction.DESCENDING
                //
                documentAdapter?.stopListening()
                setupRecyclerViewSpecific(bundle.get("Year").toString())

                documentAdapter!!.startListening()
                recyclerView.adapter!!.notifyDataSetChanged()
                //
                false
            }else{
                EntrySortButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                queryDirection = Query.Direction.ASCENDING
                //
                documentAdapter?.stopListening()
                setupRecyclerViewSpecific(bundle.get("Year").toString())

                documentAdapter!!.startListening()
                recyclerView.adapter!!.notifyDataSetChanged()
                //
                true
            }
        }


        //Endof Buttono Listener

        return view
    }

    private fun setupRecyclerViewDefault() {
        val query: Query = docRef
            .orderBy("timestamp",queryDirection)
            .limit(50)

        val firestoreRecycler : FirestoreRecyclerOptions<DocumentModel> = FirestoreRecyclerOptions.Builder<DocumentModel>()
            .setQuery(query,DocumentModel::class.java)
            .build()

        documentAdapter = DocumentAdapter(firestoreRecycler,context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = documentAdapter

    }

    private fun setupRecyclerViewSpecific(year: String){

        val query : Query = if (year != "All"){
            docRef
                .whereEqualTo("year",year)
                .orderBy("timestamp",queryDirection)
                .limit(50)
        }else {
            docRef
                .orderBy("timestamp",queryDirection)
                .limit(50)
        }

        val firestoreRecycler : FirestoreRecyclerOptions<DocumentModel> = FirestoreRecyclerOptions.Builder<DocumentModel>()
            .setQuery(query,DocumentModel::class.java)
            .build()
        documentAdapter = DocumentAdapter(firestoreRecycler, context)
        recyclerView.adapter = documentAdapter
    }

    override fun onStart() {
        super.onStart()
        documentAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        documentAdapter!!.stopListening()
    }


    fun populateArray(year: Int): Array<String>{
       var array = Array(30) { i -> (year +2 - i).toString() }
        array[0] = "All"
        return array
    }


}