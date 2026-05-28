# Asclepius - Skin Cancer Detection App

Aplikasi **Asclepius** adalah proyek submission (tugas akhir) untuk kelas **Belajar Penerapan Machine Learning untuk Android** dari **Dicoding**. Aplikasi ini mengimplementasikan Machine Learning di perangkat (*on-device*) menggunakan TensorFlow Lite untuk mendeteksi indikasi kanker kulit dari gambar galeri secara cepat, efisien, dan aman tanpa memerlukan pemrosesan cloud.

---

## 📚 Tentang Kelas: Belajar Penerapan Machine Learning untuk Android

Kelas ini ditujukan bagi Android Developer yang ingin meningkatkan keterampilannya dalam mengintegrasikan *machine learning* di ekosistem Android. Total waktu yang dibutuhkan untuk menyelesaikan materi kelas ini adalah **60 jam**.

### Materi yang Dipelajari:

1. **Machine Learning di Android (4 Jam)**
   * Memahami alasan penggunaan Machine Learning secara lokal di perangkat Android.
   * Mempelajari contoh-contoh implementasi serta framework pendukung.
2. **Konsep Dasar Android untuk Machine Learning (6 Jam)**
   * Mendalami komponen dasar Android seperti *Custom View*, akses Kamera, dan Galeri.
   * Mempelajari integrasi API berbasis cloud menggunakan **Retrofit**.
3. **ML Kit (7 Jam)**
   * Implementasi solusi siap pakai (out-of-the-box) tanpa harus melatih model sendiri.
   * Contoh: *Text Recognition*, *Translation*, dan *Barcode Scanning*.
4. **TensorFlow Lite (9 Jam)**
   * Memahami cara mencari dan menyesuaikan model kustom (*Custom Model*).
   * Menerapkan model TensorFlow Lite untuk kasus *Image Classification*, *Object Detection*, dan *Prediction*.
5. **MediaPipe (6 Jam)**
   * Menggunakan MediaPipe sebagai abstraksi TFLite yang lebih tinggi untuk pemrosesan media real-time.
   * Menggunakan MediaPipe Studio untuk pengujian model secara visual.
6. **Firebase ML (4 Jam)**
   * Mempelajari distribusi model secara dinamis (*Over-The-Air* / OTA) untuk memperbarui model tanpa merilis ulang aplikasi.
7. **Generative AI (6 Jam)**
   * Menerapkan AI Generatif dengan ML Kit dan TensorFlow Lite untuk fitur *Smart Reply* dan *BERT Question & Answer*.

---

## 🚀 Fitur Utama Aplikasi Asclepius

Aplikasi ini dibangun untuk melakukan analisis gambar kulit secara on-device dengan fitur-fitur pendukung sebagai berikut:

* 🧠 **On-Device Image Classification (TensorFlow Lite)**
  Mengklasifikasikan gambar kulit apakah terindikasi **Cancer** atau **Non Cancer** lengkap dengan *Confidence Score*. Pemrosesan berjalan secara asinkron di *background thread* untuk menjaga UI tetap responsif.
* ✂️ **Image Cropping (uCrop Integration)**
  Memungkinkan pengguna untuk memotong (*crop*), merotasi, dan memperbesar gambar sebelum dianalisis, guna meningkatkan akurasi klasifikasi dengan memfokuskan area kulit bermasalah.
* 📦 **Local Database History (Room Database)**
  Menyimpan hasil pemeriksaan sebelumnya (gambar teranalisis, label diagnosis, nilai kepercayaan, dan waktu pemeriksaan) ke penyimpanan lokal secara permanen. Pengguna dapat melihat daftar riwayat dan menghapus riwayat yang diinginkan.
* 📰 **Health News (Retrofit API Integration)**
  Menyajikan artikel berita terbaru bertema kesehatan kulit secara real-time dari Web API menggunakan Retrofit dan GSON parser.

---

## 🛠️ Struktur Proyek (Isi Project)

Proyek ini menggunakan arsitektur bersih (*Clean Architecture* sederhana) dengan pemisahan tugas (*Separation of Concerns*):

```
app/src/main/java/com/dicoding/asclepius/
├── data/
│   ├── local/
│   │   ├── dao/          # Data Access Object (DAO) untuk Room Database
│   │   ├── database/     # Inisialisasi Room Database (PredictionDatabase)
│   │   └── entity/       # Struktur Data tabel riwayat (PredictionHistory)
│   ├── remote/
│   │   ├── api/          # Konfigurasi Retrofit & Endpoint API (NewsApiService)
│   │   └── response/     # Model data JSON (NewsResponse)
│   └── repository/       # Repository untuk menjembatani data lokal/remote ke ViewModel
├── helper/
│   └── ImageClassifierHelper.kt   # Helper TFLite Task Library untuk inisialisasi & klasifikasi gambar
└── view/
    ├── MainActivity.kt   # Layar utama (Pilih Gambar, Potong Gambar, Jalankan Analisis)
    ├── ResultActivity.kt # Layar hasil (Menampilkan diagnosis & tombol simpan riwayat)
    ├── HistoryActivity.kt# Layar riwayat diagnosis tersimpan
    ├── NewsActivity.kt   # Layar daftar berita kesehatan
    └── *ViewModel & *Adapter untuk masing-masing layar
```

---

## ⚙️ Persyaratan Sistem & Dependensi

Untuk menjalankan proyek ini di perangkat lokal Anda, pastikan sistem Anda memenuhi persyaratan berikut:

* **Android Studio**: Koala / Ladybug atau versi yang lebih baru.
* **JDK**: Versi 17.
* **Minimum SDK**: API 21 (Android 5.0).
* **Target SDK**: API 34 (Android 14).

### Pustaka Penting yang Digunakan:
* `org.tensorflow:tensorflow-lite-task-vision`: Pustaka utama untuk menjalankan klasifikasi gambar secara lokal.
* `com.github.yalantis:ucrop`: Pustaka manipulasi / cropping gambar.
* `androidx.room:room-runtime`: Pustaka ORM untuk basis data lokal.
* `com.squareup.retrofit2:retrofit`: Pustaka HTTP Client untuk mengambil data berita.
