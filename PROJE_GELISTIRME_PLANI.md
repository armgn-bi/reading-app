# Proje Geliştirme Planı

## Proje Özeti
Japonca kitap okuma uygulaması - Samsung Android cihazlar için optimize edilmiş, lightweight native uygulama.

## Teknoloji Stack

| Katman | Teknoloji | Neden |
|--------|-----------|-------|
| Platform | Android (Native) | En düşük kaynak kullanımı, en iyi performans |
| Dil | Kotlin | Modern, type-safe, Google'ın resmi tercihi |
| UI | Jetpack Compose | Modern, declarative UI, az boilerplate |
| Veritabanı | Room (SQLite) | Local storage, SQL sorguları, migration desteği |
| Dosya Okuma | Java IO (TXT), PdfBox (PDF), R2Reader (EPUB) | Çoklu format desteği, lightweight |
| TTS | Android TextToSpeech API | Native, sistem entegrasyonu |
| Çeviri | ML Kit Translation (ücretsiz) veya DeepL API | Offline/online seçenekleri |
| Sözlük | Yerel JSON/SQLite veritabanı | Offline çalışma |

## Mimari

```
┌─────────────────────────────────────────────────────────┐
│                    UI Layer (Jetpack Compose)            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │  Book List  │  │  Reader UI   │  │  Word Book   │    │
│  └──────────────┘  └──────────────┘  └──────────────┘    │
└─────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────┐
│                  ViewModel Layer                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │ BookViewModel│  │ReaderViewModel│ │WordViewModel│    │
│  └──────────────┘  └──────────────┘  └──────────────┘    │
└─────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────┐
│                  Repository Layer                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │ BookRepo     │  │ReaderRepo    │  │WordRepo      │    │
│  └──────────────┘  └──────────────┘  └──────────────┘    │
└─────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────┐
│                  Data Layer                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │ Room DB      │  │File Storage  │  │SharedPreferences│  │
│  └──────────────┘  └──────────────┘  └──────────────┘    │
└─────────────────────────────────────────────────────────┘
```

## Geliştirme Aşamaları

### Aşama 1: Temel Altyapı (1-2 hafta)
- [ ] Android projesi kurulumu (Kotlin + Jetpack Compose)
- [ ] Room veritabanı kurulumu
- [ ] Temel proje yapısı (MVVM)
- [ ] Dependency Injection (Hilt/Koin)
- [ ] Temel UI bileşenleri

### Aşama 2: Kitap Yönetimi (1-2 hafta)
- [ ] Dosya seçici ve yükleme
- [ ] TXT dosya okuma
- [ ] Kitap listesi UI
- [ ] Kitap detay sayfası
- [ ] Kitap silme/düzenleme

### Aşama 3: Okuma Arayüzü (2-3 hafta)
- [ ] Metin görüntüleme (scrollable)
- [ ] Kelime seçimi
- [ ] Cümle seçimi
- [ ] Furigana gösterimi
- [ ] Yazı boyutu ayarı
- [ ] Koyu/açık tema
- [ ] Okuma ilerlemesi kaydetme

### Aşama 4: Kelime Bilgisi (1-2 hafta)
- [ ] Kelime sözlüğü veritabanı
- [ ] Kelime detay popup
- [ ] Okunuş (romaji/hiragana) gösterme
- [ ] Kelime defteri
- [ ] Kelime kartları

### Aşama 5: Çeviri (1 hafta)
- [ ] Google Translate API entegrasyonu
- [ ] Cümle çevirisi gösterme
- [ ] Çeviri cache'leme
- [ ] Offline fallback

### Aşama 6: Ek Formatlar (1-2 hafta)
- [ ] PDF okuma
- [ ] EPUB okuma
- [ ] Format dönüştürücü

### Aşama 7: Sesli Okuma (1 hafta)
- [ ] TTS entegrasyonu
- [ ] Ses kontrolü (oynat/durdur/hız)
- [ ] Japonca ses motoru

### Aşama 8: İstatistikler (1 hafta)
- [ ] Okuma süresi takibi
- [ ] Kelime sayısı
- [ ] İlerleme grafikleri
- [ ] Günlük/haftalık özet

### Aşama 9: Arama (1 hafta)
- [ ] Kitap içinde arama
- [ ] Kelime defterinde arama
- [ ] Filtreleme

### Aşama 10: Optimizasyon & Test (1-2 hafta)
- [ ] Performans optimizasyonu
- [ ] Memory leak kontrolü
- [ ] UI testleri
- [ ] Unit testleri
- [ ] Samsung cihaz testleri

## Veritabanı Şeması

### Kitaplar (books)
```sql
CREATE TABLE books (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    file_path TEXT NOT NULL UNIQUE,
    file_type TEXT NOT NULL, -- txt, pdf, epub
    total_pages INTEGER,
    current_page INTEGER DEFAULT 0,
    current_position INTEGER DEFAULT 0, -- karakter pozisyonu
    created_at INTEGER NOT NULL,
    last_read_at INTEGER,
    cover_image BLOB,
    category TEXT
);
```

### Kelime Defteri (word_book)
```sql
CREATE TABLE word_book (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    word TEXT NOT NULL,
    reading TEXT, -- hiragana okunuş
    meaning TEXT NOT NULL,
    romaji TEXT,
    jlpt_level TEXT, -- N1-N5
    example_sentence TEXT,
    created_at INTEGER NOT NULL,
    review_count INTEGER DEFAULT 0,
    next_review_at INTEGER
);
```

### Okuma İstatistikleri (reading_stats)
```sql
CREATE TABLE reading_stats (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id INTEGER NOT NULL,
    date INTEGER NOT NULL, -- timestamp
    duration_seconds INTEGER NOT NULL,
    words_read INTEGER NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(id)
);
```

### Kelime Kartları (flashcards)
```sql
CREATE TABLE flashcards (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    word_id INTEGER NOT NULL,
    box INTEGER DEFAULT 0, -- Leitner sistemi
    last_reviewed_at INTEGER,
    next_review_at INTEGER,
    FOREIGN KEY (word_id) REFERENCES word_book(id)
);
```

## Dosya Yapısı

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/readingapp/
│   │   │   ├── data/
│   │   │   │   ├── database/
│   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   ├── entities/
│   │   │   │   │   └── dao/
│   │   │   │   ├── repository/
│   │   │   │   └── models/
│   │   │   ├── ui/
│   │   │   │   ├── screens/
│   │   │   │   │   ├── BookListScreen.kt
│   │   │   │   │   ├── ReaderScreen.kt
│   │   │   │   │   ├── WordBookScreen.kt
│   │   │   │   │   └── SettingsScreen.kt
│   │   │   │   ├── components/
│   │   │   │   └── theme/
│   │   │   ├── viewmodel/
│   │   │   ├── utils/
│   │   │   │   ├── file/
│   │   │   │   ├── translation/
│   │   │   │   └── tts/
│   │   │   └── ReadingApp.kt
│   │   ├── res/
│   │   └── AndroidManifest.xml
```

## Kütüphaneler

```gradle
dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // UI
    implementation("androidx.compose.ui:ui:1.7.6")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.activity:activity-compose:1.9.3")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Dependency Injection
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // File Reading
    implementation("com.tom-roush:pdfbox-android:2.0.27.0")
    implementation("org.readium:r2-shared:2.0.0")
    implementation("org.readium:r2-reader:2.0.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // DataStore (SharedPreferences replacement)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Coil (Image loading)
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Paging 3
    implementation("androidx.paging:paging-runtime:3.3.5")
    implementation("androidx.paging:paging-compose:3.3.5")

    // ML Kit Translation (ücretsiz, offline)
    implementation("com.google.mlkit:translate:17.0.2")

    // Accompanist (ek Compose araçları)
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.6")
}
```

## Performans Optimizasyonları

1. **LazyColumn** ile büyük metinler için virtualized scrolling
2. **Paging 3** ile kitap sayfaları için lazy loading
3. **Room** ile veritabanı sorgularında indeksleme
4. **Glide/Coil** ile resim cache'leme
5. **Coroutines** ile arka planda işlem
6. **ViewModel** ile UI state yönetimi
7. **Hilt** ile dependency injection

## Test Stratejisi

- **Unit Test**: ViewModel ve Repository katmanları
- **UI Test**: Compose bileşenleri
- **Integration Test**: Veritabanı işlemleri
- **Device Test**: Samsung cihazlarda gerçek test

## Minimum SDK Sürümü

- **Min SDK**: API 24 (Android 7.0) - %98+ cihaz kapsama
- **Target SDK**: API 35 (Android 15)
- **Compile SDK**: API 35

## Geliştirme Ortamı

### Gerekli Uygulamalar

| Uygulama | Sürüm | Açıklama |
|----------|-------|----------|
| Android Studio | Hedgehog (2023.1.1) veya üzeri | Resmi IDE |
| JDK | 17 veya 21 | Java Development Kit |
| Android SDK | API 35 | Android SDK Platform |
| Git | Son sürüm | Versiyon kontrolü |

### Gerekli SDK Bileşenleri

Android Studio'da SDK Manager'dan yüklenmeli:
- Android SDK Platform 35
- Android SDK Build-Tools 35.0.0
- Android Emulator (test için)
- Google Play Intel x86 Atom System Image (emülatör için)

### Gradle Yapılandırması

`build.gradle.kts` dosyasına eklenecek pluginler:

```kotlin
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
}
```

### Emülatör Ayarları (Samsung Testi İçin)

Samsung deneyimi için emülatör oluştururken:
- Device: Pixel 6 veya Pixel 7 (One UI benzeri)
- System Image: Android 14/15 with Google Play
- RAM: En az 4GB
- Storage: En az 8GB

### Gerçek Cihaz Testi İçin

Samsung cihazda geliştirme modunu açmak:
1. Ayarlar > Yazılım Bilgisi > Yapı Numarası'na 7 kez tıkla
2. Ayarlar > Geliştirici Seçenekleri
3. USB Hata Ayıklama'yı aç
4. USB ile bilgisayara bağla

## Proje Durumu

| Durum | Açıklama |
|-------|----------|
| Başlangıç | Proje henüz oluşturulmadı |
| İlerleme | %0 |
| Sonraki Adım | Android Studio'da proje oluşturma |

## Sonraki Adım

Aşama 1 ile başlamak için Android Studio'da yeni proje oluşturulmalı.
