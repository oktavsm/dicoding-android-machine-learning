# Asclepius - Skin Cancer Detection App

The **Asclepius** application is the final project submission for the **"Belajar Penerapan Machine Learning untuk Android"** (Learning Machine Learning Application for Android) course by **Dicoding**. This application implements on-device Machine Learning using TensorFlow Lite to detect indications of skin cancer from gallery images quickly, efficiently, and securely without requiring cloud-based processing.

---

## 📚 About the Course: Learning Machine Learning Application for Android

This course is designed for Android Developers who want to enhance their skills in integrating machine learning models into the Android ecosystem. The total duration required to complete this course is **60 hours**.

### Course Modules & Syllabus:

1. **Machine Learning on Android (4 Hours)**
   * Understand the reasons for using Machine Learning locally on Android devices with real-world use cases.
   * Learn about various frameworks and tools available for mobile ML deployment.
2. **Android Basic Concepts for Machine Learning (6 Hours)**
   * Deepen knowledge of core Android components used in ML integrations, such as Custom Views, Camera APIs, and Gallery Access.
   * Learn about cloud-based machine learning integration using **Retrofit**.
3. **ML Kit (7 Hours)**
   * Learn to apply pre-trained, out-of-the-box ML models directly without custom training.
   * Examples: *Text Recognition*, *Translation*, and *Barcode Scanning*.
4. **TensorFlow Lite (9 Hours)**
   * Search, select, and customize *Custom Models* for mobile.
   * Implement TensorFlow Lite for *Image Classification*, *Object Detection*, and *Prediction*.
5. **MediaPipe (6 Hours)**
   * Learn to use MediaPipe as a higher abstraction layer over TensorFlow Lite for complex, real-time media processing.
   * Explore MediaPipe Studio to test and configure models visually.
6. **Firebase ML (4 Hours)**
   * Learn dynamic model hosting and Over-The-Air (OTA) model updates without needing to republish the app.
7. **Generative AI (6 Hours)**
   * Get creative with Generative AI using ML Kit and TensorFlow Lite to build smart interactions like *Smart Reply* and *BERT Question & Answer*.

---

## 🚀 Key Features

This application is built to perform skin image analysis locally with the following features:

* 🧠 **On-Device Image Classification (TensorFlow Lite)**
  Classifies skin images as **Cancer** or **Non Cancer** complete with a *Confidence Score*. Processing runs asynchronously in a background thread to keep the user interface smooth and responsive.
* ✂️ **Image Cropping (uCrop Integration)**
  Allows users to crop, rotate, and zoom the selected image before analysis to focus precisely on the skin lesion, improving classification accuracy.
* 📦 **Local Database History (Room Database)**
  Saves classification results locally (analyzed image path, diagnostic label, confidence level, and timestamp). Users can view the history list and delete records at any time.
* 📰 **Health News (Retrofit API Integration)**
  Displays the latest skin-health-related articles in real-time from a Web API using Retrofit and GSON parser.

---

## 🛠️ Project Directory Structure

The project follows a clean and structured architecture:

```
app/src/main/java/com/dicoding/asclepius/
├── data/
│   ├── local/
│   │   ├── dao/          # Data Access Object (DAO) for Room Database operations
│   │   ├── database/     # Room Database Initialization (PredictionDatabase)
│   │   └── entity/       # Database Schema entity (PredictionHistory)
│   ├── remote/
│   │   ├── api/          # Retrofit Configuration & Endpoint Interface (NewsApiService)
│   │   └── response/     # GSON data response models (NewsResponse)
│   └── repository/       # Repository layer to bridge local/remote data sources with ViewModels
├── helper/
│   └── ImageClassifierHelper.kt   # TFLite Task Library helper for initializing the model & running inference
└── view/
    ├── MainActivity.kt   # Main Screen (select image, initiate crop, trigger classification)
    ├── ResultActivity.kt # Result Screen (display diagnosis, save result to local database)
    ├── HistoryActivity.kt# History Screen (view and delete saved analysis records)
    ├── NewsActivity.kt   # News Screen (displays healthy skin articles in a list)
    └── *ViewModel & *Adapter classes for respective screens
```

---

## ⚙️ Requirements & Dependencies

To build and run this application on your local machine, ensure you meet the following prerequisites:

* **Android Studio**: Koala / Ladybug or newer.
* **JDK**: Version 17.
* **Minimum SDK**: API 21 (Android 5.0).
* **Target SDK**: API 34 (Android 14).

### Key Dependencies Used:
* `org.tensorflow:tensorflow-lite-task-vision`: Core library for on-device image classification.
* `com.github.yalantis:ucrop`: Image cropping and editing library.
* `androidx.room:room-runtime`: Local database ORM.
* `com.squareup.retrofit2:retrofit`: HTTP Client for fetching remote health articles.
