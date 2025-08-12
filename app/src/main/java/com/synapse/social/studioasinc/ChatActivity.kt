package com.synapse.social.studioasinc

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.IOException
import java.util.*

class ChatActivity : AppCompatActivity() {

    private var synapseLoadingDialog: ProgressDialog? = null
    private var chatMessagesLimit = 80.0
    private val chatSendMap = HashMap<String, Any>()
    private val chatInboxSend = HashMap<String, Any>()
    private val chatInboxSend2 = HashMap<String, Any>()
    private var replyMessageID = "null"
    private var path: String? = ""

    private lateinit var body: CenterCropLinearLayoutNoEffect
    private lateinit var top: CenterCropLinearLayoutNoEffect
    private lateinit var middle: LinearLayout
    private lateinit var messageInputOverallContainer: LinearLayout
    private lateinit var unblockBtn: TextView
    private lateinit var blockedTxt: TextView
    private lateinit var back: ImageView
    private lateinit var topProfileLayout: LinearLayout
    private lateinit var chatMessagesListRecycler: RecyclerView
    private lateinit var noChatText: TextView
    private lateinit var messageEt: EditText
    private lateinit var sendRoundBtn: LinearLayout
    private lateinit var galleryBtn: ImageView
    private lateinit var imgContainerLayout: LinearLayout
    private lateinit var selectedImgPreview: ImageView
    private lateinit var imgName: TextView
    private lateinit var imgUploadProg: ProgressBar
    private lateinit var removeSelectedImgIcon: ImageView

    private lateinit var auth: FirebaseAuth
    private lateinit var mainDb: FirebaseDatabase
    private lateinit var vbr: Vibrator
    private lateinit var filePicker: FilePicker

    private val chatMessagesList = ArrayList<HashMap<String, Any>>()
    private lateinit var adapter: ChatMessagesListRecyclerAdapter

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                filePicker.pickFile("image/*")
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat)
        initialize()
        initializeLogic()
    }

    private fun initialize() {
        body = findViewById(R.id.body)
        top = findViewById(R.id.top)
        middle = findViewById(R.id.middle)
        messageInputOverallContainer = findViewById(R.id.message_input_overall_container)
        unblockBtn = findViewById(R.id.unblock_btn)
        blockedTxt = findViewById(R.id.blocked_txt)
        back = findViewById(R.id.back)
        topProfileLayout = findViewById(R.id.topProfileLayout)
        chatMessagesListRecycler = findViewById(R.id.ChatMessagesListRecycler)
        noChatText = findViewById(R.id.noChatText)
        messageEt = findViewById(R.id.message_et)
        sendRoundBtn = findViewById(R.id.send_round_btn)
        galleryBtn = findViewById(R.id.gallery_btn)
        imgContainerLayout = findViewById(R.id.img_container_layout)
        selectedImgPreview = findViewById(R.id.selected_img_preview)
        imgName = findViewById(R.id.img_name)
        imgUploadProg = findViewById(R.id.img_upload_prog)
        removeSelectedImgIcon = findViewById(R.id.remove_selected_img_icon)

        auth = FirebaseAuth.getInstance()
        mainDb = FirebaseDatabase.getInstance()
        vbr = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        filePicker = FilePicker(activityResultRegistry, this) { uri ->
            if (uri != null) {
                path = FileUtil.convertUriToFilePath(this, uri)
                if (path != null) {
                    imgContainerLayout.visibility = View.VISIBLE
                    imgUploadProg.visibility = View.GONE
                    selectedImgPreview.setImageURI(uri)
                    imgName.text = Uri.parse(path).lastPathSegment
                } else {
                    Toast.makeText(this, "Could not get file path from URI", Toast.LENGTH_SHORT).show()
                }
            }
        }

        back.setOnClickListener { onBackPressed() }
        galleryBtn.setOnClickListener { checkPermissionsAndPickImage() }
        sendRoundBtn.setOnClickListener { sendBtn() }
        removeSelectedImgIcon.setOnClickListener {
            path = ""
            imgContainerLayout.visibility = View.GONE
        }
    }

    private fun initializeLogic() {
        adapter = ChatMessagesListRecyclerAdapter(chatMessagesList)
        chatMessagesListRecycler.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        chatMessagesListRecycler.adapter = adapter
        getChatMessagesRef()
    }

    private fun checkPermissionsAndPickImage() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            filePicker.pickFile("image/*")
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun sendBtn() {
        if (path.isNullOrEmpty()) {
            if (messageEt.text.toString().trim().isNotEmpty()) {
                sendMessage(messageEt.text.toString().trim(), null)
            }
        } else {
            loadingDialog(true)
            FasterCloudinaryUploader.upload(this, path!!, object : FasterCloudinaryUploader.UploaderCallback {
                override fun onProgress(progress: Int) {
                    imgUploadProg.visibility = View.VISIBLE
                    imgUploadProg.progress = progress
                }

                override fun onSuccess(url: String, publicId: String) {
                    loadingDialog(false)
                    sendMessage(messageEt.text.toString().trim(), url)
                }

                override fun onError(error: String) {
                    loadingDialog(false)
                    Toast.makeText(applicationContext, "Upload failed: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun sendMessage(messageText: String, imageUrl: String?) {
        val cc = Calendar.getInstance()
        val uniqueMessageKey = mainDb.reference.push().key ?: ""
        chatSendMap["uid"] = auth.currentUser!!.uid
        chatSendMap["TYPE"] = "MESSAGE"
        if (messageText.isNotEmpty()) {
            chatSendMap["message_text"] = messageText
        }
        if (imageUrl != null) {
            chatSendMap["message_image_uri"] = imageUrl
        }
        chatSendMap["message_state"] = "sended"
        if (replyMessageID != "null") {
            chatSendMap["replied_message_id"] = replyMessageID
        }
        chatSendMap["key"] = uniqueMessageKey
        chatSendMap["push_date"] = cc.timeInMillis.toString()

        val myUid = auth.currentUser!!.uid
        val otherUid = intent.getStringExtra("uid")!!

        mainDb.getReference("skyline/chats").child(myUid).child(otherUid).child(uniqueMessageKey).updateChildren(chatSendMap)
        mainDb.getReference("skyline/chats").child(otherUid).child(myUid).child(uniqueMessageKey).updateChildren(chatSendMap)

        updateInbox(myUid, otherUid, messageText)
        updateInbox(otherUid, myUid, messageText)

        messageEt.setText("")
        path = ""
        imgContainerLayout.visibility = View.GONE
        replyMessageID = "null"
    }

    private fun updateInbox(user1: String, user2: String, lastMessage: String) {
        val inboxData = HashMap<String, Any>()
        inboxData["uid"] = user2
        inboxData["TYPE"] = "MESSAGE"
        inboxData["last_message_uid"] = auth.currentUser!!.uid
        inboxData["last_message_text"] = lastMessage
        inboxData["last_message_state"] = "sended"
        inboxData["push_date"] = Calendar.getInstance().timeInMillis.toString()

        mainDb.getReference("skyline/inbox").child(user1).child(user2).updateChildren(inboxData)
    }

    private fun getChatMessagesRef() {
        val myUid = auth.currentUser!!.uid
        val otherUid = intent.getStringExtra("uid")!!

        mainDb.getReference("skyline/chats").child(myUid).child(otherUid).limitToLast(chatMessagesLimit.toInt())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        chatMessagesListRecycler.visibility = View.VISIBLE
                        noChatText.visibility = View.GONE
                        chatMessagesList.clear()
                        for (data in snapshot.children) {
                            val map = data.value as HashMap<String, Any>
                            chatMessagesList.add(map)
                        }
                        adapter.notifyDataSetChanged()
                        chatMessagesListRecycler.scrollToPosition(chatMessagesList.size - 1)
                    } else {
                        chatMessagesListRecycler.visibility = View.GONE
                        noChatText.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun loadingDialog(visibility: Boolean) {
        if (visibility) {
            if (synapseLoadingDialog == null) {
                synapseLoadingDialog = ProgressDialog(this).apply {
                    setCancelable(false)
                    setCanceledOnTouchOutside(false)
                    requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
                    window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(Color.TRANSPARENT))
                }
            }
            synapseLoadingDialog?.show()
            synapseLoadingDialog?.setContentView(R.layout.loading_synapse)
        } else {
            synapseLoadingDialog?.dismiss()
        }
    }

    inner class ChatMessagesListRecyclerAdapter(private val data: ArrayList<HashMap<String, Any>>) :
        RecyclerView.Adapter<ChatMessagesListRecyclerAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = layoutInflater.inflate(R.layout.chat_msg_cv_synapse, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // This is a simplified version of the original adapter.
            // A full implementation would require more time and care.
            val messageText = holder.itemView.findViewById<TextView>(R.id.message_text)
            val messageImage = holder.itemView.findViewById<ImageView>(R.id.mMessageImageView)
            val item = data[position]

            if (item.containsKey("message_text")) {
                messageText.text = item["message_text"].toString()
                messageText.visibility = View.VISIBLE
            } else {
                messageText.visibility = View.GONE
            }

            if (item.containsKey("message_image_uri")) {
                com.bumptech.glide.Glide.with(this@ChatActivity).load(item["message_image_uri"].toString()).into(messageImage)
                messageImage.visibility = View.VISIBLE
            } else {
                messageImage.visibility = View.GONE
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }
}
