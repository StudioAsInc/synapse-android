package com.synapse.social.studioasinc

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.synapse.social.studioasinc.databinding.ChatsettingsBinding
import com.synapse.social.studioasinc.util.ThemeUtils

class ChatSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ChatsettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mainDb: DatabaseReference
    private lateinit var appSettings: android.content.SharedPreferences
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChatsettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        mainDb = FirebaseDatabase.getInstance().reference.child("main")
        appSettings = getSharedPreferences("appSettings", MODE_PRIVATE)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        initializeLogic()
    }

    private fun initializeLogic() {
        binding.mBack.setOnClickListener { finish() }

        binding.inappbrowserSwitchLay.setOnClickListener {
            startActivity(Intent(this, BgWallpapersActivity::class.java))
        }

        binding.premiumFeaturesMainOption.setOnClickListener {
            startActivity(Intent(this, LabsActivity::class.java))
        }

        setupTextSizeSlider()
        setupCornerRadiusSlider()
        setupUI()
    }

    private fun setupTextSizeSlider() {
        val textSizeSlider = binding.seekbar6
        val savedTextSize = appSettings.getString("ChatTextSize", "16")
        val initialTextSizeSliderValue = savedTextSize?.toFloatOrNull() ?: 16f

        Log.d("ChatTextSize", "Using text size: $initialTextSizeSliderValue")
        textSizeSlider.value = initialTextSizeSliderValue

        updateTextSize(initialTextSizeSliderValue)

        textSizeSlider.addOnChangeListener { _, value, _ ->
            updateTextSize(value)
            appSettings.edit().putString("ChatTextSize", value.toString()).apply()
        }
    }

    private fun updateTextSize(size: Float) {
        binding.txtMsg1.textSize = size
        binding.messageText.textSize = size
        binding.mRepliedMessageLayoutMessage.textSize = size
    }

    private fun setupCornerRadiusSlider() {
        val cornerRadiusSlider = binding.roundSeekbar
        val savedCornerRadius = appSettings.getString("ChatCornerRadius", "20")
        val initialCornerRadiusSliderValue = savedCornerRadius?.toFloatOrNull() ?: 20f

        Log.d("ChatCornerRadius", "Using corner radius: $initialCornerRadiusSliderValue")
        cornerRadiusSlider.value = initialCornerRadiusSliderValue

        updateCornerRadius(initialCornerRadiusSliderValue)

        cornerRadiusSlider.addOnChangeListener { _, value, _ ->
            updateCornerRadius(value)
            appSettings.edit().putString("ChatCornerRadius", value.toString()).apply()
        }
    }

    private fun updateCornerRadius(radius: Float) {
        val density = resources.displayMetrics.density
        val cornerRadius = density * radius

        val messageBg1Drawable = GradientDrawable().apply {
            setColor(0xFF6B4CFF.toInt())
            setCornerRadius(cornerRadius)
        }
        binding.messageBG1.background = messageBg1Drawable

        val messageBgDrawable = GradientDrawable().apply {
            setColor(0xFFFFFFFF.toInt())
            setCornerRadius(cornerRadius)
            setStroke((2 * density).toInt(), 0xFFDFDFDF.toInt())
        }
        binding.messageBG.background = messageBgDrawable
    }

    private fun setupUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = 0xFFFFFFFF.toInt()
        window.navigationBarColor = 0xFFFAFAFA.toInt()

        binding.applicationStage.background = GradientDrawable().apply {
            cornerRadius = 35f
            setColor(0xFFFFFFFF.toInt())
        }
        binding.accountStage.background = GradientDrawable().apply {
            cornerRadius = 35f
            setColor(0xFFFFFFFF.toInt())
        }
        binding.accountStack2.background = GradientDrawable().apply {
            cornerRadius = 35f
            setColor(0xFFFFFFFF.toInt())
        }
        binding.accountStack3.background = GradientDrawable().apply {
            cornerRadius = 35f
            setColor(0xFFFFFFFF.toInt())
        }

        binding.mRepliedMessageLayoutLeftBar.background = GradientDrawable().apply {
            cornerRadius = 360f
            setColor(ThemeUtils.getThemeColor(this@ChatSettingsActivity, com.google.android.material.R.attr.colorPrimaryContainer))
        }

        binding.mProfileImage.setImageResource(R.drawable.ashik_dp)
        binding.mProfileImage.clipToOutline = true
        binding.mProfileImage.background = GradientDrawable().apply {
            cornerRadius = 300f
            setColor(Color.TRANSPARENT)
        }
    }
}
