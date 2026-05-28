package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.PredictionHistory
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private val viewModel: MainViewModel by viewModels()

    private var imageUri: String? = null
    private var resultLabel: String? = null
    private var confidenceScore: Float = 0f

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT_LABEL = "extra_result_label"
        const val EXTRA_CONFIDENCE_SCORE = "extra_confidence_score"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        setSupportActionBar(binding.resultToolbar)
        binding.resultToolbar.setNavigationOnClickListener { finish() }

        imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
        resultLabel = intent.getStringExtra(EXTRA_RESULT_LABEL)
        confidenceScore = intent.getFloatExtra(EXTRA_CONFIDENCE_SCORE, 0f)

        displayResult()

        binding.btnSave.setOnClickListener { saveResult() }
        binding.btnScanAgain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun displayResult() {
        val uri = imageUri?.let { Uri.parse(it) } ?: return

        // Show image (result_image ID preserved)
        binding.resultImage.load(uri) {
            crossfade(true)
            error(R.drawable.ic_place_holder)
        }

        val isCancer = resultLabel?.let { label ->
            label.contains("Cancer", ignoreCase = true) &&
                    !label.startsWith("Non", ignoreCase = true) &&
                    !label.contains("Non Cancer", ignoreCase = true)
        } ?: false

        val scorePercent = confidenceScore * 100f

        // Set result text (result_text ID preserved)
        binding.resultText.text = if (isCancer) {
            getString(R.string.result_cancer)
        } else {
            getString(R.string.result_non_cancer)
        }

        binding.resultSubtitle.text = if (isCancer) {
            getString(R.string.result_subtitle_cancer)
        } else {
            getString(R.string.result_subtitle_non_cancer)
        }

        // Badge
        binding.resultBadge.apply {
            text = resultLabel ?: "Unknown"
            if (isCancer) {
                setTextColor(getColor(R.color.cancer_color))
                setBackgroundResource(R.drawable.bg_badge_cancer)
            } else {
                setTextColor(getColor(R.color.non_cancer_color))
                setBackgroundResource(R.drawable.bg_badge_non_cancer)
            }
        }

        // Confidence score display
        binding.confidenceScoreText.apply {
            text = getString(R.string.confidence_score, scorePercent)
            setTextColor(
                if (isCancer) getColor(R.color.cancer_color)
                else getColor(R.color.primary)
            )
        }

        // Progress bar animation
        binding.confidenceProgressBar.apply {
            progress = scorePercent.toInt()
            if (isCancer) {
                setIndicatorColor(getColor(R.color.cancer_color))
            }
        }
    }

    private fun saveResult() {
        val uri = imageUri ?: run {
            showToast(getString(R.string.save_failed))
            return
        }

        val history = PredictionHistory(
            imageUri = uri,
            label = resultLabel ?: "Unknown",
            confidenceScore = confidenceScore,
            timestamp = System.currentTimeMillis()
        )

        viewModel.saveHistory(history)
        binding.btnSave.isEnabled = false
        binding.btnSave.text = "✓ Tersimpan"
        showToast(getString(R.string.save_success))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}