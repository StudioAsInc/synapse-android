package com.synapse.social.studioasinc

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class CreateImagePostActivity : AppCompatActivity() {

    private var synapseLoadingDialog: ProgressDialog? = null
    private var addFromUrlStr: String? = null
    private val imagesListMap = ArrayList<HashMap<String, Any>>()

    private lateinit var main: LinearLayout
    private lateinit var top: LinearLayout
    private lateinit var cropImageView: CropImageView
    private lateinit var urlImagePreview: LinearLayout
    private lateinit var body: LinearLayout
    private lateinit var back: ImageView
    private lateinit var title: TextView
    private lateinit var topSpc: LinearLayout
    private lateinit var continueButton: Button
    private lateinit var urlImagePreviewImage: ImageView
    private lateinit var imagesView: RecyclerView
    private lateinit var bottomButtons: LinearLayout
    private lateinit var selectGallery: Button
    private lateinit var fromUrl: Button

    private lateinit var filePicker: FilePicker

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                initializeLogic()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_image_post)
        initialize()
        checkPermissions()
    }

    private fun checkPermissions() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            initializeLogic()
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun initialize() {
        main = findViewById(R.id.main)
        top = findViewById(R.id.top)
        cropImageView = findViewById(R.id.cropImageView)
        urlImagePreview = findViewById(R.id.urlImagePreview)
        body = findViewById(R.id.body)
        back = findViewById(R.id.back)
        title = findViewById(R.id.title)
        topSpc = findViewById(R.id.topSpc)
        continueButton = findViewById(R.id.continueButton)
        urlImagePreviewImage = findViewById(R.id.urlImagePreviewImage)
        imagesView = findViewById(R.id.imagesView)
        bottomButtons = findViewById(R.id.bottomButtons)
        selectGallery = findViewById(R.id.selectGallery)
        fromUrl = findViewById(R.id.From_url)

        filePicker = FilePicker(activityResultRegistry, this) { uri ->
            if (uri != null) {
                val intent = Intent(this, CreateImagePostNextStepActivity::class.java)
                intent.putExtra("type", "local")
                intent.putExtra("path", uri.toString())
                startActivity(intent)
                finish()
            }
        }

        back.setOnClickListener { onBackPressed() }

        continueButton.setOnClickListener {
            if (addFromUrlStr == null) {
                try {
                    saveBitmapAsPng(cropImageView.croppedImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                val intent = Intent(this, CreateImagePostNextStepActivity::class.java)
                intent.putExtra("type", "url")
                intent.putExtra("path", addFromUrlStr)
                startActivity(intent)
                finish()
            }
        }

        selectGallery.setOnClickListener {
            filePicker.pickFile("image/*")
        }

        fromUrl.setOnClickListener {
            addFromUrlDialog()
        }
    }

    private fun initializeLogic() {
        val display = windowManager.defaultDisplay
        val screenHeight = display.height
        val desiredHeight = screenHeight / 2 - 24
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, desiredHeight)
        cropImageView.layoutParams = params
        urlImagePreview.layoutParams = params

        stateColor(Color.WHITE, Color.WHITE)
        viewGraphics(back, Color.WHITE, 0xFFE0E0E0.toInt(), 300f, 0f, Color.TRANSPARENT)

        imagesView.adapter = ImagesViewAdapter(imagesListMap)
        imagesView.layoutManager = GridLayoutManager(this, 4)

        getImageFiles()
        cropImageView.setAspectRatio(4, 3)
        cropImageView.isFixAspectRatio = false
    }

    private fun saveBitmapAsPng(bitmap: Bitmap) {
        loadingDialog(true)
        val cc = Calendar.getInstance()
        val getCacheDir = externalCacheDir
        val getCacheDirName = "cropped_images"
        val getCacheFolder = File(getCacheDir, getCacheDirName)
        getCacheFolder.mkdirs()
        val getImageFile = File(getCacheFolder, "${cc.timeInMillis}.png")
        val savedFilePath = getImageFile.absolutePath

        Thread {
            try {
                val outStream = FileOutputStream(getImageFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                outStream.flush()
                outStream.close()
                runOnUiThread {
                    loadingDialog(false)
                    val newIntent = Intent(this, CreateImagePostNextStepActivity::class.java)
                    newIntent.putExtra("type", "local")
                    newIntent.putExtra("path", savedFilePath)
                    startActivity(newIntent)
                    finish()
                }
            } catch (e: IOException) {
                runOnUiThread {
                    loadingDialog(false)
                    Toast.makeText(applicationContext, "Something went wrong.", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
        }.start()
    }

    private fun getImageFiles() {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val imagesCursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        imagesCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    if (imagePath.endsWith(".png") || imagePath.endsWith(".jpg") || imagePath.endsWith(".jpeg")) {
                        val mediaItem = HashMap<String, Any>()
                        mediaItem["type"] = "Image"
                        mediaItem["path"] = imagePath
                        imagesListMap.add(mediaItem)
                    }
                } while (cursor.moveToNext())
            }
        }

        imagesView.adapter?.notifyDataSetChanged()
        if (imagesListMap.isNotEmpty()) {
            loadCropImage(imagesListMap[0]["path"].toString(), false)
        }
    }

    private fun stateColor(statusColor: Int, navigationColor: Int) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = statusColor
        window.navigationBarColor = navigationColor
    }

    private fun viewGraphics(
        view: View,
        onFocus: Int,
        onRipple: Int,
        radius: Float,
        stroke: Float,
        strokeColor: Int
    ) {
        val gg = GradientDrawable()
        gg.setColor(onFocus)
        gg.cornerRadius = radius
        gg.setStroke(stroke.toInt(), strokeColor)
        val re = android.graphics.drawable.RippleDrawable(
            android.content.res.ColorStateList(arrayOf(intArrayOf()), intArrayOf(onRipple)),
            gg,
            null
        )
        view.background = re
    }

    private fun loadingDialog(visibility: Boolean) {
        if (visibility) {
            if (synapseLoadingDialog == null) {
                synapseLoadingDialog = ProgressDialog(this)
                synapseLoadingDialog?.setCancelable(false)
                synapseLoadingDialog?.setCanceledOnTouchOutside(false)
                synapseLoadingDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                synapseLoadingDialog?.window?.setBackgroundDrawable(
                    android.graphics.drawable.ColorDrawable(
                        Color.TRANSPARENT
                    )
                )
            }
            synapseLoadingDialog?.show()
            synapseLoadingDialog?.setContentView(R.layout.loading_synapse)
        } else {
            synapseLoadingDialog?.dismiss()
        }
    }

    private fun loadCropImage(path: String, isUrl: Boolean) {
        if (!isUrl) {
            addFromUrlStr = null
            val file = File(path)
            val uri = Uri.fromFile(file)
            cropImageView.setImageUriAsync(uri)
            urlImagePreview.visibility = View.GONE
            cropImageView.visibility = View.VISIBLE
        } else {
            addFromUrlStr = path
            Glide.with(applicationContext).load(Uri.parse(path)).into(urlImagePreviewImage)
            urlImagePreview.visibility = View.VISIBLE
            cropImageView.visibility = View.GONE
        }
    }

    private fun addFromUrlDialog() {
        val dialogs = MaterialAlertDialogBuilder(this)
        dialogs.setTitle("Add through url")
        val editTextDesign = LayoutInflater.from(this).inflate(R.layout.single_et, null)
        dialogs.setView(editTextDesign)

        val editText1 = editTextDesign.findViewById<EditText>(R.id.edittext1)
        editText1.isFocusableInTouchMode = true

        dialogs.setPositiveButton("Add") { _, _ ->
            val url = editText1.text.toString().trim()
            if (url.isNotEmpty() && checkValidUrl(url)) {
                addFromUrlStr = url
            }
        }
        dialogs.setNegativeButton("Cancel", null)
        val editTextDialog = dialogs.create()
        editTextDialog.setCancelable(true)
        editTextDialog.show()
    }

    private fun checkValidUrl(url: String): Boolean {
        return try {
            URL(url)
            true
        } catch (e: MalformedURLException) {
            false
        }
    }

    inner class ImagesViewAdapter(private val data: ArrayList<HashMap<String, Any>>) :
        RecyclerView.Adapter<ImagesViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = layoutInflater
            val v = inflater.inflate(R.layout.synapse_create_img_post_image_cv, null)
            val lp = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            v.layoutParams = lp
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val view = holder.itemView
            val relative = view.findViewById<RelativeLayout>(R.id.relative)
            val image = view.findViewById<ImageView>(R.id.image)
            val typeIcon = view.findViewById<ImageView>(R.id.typeIcon)
            val mediaDuration = view.findViewById<TextView>(R.id.mediaDuration)

            imageColor(typeIcon, Color.WHITE)
            typeIcon.background = GradientDrawable().apply {
                cornerRadius = 24f
                setColor(0x7B000000)
            }
            mediaDuration.background = GradientDrawable().apply {
                cornerRadius = 24f
                setColor(0x7B000000)
            }
            typeIcon.setImageResource(R.drawable.image_ic)
            mediaDuration.visibility = View.GONE

            relative.setOnClickListener {
                loadCropImage(data[position]["path"].toString(), false)
            }
            image.setImageBitmap(
                FileUtil.decodeSampleBitmapFromPath(
                    data[position]["path"].toString(),
                    1024,
                    1024
                )
            )
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }

    private fun imageColor(image: ImageView, color: Int) {
        image.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}
