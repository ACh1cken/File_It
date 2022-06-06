package com.example.fileit.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fileit.R
import com.example.fileit.webcrawler.webcrawler
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class AnnouncementFragment : Fragment() , AnnouncementRecyclerAdapter.onClickListener{
    private lateinit var job : CompletableJob
//    val supervisor = SupervisorJob()
//    private lateinit var datalist : List<ExtractedData>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val model : webcrawler by viewModels()

        val recyclerview = view.findViewById<RecyclerView>(R.id.announcementRecyclerView)

        val announcementadapter = AnnouncementRecyclerAdapter(this)
        recyclerview.adapter = announcementadapter
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.setHasFixedSize(false)

        model.extractedData.observe(viewLifecycleOwner) { list ->
            announcementadapter.setData(list)
            announcementadapter.notifyDataSetChanged()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_announcement, container, false)




            //initJob(model)


//            try {
//
//                CoroutineScope(IO+job).launch {
//                   model.updateData()
//                }
//            }catch (e1: Throwable){
//                showToast("TimeoutError")
//                }



            return view
    }

    private fun initJob(viewModel: webcrawler) {
        job = Job()
        startJob(job,viewModel)

        job.invokeOnCompletion {
            it?.message.let{
                var msg = it
                if (msg.isNullOrBlank()){
                    msg = "Unknown Error"
                }
                println("$job was cancelled. Reason $msg")
                showToast(msg)
            }
        }
    }

    fun showToast(text: String){
        GlobalScope.launch(Main) {
            Toast.makeText( context,text,Toast.LENGTH_SHORT).show()
        }
    }

    fun startJob(job: Job,model: webcrawler){
        CoroutineScope(IO+job).launch{
            withTimeout(14000L){
                model.updateData()

            }
        }
    }

    override fun onItemClick(string: String?) {
        //showToast("Test"+ position)
        Log.e("Click", " $string")
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(string)))
    }



    

}