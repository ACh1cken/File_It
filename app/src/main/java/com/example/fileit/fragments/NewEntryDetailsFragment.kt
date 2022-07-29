package com.example.fileit.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.fileit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.fragment_new_entry_details.*
import java.util.Calendar


class NewEntryDetailsFragment : Fragment() {
    private var currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private var fileTypeArr : Array<String> = arrayOf(
        "-Select File Type-",
        "Insurance Related",
        "Medical Statement/Receipt",
        "Electronics Related",
        "Education Related",
        "Sports Related",
        "Lifestyle Related")
    private var bundle = Bundle()
    private var progressBarProgress :Int = 0
    private var fileUploadProgressBar: ProgressBar? = null
    private lateinit var fileUploadProgressBarText : TextView
    private lateinit var submitButton : Button



    //Firebase Related boilerplate code
    private lateinit var storage: FirebaseStorage
    private lateinit var userUid: String
    private lateinit var userEmail: String
    private lateinit var storageChildRef : StorageReference
    private var fileUri : Uri? = null
    private var childLabel:String? =null
    //End of Firebase Related

    private lateinit var fileAmountEditText : EditText
    private lateinit var fileNameEditText : EditText
    private lateinit var fileAdditionalDescription : EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_entry_details, container, false)

        val filetypeSpinner = view.findViewById<Spinner>(R.id.new_entry_details_fileType_spinner)
        val yearSpinner = view.findViewById<Spinner>(R.id.new_entry_details_year_spinner)

//        val documentProgress = view.findViewById<ProgressBar>(R.id.new_entry_details_documentCompletion_progressBar)

        fileNameEditText = view.findViewById<EditText>(R.id.new_entry_details_fileName_editText)
        fileAdditionalDescription = view.findViewById<EditText>(R.id.new_entry_details_description_editText)

        fileAmountEditText = view.findViewById<EditText>(R.id.new_entry_details_amount_editText)

        val fileUploadButton = view.findViewById<ImageButton>(R.id.fileUploadButton)
        submitButton = view.findViewById<Button>(R.id.new_entry_details_submitButton)
        fileUploadProgressBarText = view.findViewById(R.id.new_entry_details_uploadProgress_textView)
        fileUploadProgressBar = view.findViewById(R.id.new_entry_details_upload_progressbar)



        requireActivity().toolbar!!.visibility = View.GONE

        //editText
        val Count = intArrayOf(0)

        //progressBar
        var hasSelectedFileType = false
        var hasSelectedYearSpinner =false

        //Firebase
        storage = FirebaseStorage.getInstance()
        userUid = FirebaseAuth.getInstance().uid.toString()
        userEmail = FirebaseAuth.getInstance().currentUser!!.email.toString()

        val storageRef = storage.reference


//        childLabel = "receipt"
//        storageChildRef = storageRef.child("/users/$childLabel/$userUid")

        childLabel = "documents"
        storageChildRef = storageRef.child("/users/$userUid/$childLabel")


        val yearAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_dropdown_item, populateArray(currentYear)
        )
        val fileTypeAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    requireActivity().applicationContext,
                    android.R.layout.simple_spinner_dropdown_item, fileTypeArr
        )

        filetypeSpinner.adapter = fileTypeAdapter
        yearSpinner.adapter = yearAdapter

        filetypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                if (p0!!.getItemAtPosition(p2).toString()!= "-Select File Type-") {
                    bundle.putString("fileType", p0!!.getItemAtPosition(p2).toString())
                    Log.e("FileTypeSpinner", bundle.get("fileType").toString())
                    if(!hasSelectedFileType) {
                        progressBarProgress += 25
                        updateProgress()
                        hasSelectedFileType = true
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.e("FileTypeSpinner","Nothing Selected")
                bundle.putString("fileType",null)
            }

        }
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                if (p0!!.getItemAtPosition(p2).toString()!= "-Select Year-") {
                    bundle.putString("fileYear", p0!!.getItemAtPosition(p2).toString())
                    Log.e("Spinner", bundle.get("fileYear").toString())
                    if (!hasSelectedYearSpinner) {
                        progressBarProgress += 25
                        updateProgress()
                        hasSelectedYearSpinner = true
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.e("Spinner","Nothing Selected")
                bundle.putString("fileYear",null)
            }

        }

        fileUploadButton.setOnClickListener(){
            chooseFile()
            progressBarProgress += 25
            updateProgress()
        }




        fileNameEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                if(Count[0] == 0){
                    Count[0]++
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {

                        if (fileNameEditText.text.isNotEmpty()){
                            progressBarProgress += 25
                            updateProgress()
                            bundle.putString("fileName",fileNameEditText.text.toString())
                            Log.e("FileName",bundle.get("fileName").toString())
                        }else{
                            if (progressBarProgress > 0){
                                progressBarProgress -= 25
                                updateProgress()
                            }
                        }
                        Count[0] = 0
                    }, 3000)
                }


            }
        })
        fileAdditionalDescription.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun afterTextChanged(p0: Editable?) {
                        if(Count[0] == 0){
                            Count[0]++
                            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                                if (fileAdditionalDescription.text.isNotEmpty()){
                                    bundle.putString("additional_description",fileAdditionalDescription.text.toString())

                                }else{
                                    bundle.putString("additional_description",null)
                                }
                                Log.e("EditText",bundle.get("additional_description").toString())

                                Count[0] = 0
                            }, 3000)
                        }


                    }
                })

        fileAmountEditText.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun afterTextChanged(p0: Editable?) {

                        if(Count[0] == 0){
                            Count[0]++
                            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                                if (fileAmountEditText.text.isNotEmpty()){
                                    var int1 = 0
                                    try{
                                        int1 = Integer.parseInt(fileAmountEditText.text.toString())
                                    }catch (nfe:NumberFormatException){
                                        Toast.makeText(context,"This is not a proper number: $nfe",Toast.LENGTH_SHORT).show()
                                    }
                                    bundle.putInt("amount",int1)
                                }else{
                                    bundle.putInt("amount",0)
                                }
                                Log.e("EditText",bundle.get("amount").toString())

                                Count[0] = 0
                            }, 3000)
                        }

                    }
                })


        return view

    }



    private fun chooseFile(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        getResult.launch(intent)
    }



    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK) {



                if (it.data?.data != null) {
                    Log.e("FileUri", it.data!!.data.toString())
                    fileUri = it.data!!.data
                    var filename = it.data!!.data!!.lastPathSegment

                    //UploadFile
                    upload(fileUri!!,bundle.get("fileName").toString())
//                    uploadProgressBar?.visibility = View.VISIBLE
                }else{
                    Log.e("FileUri","NULL")
                }
            }
        }


    private fun upload(uri: Uri, filename : String){
//        var file = Uri.fromFile(File(uri))
        val uploadRef = storageChildRef.child(filename)
        var uploadtask = uploadRef.putFile(uri)

        uploadtask.addOnFailureListener{
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }.addOnSuccessListener {
            Toast.makeText(context,"File uploaded successfully", Toast.LENGTH_LONG).show()
            uploadRef.downloadUrl.addOnSuccessListener{
//                uploadProgressBar?.visibility = View.GONE
                Log.e("DownloadUrl",it.toString())
                val downloadUrl = it.toString()

                submitButton.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.teal_200))

                submitButton.setOnClickListener {
                    if(progressBarProgress >= 100){
                        fragmentContainerView2.findNavController().navigate(R.id.announcementFragment, null,
                            navOptions {
                                anim {
                                    enter = android.R.animator.fade_in
                                    exit = android.R.animator.fade_out
                                }
                            })

//                upload(fileUri!!, bundle.get("fileName").toString())
                        //Submit details to db

                        //Amount
                        if (fileAmountEditText.text.isNotEmpty()){
                            var int1 = 0
                            try{
                                int1 = Integer.parseInt(fileAmountEditText.text.toString())
                            }catch (nfe:NumberFormatException){
                                Toast.makeText(context,"This is not a proper number: $nfe",Toast.LENGTH_SHORT).show()
                            }
                            bundle.putInt("amount",int1)
                        }else{
                            bundle.putInt("amount",0)
                        }

                        //filename
                        if (fileNameEditText.text.isNotEmpty()){
                            progressBarProgress += 25
                            updateProgress()
                            bundle.putString("fileName",fileNameEditText.text.toString())
                            Log.e("FileName",bundle.get("fileName").toString())
                        }else{
                            if (progressBarProgress > 0){
                                progressBarProgress -= 25
                                updateProgress()
                            }
                        }

                        //description
                        if (fileAdditionalDescription.text.isNotEmpty()){
                            bundle.putString("additional_description",fileAdditionalDescription.text.toString())

                        }else{
                            bundle.putString("additional_description",null)
                        }
                        Log.e("EditText",bundle.get("additional_description").toString())






                        updateDB(downloadUrl,bundle.getString("fileYear").toString(),
                            bundle.getString("fileType").toString(),
                            bundle.getString("additional_description").toString(),
                            bundle.getString("fileName").toString(),
                            bundle.getInt("amount")
                        )
                        requireActivity().toolbar!!.visibility = View.VISIBLE
                    }else{
                        Toast.makeText(context,"Please fill out all required fields.",Toast.LENGTH_SHORT).show()
                    }
                }


            }


        }

            .addOnProgressListener {
                var progress: Long =Math.round (((100.0 * it.bytesTransferred) / it.totalByteCount))
                Log.e("Progress","Current progress $progress Size: ${it.totalByteCount}")
                fileUploadProgressBar!!.progress = progress.toInt()

                fileUploadProgressBarText.setText("$progress%")
            }

    }

    private fun updateDB(url: String, year: String, docType:String, additional_name: String?, filename: String, fileAmount: Int){
        val db = Firebase.firestore
        val userRef = db.collection("users").document(userUid)
        var dnum : Int
        val data1 = Users(
            email = userEmail,
            uid = userUid
        )
        userRef.get().addOnCompleteListener {
            if (!it.result.exists()) {
                userRef.set(data1)

                userRef.update(
                    "d_num",
                    FieldValue.increment(1)
                )//add new document, increase doccount
                    .addOnCompleteListener {

                        userRef.get().addOnCompleteListener {

                            var docSnapshot: DocumentSnapshot = it.result
                            Log.e("Snapshot", docSnapshot.toString())
                            dnum = docSnapshot.get("d_num").toString().toInt()
                            Log.e("d_Num", "$dnum")

                            userRef.collection("documents").document().set(
                                Documents(
                                    year = year,
                                    additional_name = additional_name,
                                    docType = docType,
                                    downloadURL = url.toString(),
                                    userUid = userUid,
                                    timestamp = FieldValue.serverTimestamp(),
                                    filename = filename,
                                    fileAmount = fileAmount
                                )
                            )
                        }
                    }
            } else {

                userRef.update(
                    "d_num",
                    FieldValue.increment(1)
                )//add new document, increase doccount
                    .addOnCompleteListener {

                        userRef.get().addOnCompleteListener {

                            var docSnapshot: DocumentSnapshot = it.result
                            Log.e("Snapshot", docSnapshot.toString())
                            dnum = docSnapshot.get("d_num").toString().toInt()
                            Log.e("d_Num", "$dnum")

                            userRef.collection("documents").document().set(
                                Documents(
                                    year = year,
                                    additional_name = additional_name,
                                    docType = docType,
                                    downloadURL = url.toString(),
                                    userUid = userUid,
                                    timestamp = FieldValue.serverTimestamp(),
                                    filename = filename,
                                    fileAmount = fileAmount
                                )
                            )
                        }
                    }
            }



        }
            .addOnFailureListener{
                it.message?.let { it1 -> Log.e("GET", it1) }
            }
    }

    //Functions
    fun populateArray(year: Int): Array<String>{
        var array = Array(30) { i -> (year +2 - i).toString() }
        array[0] = "-Select Year-"
        return array
    }

    fun updateProgress(){

            new_entry_details_documentCompletion_progressBar.progress = progressBarProgress
            new_entry_details_documentCompletion_progress_textview.text = "$progressBarProgress%"

        if(new_entry_details_documentCompletion_progressBar.progress >= 100){
            new_entry_details_documentCompletion_progress_textview.visibility = View.GONE
            new_entry_details_documentCompletion_checkImage.visibility = View.VISIBLE
        }
    }


    data class Users(
        var email : String,
        var uid : String,
        var d_num: Int = 0
    )


    data class Documents(
        var year: String,
        var additional_name: String? = null,
        var docType: String,
        var userUid: String,
        var downloadURL: String,
        var timestamp: FieldValue?,
        var filename: String,
        var fileAmount: Int
    )
}