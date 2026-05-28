package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private val viewModel: MainViewModel by viewModels()

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            // Launch uCrop after gallery selection (Saran 2)
            startUCrop(uri)
        } else {
            showToast(getString(R.string.no_image_selected))
        }
    }

    private val uCropLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            if (resultUri != null) {
                currentImageUri = resultUri
                viewModel.setImageUri(resultUri)
                showImage()
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            showToast("Crop error: ${cropError?.message}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Restore state
        viewModel.currentImageUri.observe(this) { uri ->
            if (uri != null && currentImageUri != uri) {
                currentImageUri = uri
                showImage()
            }
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.progressIndicator.visibility = if (loading) View.VISIBLE else View.GONE
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }

        // Bottom Navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    false
                }
                R.id.nav_news -> {
                    startActivity(Intent(this, NewsActivity::class.java))
                    false
                }
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.nav_home
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startUCrop(sourceUri: Uri) {
        val fileName = "crop_${System.currentTimeMillis()}.jpg"
        val destUri = Uri.fromFile(File(cacheDir, fileName))

        val uCropIntent = UCrop.of(sourceUri, destUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1080, 1080)
            .withOptions(UCrop.Options().apply {
                setToolbarColor(getColor(R.color.primary))
                setStatusBarColor(getColor(R.color.primary_dark))
                setActiveControlsWidgetColor(getColor(R.color.primary))
                setToolbarWidgetColor(getColor(R.color.white))
                setFreeStyleCropEnabled(true)
                setCompressionQuality(90)
            })
            .getIntent(this)
        uCropLauncher.launch(uCropIntent)
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let { uri ->
            binding.previewImageView.setImageURI(uri)
        }
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        currentImageUri?.let { uri ->
            viewModel.setLoading(true)
            val classifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        runOnUiThread {
                            viewModel.setLoading(false)
                            showToast(getString(R.string.classification_error, error))
                        }
                    }

                    override fun onResults(results: List<Classifications>?) {
                        runOnUiThread {
                            viewModel.setLoading(false)
                            if (results.isNullOrEmpty() || results[0].categories.isEmpty()) {
                                showToast(getString(R.string.failed_to_process))
                                return@runOnUiThread
                            }
                            val topResult = results[0].categories[0]
                            val label = topResult.label
                            val score = topResult.score
                            moveToResult(uri, label, score)
                        }
                    }
                }
            )
            // Run on background thread to avoid blocking UI
            Thread {
                classifierHelper.classifyStaticImage(uri)
            }.start()
        } ?: showToast(getString(R.string.no_image_selected))
    }

    private fun moveToResult(imageUri: Uri, label: String, score: Float) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri.toString())
            putExtra(ResultActivity.EXTRA_RESULT_LABEL, label)
            putExtra(ResultActivity.EXTRA_CONFIDENCE_SCORE, score)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}