package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    private val context: Context,
    private val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    companion object {
        private const val TAG = "ImageClassifierHelper"
        private const val MODEL_NAME = "cancer_classification.tflite"
        private const val MAX_RESULTS = 1
        private const val THRESHOLD = 0.0f
        private const val INPUT_SIZE = 224
    }

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        // TODO: Menyiapkan Image Classifier untuk memproses gambar.
        try {
            val options = ImageClassifier.ImageClassifierOptions.builder()
                .setScoreThreshold(THRESHOLD)
                .setMaxResults(MAX_RESULTS)
                .build()
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                MODEL_NAME,
                options
            )
        } catch (e: Exception) {
            classifierListener?.onError(e.message ?: "Failed to initialize classifier")
            Log.e(TAG, "Error initializing classifier: ${e.message}")
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        // TODO: mengklasifikasikan imageUri dari gambar statis.
        if (imageClassifier == null) {
            setupImageClassifier()
            if (imageClassifier == null) {
                classifierListener?.onError("Classifier not initialized. Please try again.")
                return
            }
        }

        val bitmap = uriToBitmap(imageUri) ?: run {
            classifierListener?.onError("Failed to load image. Please try another image.")
            return
        }

        // Pre-process: resize to 224x224, cast to UINT8 (no normalization needed for uint8 model)
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(INPUT_SIZE, INPUT_SIZE, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.UINT8))
            .build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

        try {
            val results = imageClassifier?.classify(tensorImage)
            classifierListener?.onResults(results)
        } catch (e: Exception) {
            classifierListener?.onError(e.message ?: "Classification failed. Please try again.")
            Log.e(TAG, "Classification error: ${e.message}")
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }.copy(Bitmap.Config.ARGB_8888, true)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to convert URI to Bitmap: ${e.message}")
            null
        }
    }

    fun close() {
        imageClassifier?.close()
        imageClassifier = null
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(results: List<Classifications>?)
    }
}