# 🎵 MusicPlayerApp

Aplikasi Android sederhana untuk memutar file musik (MP3) yang tersimpan di penyimpanan eksternal, dikembangkan menggunakan **Jetpack Compose** dan `MediaPlayer`.

---

## 📱 Fitur

- Menampilkan daftar lagu dari folder Music
- Memutar lagu lokal (.mp3)
- Kontrol pemutar:
  - ▶️ Play / ⏸ Pause
  - ⏹ Stop
  - ⏭ Next
  - ⏮ Previous
- Permintaan izin otomatis untuk mengakses media
- Antarmuka modern menggunakan Jetpack Compose

---

## 📂 Struktur Folder

MusicPlayerApp/
├── app/
│ ├── src/
│ │ └── main/
│ │ ├── java/com/sonic/exoplayer/MainActivity.kt
│ │ ├── res/
│ │ └── AndroidManifest.xml
├── build.gradle
├── settings.gradle
├── README.md
└── .gitignore


---

## 🚀 Cara Menjalankan

### ✅ Via Android Studio

1. Clone repository:

git clone https://github.com/RofStar9/MusicPlayerApp.git


2. Buka Android Studio → `File > Open`
3. Pilih folder `MusicPlayerApp`
4. Tunggu Gradle sync selesai
5. Jalankan ke emulator atau perangkat

### ✅ Via Terminal Android Studio

```bash
git clone https://github.com/RofStar9/MusicPlayerApp.git
cd MusicPlayerApp
./gradlew build

🛠️ Teknologi yang Digunakan

    Kotlin

    Jetpack Compose

    Android Jetpack Components

    MediaPlayer API

    Permission Handling (Android 13+ dan sebelumnya)

👤 Developer

Rama Oris Fernando
🆔 NIM: 233310010
📘 Program Studi Teknologi Komputer D3
🏫 Universitas Teknologi Digital Indonesia
