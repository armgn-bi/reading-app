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
| Sözlük | JMdict subset (test), JMdict full (prod) | Offline çalışma, açık kaynak |

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
- [x] Android projesi kurulumu (Kotlin + Jetpack Compose)
- [x] Room veritabanı kurulumu
- [x] Temel proje yapısı (MVVM)
- [x] Dependency Injection (Koin) - Hilt KSP uyumsuzluğu nedeniyle Koin kullanıldı
- [x] Temel UI bileşenleri
- [x] BookListScreen (tamamlanmış - kitap ismi, son okuma tarihi, ilerleme durumu)
- [x] ReaderScreen (placeholder - Aşama 3'te detaylandırılacak)

### Aşama 2: Kitap Yönetimi (1-2 hafta)
- [x] Dosya seçici ve yükleme
- [x] TXT dosya okuma
- [x] Paralel çeviri dosyası desteği (kitap.txt + kitap_tr.txt)
- [x] Kitap listesi UI
- [x] Kitap detay sayfası (ReaderScreen'e geçiş yapılacak - Aşama 3)
- [x] Kitap silme/düzenleme

### Aşama 3: Okuma Arayüzü (2-3 hafta)
- [ ] Metin görüntüleme (sayfa şeklinde)
- [ ] Sayfalama stratejisi: Satır sayısına göre (20 satır = 1 sayfa, yazı boyutuna göre dinamik)
- [ ] Kelime seçimi (tıklayınca kelime seçme - space ile kelime sınırları)
- [ ] Cümle seçimi (long press)
- [ ] Furigana gösterimi (varsayılan olarak açık, ayarlardan kapatılabilir, metin formatı: 昔々[むかしむかし], boyut: %50 küçük, renk: ana metin ile aynı)
- [ ] Yazı boyutu ayarı (okuma ekranında hızlı buton, standart uygulama aralığı)
- [ ] Koyu/açık tema (tema seçme butonu ile)
- [ ] Okuma ilerlemesi kaydetme
- [ ] Üst panel: Sabit yükseklik, kelime seçiliyse kelime bilgisi, değilse cümle çevirisi (her zaman açık)
- [ ] Sayfa navigasyonu: Butonlar + swipe gesture (sağa/sola kaydırma), içeride sayfa numarası gösterimi
- [ ] Kelime seçimi akışı:
  - [ ] Kelimeye tıklayınca kelime seçilir, üst panelde kelime bilgisi gösterilir
  - [ ] Kelimeye basılı tutunca kelime seçimi kalır, üst panelde kelime bilgisi gösterilir
  - [ ] Basılı tutmayı bırakınca kelime seçimi iptal olur, üst panelde cümle çevirisine dönülür
- [ ] Yazı boyutu değişince sayfa sayısı da değişir

### Aşama 4: Kelime Bilgisi (1-2 hafta)
- [ ] Kelime sözlüğü veritabanı (JMdict subset - test için küçük veri seti)
- [ ] Kelime seçimi (tıklayınca kelime seçme - space ile kelime sınırları)
- [ ] Kelimeye basılı tutma (press/release ile seçili kalma)
- [ ] Kelime sekme (sayfanın üstünde çeviri kısmı yerine açılacak)
- [ ] Kelime bilgisi gösterimi (kelime, okunuşu, anlamı, JLPT seviyesi)
- [ ] Okunuş (hiragana) gösterme
- [ ] Kelime defteri
- [ ] Kelime defteri ekranı:
  - [ ] Kelime listesi (kelime, okunuş, anlam, JLPT, tarih)
  - [ ] Arama (kanji veya romaji ile)
  - [ ] Sıralama seçeneği (tarih, kelime, JLPT)
  - [ ] JLPT seviyesine göre filtreleme
  - [ ] Silme (onay dialog ile)
  - [ ] CSV dışa aktar (kelime, okunuş, anlam alanları)
  - [ ] Anki'ye aktar butonu (kelime defteri içinde)

### Aşama 5: Çeviri (1 hafta)
- [ ] Paralel çeviri dosyası desteği (öncelikli)
- [ ] Google Translate API entegrasyonu (fallback)
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
- [ ] Genel istatistikler (toplam kitap, sayfa, kelime sayısı)
- [ ] Kitap bazında istatistikler (okunan sayfa, ilerleme yüzdesi)
- [ ] Kelime istatistikleri (JLPT seviyesine göre dağılım)
- [ ] Zaman bazında istatistikler (tüm zaman)
- [ ] Başarı istatistikleri (tamamlanan kitap sayısı)
- [ ] Grafikler (çubuk grafik)

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
    translation_file_path TEXT, -- paralel çeviri dosyası yolu
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
    created_at INTEGER NOT NULL
);
```

### Okuma İstatistikleri (reading_stats)
```sql
CREATE TABLE reading_stats (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id INTEGER NOT NULL,
    date INTEGER NOT NULL, -- timestamp
    created_at INTEGER NOT NULL
);
```

### Kelime Kartları (flashcards)
```sql
-- Kaldırıldı - Kelime listesi yeterli, Anki'ye aktarılacak
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
    implementation("io.insert-koin:koin-android:4.0.0")
    implementation("io.insert-koin:koin-androidx-compose:4.0.0")

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
| Başlangıç | Proje oluşturuldu |
| İlerleme | %20 (Aşama 2/10 tamamlandı ve test edildi) |
| Sonraki Adım | Aşama 3: Okuma Arayüzü |

## Notlar

### Aşama 2 Tamamlandı (Test Edildi)
- Dosya seçici ve yükleme ✅
- TXT dosya okuma ✅
- Paralel çeviri dosyası desteği ✅
- Kitap listesi UI ✅
- Kitap silme (onay dialog ile) ✅
- Kitap ismi değiştirme ✅
- Room migration hatası düzeltildi (fallbackToDestructiveMigration) ✅



## Navigation

**Ekranlar:**
1. BookListScreen (Ana ekran)
2. ReaderScreen (Okuma ekranı)
3. WordBookScreen (Kelime defteri)
4. SettingsScreen (Ayarlar)

**Navigation Graph:**
```
BookListScreen
    ↓ (kitap seçimi)
ReaderScreen
    ↓ (ayarlar butonu)
SettingsScreen
    ↓ (kelime defteri butonu)
WordBookScreen
```

**Parametreler:**
- `ReaderScreen`: `bookId: Long`
- Diğer ekranlar: Parametre yok

**Navigation Kod Örneği:**
```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "book_list") {
        composable("book_list") {
            BookListScreen(
                onBookClick = { bookId ->
                    navController.navigate("reader/$bookId")
                },
                onWordBookClick = {
                    navController.navigate("word_book")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }

        composable(
            route = "reader/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: 0L
            ReaderScreen(
                bookId = bookId,
                onBack = {
                    navController.popBackStack()
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }

        composable("word_book") {
            WordBookScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
```

**Bottom Navigation Bar:**
- Sadece okuma ekranı için
- Kitaplar, Kelime Defteri, Ayarlar

## Sonraki Adım

Aşama 1 ile başlamak için temel altyapı özellikleri geliştirilecek.

Aşama 2 ile başlamak için kitap yönetimi özellikleri geliştirilecek.

## Algoritmalar

### 10.1 Furigana Ayrıştırma

**Giriş:** `<昔々[むかしむかし]>、<あるところに><小さな><村><がありました>。`

**Çıkış:**
```kotlin
listOf(
    FuriganaText("昔々", "むかしむかし", 0, 4),
    FuriganaText("、", null, 4, 5),
    FuriganaText("あるところに", null, 5, 10),
    FuriganaText("小さな", null, 10, 13),
    FuriganaText("村", null, 13, 14),
    FuriganaText("がありました", null, 14, 19),
    FuriganaText("。", null, 19, 20)
)
```

**Algoritma:**
1. Metni tarayarak `<` ve `>` karakterlerini bul
2. `<` ve `>` arasındaki metni kelime olarak al
3. Kelime içinde `[` ve `]` karakterlerini bul
4. `[` ve `]` arasındaki metni furigana olarak al
5. Furigana olmayan metinleri normal olarak işle

### 10.2 Kelime Seçimi

**Giriş:** Tıklama pozisyonu (örn: 15)

**Algoritma:**
1. Tıklama pozisyonunu bul
2. `<` ve `>` işaretleri ile kelime sınırlarını bul
3. Kelimeyi seç
4. Kelimeyi state'te tut

### 10.3 Cümle Seçimi

**Giriş:** Tıklama pozisyonu (örn: 15)

**Algoritma:**
1. Tıklama pozisyonunu bul
2. Soldan cümle başlangıcını bul (。？！)
3. Sağdan cümle sonunu bul (。？！)
4. Cümleyi seç
5. Cümleyi state'te tut

### 10.4 Sayfalama

**Giriş:** Metin, yazı boyutu, satır sayısı

**Algoritma:**
1. Metni satırlara böl (yazı boyutuna göre)
2. Her satır için karakter sayısını hesapla
3. Satırları sayfalara grupla (10 satır = 1 sayfa, yazı boyutuna göre dinamik)
4. Sayfa sayısını hesapla
5. Yazı boyutu değişince yeniden hesapla

### 10.5 İlerleme Kaydetme

**Algoritma:**
1. Sayfa değişince:
   - `currentPage`'i güncelle
   - `lastReadAt`'ı güncelle
   - Veritabanına kaydet
2. Uygulama kapanınca:
   - Son durumu kaydet
3. Kitap yüklenince:
   - Son okunan sayfayı yükle

### 10.6 Paralel Çeviri Dosyası Okuma

**Giriş:** Kitap dosyası yolu, çeviri dosyası yolu

**Algoritma:**
1. Kitap dosyasını oku
2. Çeviri dosyasını oku (varsa)
3. Satır bazlı eşleştirme
4. Cümle çevirisi bulma

### 10.7 Kelime Defterine Ekleme

**Giriş:** Kelime, okunuş, anlam, JLPT seviyesi

**Algoritma:**
1. Kelime zaten var mı kontrol et
2. Yoksa yeni kelime oluştur
3. Veritabanına kaydet
4. Tarih kaydet

### 10.8 CSV Dışa Aktar

**Giriş:** Kelime defteri listesi

**Algoritma:**
1. Kelime listesini CSV formatına dönüştür
2. Dosya kaydet
3. Başarı mesajı göster

### 10.9 Anki'ye Aktar

**Giriş:** Kelime defteri listesi

**Algoritma:**
1. Kelime listesini Anki formatına dönüştür
2. Dosya kaydet
3. Başarı mesajı göster

### 10.10 İstatistikler Hesaplama

**Giriş:** Veritabanı

**Algoritma:**
1. Toplam kitap sayısını hesapla
2. Toplam sayfa sayısını hesapla
3. Kaydedilen kelime sayısını hesapla
4. Kitap bazında istatistikleri hesapla
5. Kelime dağılımını (JLPT) hesapla
6. Tamamlanan kitap sayısını hesapla

### 10.11 Kitap Gruplandırma

**Giriş:** Kategori adı

**Algoritma:**
1. Kategori oluştur
2. Kitapları kategorilere ata
3. Kategoriye göre filtrele

### 10.12 JLPT Seviyesi Tespiti

**Giriş:** Kelime

**Algoritma:**
1. Sözlükten kelimeyi sorgula
2. JLPT seviyesini al
3. Döndür
