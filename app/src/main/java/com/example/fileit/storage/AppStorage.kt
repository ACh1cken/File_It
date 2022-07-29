package com.example.fileit.storage
//
//import android.app.Activity
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ProgressBar
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.fragment.app.Fragment
//import com.example.fileit.R
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.*
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageReference
//
////prototype Code Dump
//class AppStorage : Fragment() {
//    private lateinit var storage: FirebaseStorage
//    private lateinit var userUid: String
//    private lateinit var userEmail: String
//    private lateinit var storageChildRef : StorageReference
//    private var fileUri : Uri? = null
//    private lateinit var uploadButton : Button
//    private var progressBar : ProgressBar? = null
//    private lateinit var progressText : TextView
//    private lateinit var childLabel : String
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        //val view =  super.onCreateView(inflater, container, savedInstanceState)
//        val view = inflater.inflate(R.layout.fragment_appstorage, container, false)
//
//        uploadButton = view.findViewById(R.id.uploadButton)
//        progressBar = view.findViewById(R.id.uploadProgressBar)
//        progressText = view.findViewById(R.id.progressText)
//
//
//        storage = FirebaseStorage.getInstance()
//        userUid = FirebaseAuth.getInstance().uid.toString()
//        userEmail = FirebaseAuth.getInstance().currentUser!!.email.toString()
//
//        val storageRef = storage.reference
//
//
//        childLabel = "receipt"
//
//
//        storageChildRef = storageRef.child("/users/$childLabel/$userUid")
//
//
//
//
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        uploadButton.setOnClickListener{
//            chooseFile()
//        }
//        super.onViewCreated(view, savedInstanceState)
//    }
//
//    private val getResult =
//        registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()){
//        if(it.resultCode == Activity.RESULT_OK) {
//
//
//
//            if (it.data?.data != null) {
//                Log.e("FileUri", it.data!!.data.toString())
//                fileUri = it.data!!.data
//                var filename = it.data!!.data!!.lastPathSegment
//                upload(fileUri!!,filename!!)
//                progressBar?.visibility = View.VISIBLE
//            }else{
//                Log.e("FileUri","NULL")
//            }
//        }
//    }
//
//    fun upload(uri: Uri,filename : String){
////        var file = Uri.fromFile(File(uri))
//        val uploadRef = storageChildRef.child(filename)
//        var uploadtask = uploadRef.putFile(uri)
//
//        uploadtask.addOnFailureListener{
//            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
//        }.addOnSuccessListener {
//                Toast.makeText(context,"File uploaded successfully", Toast.LENGTH_LONG).show()
//            uploadRef.downloadUrl.addOnSuccessListener{
//                progressBar?.visibility = View.GONE
//                Log.e("DownloadUrl",it.toString())
//                updateDB(it,"2002","receipt","optional")
//            }
//
//
//            }
//
//            .addOnProgressListener {
//                var progress: Long =Math.round (((100.0 * it.bytesTransferred) / it.totalByteCount))
//                Log.e("Progress","Current progress $progress Size: ${it.totalByteCount}")
//                progressBar?.setProgress(progress.toInt())
//
//                progressText.setText("$progress % transferred")
//            }
//
//    }
//
//    fun updateDB(url: Uri, year: String, docType:String, additional_name: String?){
//        val db = Firebase.firestore
//        val userRef = db.collection("users").document(userUid)
//        var dnum : Int
//        val data1 = Users(
//            email = userEmail,
//            uid = userUid
//        )
//        userRef.get().addOnCompleteListener {
//            if (!it.result.exists()) {
//                userRef.set(data1)
//
//                userRef.update(
//                    "d_num",
//                    FieldValue.increment(1)
//                )//add new document, increase doccount
//                    .addOnCompleteListener {
//
//                        userRef.get().addOnCompleteListener {
//
//                            var docSnapshot: DocumentSnapshot = it.result
//                            Log.e("Snapshot", docSnapshot.toString())
//                            dnum = docSnapshot.get("d_num").toString().toInt()
//                            Log.e("d_Num", "$dnum")
//
//                            userRef.collection("documents").document("doc_$dnum").set(
//                                Documents(
//                                    year = year,
//                                    additional_name = additional_name,
//                                    docType = docType,
//                                    downloadURL = url.toString(),
//                                    userUid = userUid,
//                                    timestamp = FieldValue.serverTimestamp()
//                                )
//                            )
//                        }
//                    }
//            } else {
//
//                userRef.update(
//                    "d_num",
//                    FieldValue.increment(1)
//                )//add new document, increase doccount
//                    .addOnCompleteListener {
//
//                        userRef.get().addOnCompleteListener {
//
//                            var docSnapshot: DocumentSnapshot = it.result
//                            Log.e("Snapshot", docSnapshot.toString())
//                            dnum = docSnapshot.get("d_num").toString().toInt()
//                            Log.e("d_Num", "$dnum")
//
//                            userRef.collection("documents").document("doc_$dnum").set(
//                                Documents(
//                                    year = year,
//                                    additional_name = additional_name,
//                                    docType = docType,
//                                    downloadURL = url.toString(),
//                                    userUid = userUid,
//                                    timestamp = FieldValue.serverTimestamp()
//                                )
//                            )
//                        }
//                    }
//            }
//
//        }
//            .addOnFailureListener{
//                it.message?.let { it1 -> Log.e("GET", it1) }
//            }
//    }
//
//    fun chooseFile(){
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "*/*"
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        getResult.launch(intent)
//    }
//
//
//    data class Users(
//        var email : String,
//        var uid : String,
//        var d_num: Int = 0
//    )
//
//
//    data class Documents(
//        var year: String,
//        var additional_name: String? = null,
//        var docType: String,
//        var userUid: String,
//        var downloadURL: String,
//        var timestamp: FieldValue?
//    )
//}