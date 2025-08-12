package com.synapse.social.studioasinc

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class ProfileCoverPhotoHistoryActivity : AppCompatActivity() {

    private var synapseLoadingDialog: ProgressDialog? = null
    private var currentAvatarUri: String? = ""
    private val mAddProfilePhotoMap = HashMap<String, Any>()
    private val mSendMap = HashMap<String, Any>()
    private val profileHistoryList = ArrayList<HashMap<String, Any>>()

    private lateinit var main: LinearLayout
    private lateinit var top: LinearLayout
    private lateinit var body: LinearLayout
    private lateinit var back: ImageView
    private lateinit var title: TextView
    private lateinit var mSwipeLayout: SwipeRefreshLayout
    private lateinit var mLoadingBody: LinearLayout
    private lateinit var mLoadedBody: LinearLayout
    private lateinit var isDataExistsLayout: LinearLayout
    private lateinit var isDataNotExistsLayout: LinearLayout
    private lateinit var profilePhotosHistoryList: RecyclerView
    private lateinit var isDataNotExistsLayoutTitle: TextView
    private lateinit var isDataNotExistsLayoutSubTitle: TextView
    private lateinit var mLoadingBar: ProgressBar
    private lateinit var fab: FloatingActionButton

    private lateinit var auth: FirebaseAuth
    private lateinit var maindb: FirebaseDatabase
    private lateinit var filePicker: FilePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_cover_photo_history)
        initialize()
        initializeLogic()
    }

    private fun initialize() {
        main = findViewById(R.id.main)
        top = findViewById(R.id.top)
        body = findViewById(R.id.body)
        back = findViewById(R.id.back)
        title = findViewById(R.id.title)
        mSwipeLayout = findViewById(R.id.mSwipeLayout)
        mLoadingBody = findViewById(R.id.mLoadingBody)
        mLoadedBody = findViewById(R.id.mLoadedBody)
        isDataExistsLayout = findViewById(R.id.isDataExistsLayout)
        isDataNotExistsLayout = findViewById(R.id.isDataNotExistsLayout)
        profilePhotosHistoryList = findViewById(R.id.ProfilePhotosHistoryList)
        isDataNotExistsLayoutTitle = findViewById(R.id.isDataNotExistsLayoutTitle)
        isDataNotExistsLayoutSubTitle = findViewById(R.id.isDataNotExistsLayoutSubTitle)
        mLoadingBar = findViewById(R.id.mLoadingBar)
        fab = findViewById(R.id._fab)

        auth = FirebaseAuth.getInstance()
        maindb = FirebaseDatabase.getInstance()

        filePicker = FilePicker(activityResultRegistry, this) { uri ->
            if (uri != null) {
                val filePath = FileUtil.convertUriToFilePath(this, uri)
                if (filePath != null) {
                    uploadCoverPhoto(filePath)
                } else {
                    Toast.makeText(this, "Could not get file path from URI", Toast.LENGTH_SHORT).show()
                }
            }
        }

        back.setOnClickListener { onBackPressed() }
        mSwipeLayout.setOnRefreshListener { getReference() }
        fab.setOnClickListener { addProfilePhotoUrlDialog() }
    }

    private fun initializeLogic() {
        profilePhotosHistoryList.layoutManager = LinearLayoutManager(this)
        profilePhotosHistoryList.adapter = ProfilePhotosHistoryListAdapter(profileHistoryList)
        getReference()
    }

    private fun getReference() {
        isDataExistsLayout.visibility = View.GONE
        isDataNotExistsLayout.visibility = View.GONE
        mSwipeLayout.visibility = View.GONE
        mLoadingBody.visibility = View.VISIBLE

        val getUserReference = maindb.getReference("skyline/users").child(auth.currentUser!!.uid)
        getUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentAvatarUri = dataSnapshot.child("profile_cover_image").getValue(String::class.java)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val getProfileHistoryRef = maindb.getReference("skyline/cover-image-history").child(auth.currentUser!!.uid)
        getProfileHistoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    isDataExistsLayout.visibility = View.VISIBLE
                    isDataNotExistsLayout.visibility = View.GONE
                    mSwipeLayout.visibility = View.VISIBLE
                    mLoadingBody.visibility = View.GONE
                    profileHistoryList.clear()
                    for (data in dataSnapshot.children) {
                        val map = data.value as HashMap<String, Any>
                        profileHistoryList.add(map)
                    }
                    profileHistoryList.sortByDescending { it["upload_date"].toString().toLong() }
                    profilePhotosHistoryList.adapter?.notifyDataSetChanged()
                } else {
                    isDataExistsLayout.visibility = View.GONE
                    isDataNotExistsLayout.visibility = View.VISIBLE
                    mSwipeLayout.visibility = View.VISIBLE
                    mLoadingBody.visibility = View.GONE
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        mSwipeLayout.isRefreshing = false
    }

    private fun uploadCoverPhoto(filePath: String) {
        loadingDialog(true)
        FasterCloudinaryUploader.upload(this, filePath, object: FasterCloudinaryUploader.UploaderCallback {
            override fun onProgress(progress: Int) {}

            override fun onSuccess(url: String, publicId: String) {
                val profileHistoryKey = maindb.reference.push().key ?: ""
                mAddProfilePhotoMap["key"] = profileHistoryKey
                mAddProfilePhotoMap["image_url"] = url
                mAddProfilePhotoMap["upload_date"] = Calendar.getInstance().timeInMillis.toString()
                mAddProfilePhotoMap["type"] = "cloudinary"
                mAddProfilePhotoMap["public_id"] = publicId
                maindb.getReference("skyline/cover-image-history/${auth.currentUser!!.uid}/$profileHistoryKey").updateChildren(mAddProfilePhotoMap)
                Toast.makeText(applicationContext, "Cover Image Added", Toast.LENGTH_SHORT).show()
                getReference()
                loadingDialog(false)
            }

            override fun onError(error: String) {
                loadingDialog(false)
                Toast.makeText(applicationContext, "Upload failed: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addProfilePhotoUrlDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.profile_cover_image_history_add_dialog, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val userAvatarUrlInput = dialogView.findViewById<EditText>(R.id.user_avatar_url_input)
        val userAvatarImage = dialogView.findViewById<ImageView>(R.id.user_avatar_image)
        val addButton = dialogView.findViewById<TextView>(R.id.add_button)
        val cancelButton = dialogView.findViewById<TextView>(R.id.cancel_button)
        val pickButton = dialogView.findViewById<Button>(R.id.pick_button) // Assuming you add this button to the layout

        pickButton.setOnClickListener {
            filePicker.pickFile("image/*")
            dialog.dismiss()
        }

        addButton.setOnClickListener {
            val url = userAvatarUrlInput.text.toString().trim()
            if (url.isNotEmpty() && checkValidUrl(url)) {
                val profileHistoryKey = maindb.reference.push().key ?: ""
                mAddProfilePhotoMap["key"] = profileHistoryKey
                mAddProfilePhotoMap["image_url"] = url
                mAddProfilePhotoMap["upload_date"] = Calendar.getInstance().timeInMillis.toString()
                mAddProfilePhotoMap["type"] = "url"
                maindb.getReference("skyline/cover-image-history/${auth.currentUser!!.uid}/$profileHistoryKey").updateChildren(mAddProfilePhotoMap)
                Toast.makeText(applicationContext, "Cover Image Added", Toast.LENGTH_SHORT).show()
                getReference()
                dialog.dismiss()
            }
        }

        cancelButton.setOnClickListener { dialog.dismiss() }
        dialog.setCancelable(true)
        dialog.show()
    }

    private fun checkValidUrl(url: String): Boolean {
        return try {
            URL(url)
            true
        } catch (e: MalformedURLException) {
            false
        }
    }

    private fun deleteProfileImage(key: String, type: String, uri: String, publicId: String?) {
        if (uri == currentAvatarUri) {
            mSendMap["profile_cover_image"] = "null"
            mSendMap["cover_image_history_type"] = "local"
            maindb.getReference("skyline/users/${auth.currentUser!!.uid}").updateChildren(mSendMap)
            currentAvatarUri = "null"
            mSendMap.clear()
        }
        if (type == "cloudinary" && publicId != null) {
            FasterCloudinaryUploader.delete(publicId, object: FasterCloudinaryUploader.DeleteCallback {
                override fun onSuccess() {
                    maindb.getReference("skyline/cover-image-history/${auth.currentUser!!.uid}/$key").removeValue()
                    getReference()
                }
                override fun onError(error: String) {
                    Toast.makeText(applicationContext, "Delete failed: $error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            maindb.getReference("skyline/cover-image-history/${auth.currentUser!!.uid}/$key").removeValue()
            getReference()
        }
    }

    private fun loadingDialog(visibility: Boolean) {
        if (visibility) {
            if (synapseLoadingDialog == null) {
                synapseLoadingDialog = ProgressDialog(this).apply {
                    setCancelable(false)
                    setCanceledOnTouchOutside(false)
                    requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
            }
            synapseLoadingDialog?.show()
            synapseLoadingDialog?.setContentView(R.layout.loading_synapse)
        } else {
            synapseLoadingDialog?.dismiss()
        }
    }

    inner class ProfilePhotosHistoryListAdapter(private val data: ArrayList<HashMap<String, Any>>) :
        RecyclerView.Adapter<ProfilePhotosHistoryListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.profile_cover_image_history_list, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = data[position]
            val profile = holder.itemView.findViewById<ImageView>(R.id.profile)
            val checked = holder.itemView.findViewById<LinearLayout>(R.id.checked)

            Glide.with(this@ProfileCoverPhotoHistoryActivity).load(Uri.parse(item["image_url"].toString())).into(profile)
            checked.visibility = if (item["image_url"].toString() == currentAvatarUri) View.VISIBLE else View.GONE

            holder.itemView.setOnClickListener {
                val newUri = item["image_url"].toString()
                mSendMap["profile_cover_image"] = if (newUri == currentAvatarUri) "null" else newUri
                mSendMap["cover_image_history_type"] = if (newUri == currentAvatarUri) "local" else item["type"].toString()
                maindb.getReference("skyline/users/${auth.currentUser!!.uid}").updateChildren(mSendMap)
                currentAvatarUri = if (newUri == currentAvatarUri) "null" else newUri
                notifyDataSetChanged()
            }

            holder.itemView.setOnLongClickListener {
                deleteProfileImage(item["key"].toString(), item["type"].toString(), item["image_url"].toString(), item["public_id"] as? String)
                true
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }
}
