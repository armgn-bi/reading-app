# Reading App

Japonca kitap okuma uygulaması - Samsung Android cihazlar için optimize edilmiş, lightweight native uygulama.

## Özellikler

### Temel Özellikler
- Kitap Yükleme - Kullanıcının kendi yüklediği kitapları okuma
- Cümle Çevirisi - Ekranın üst kısmında seçilen cümlenin çevirisi
- Kelime Bilgisi - Kelime seçildiğinde anlamı ve okunuşu
- Kitap Listesi - Yüklenen kitapları görüntüleme ve yönetme
- Kitap Gruplandırma - Kategori/klasör halinde gruplandırma
- Okuma İlerlemesi - Son okunan konumu kaydetme
- Kelime Defteri - Seçilen kelimeleri kaydetme
- Furigana Desteği - Kanji karakterlerinin okunuşlarını (hiragana) gösterme
- Yazı Boyutu Ayarı - Okuma deneyimi için yazı boyutunu değiştirme
- Koyu/Açık Tema - Göz yormaması için tema seçimi
- Kelime Kartları - Öğrenilen kelimeleri tekrar etme (Anki benzeri)
- Arama - Kitap içinde veya kelime defterinde arama
- Paralel Çeviri Dosyası - Kitapla birlikte çeviri dosyası yükleme (kitap.txt + kitap_tr.txt)
- Çeviri API Entegrasyonu - ML Kit Translation (ücretsiz, offline)
- Dosya Formatları - TXT, PDF, EPUB desteği
- İstatistikler - Okuma süresi, kelime sayısı gibi istatistikler

### Önerilen Ek Özellikler
- Sesli Okuma - Metni sesli okuma (TTS) desteği
- Not Alma - Kitap içinde not ekleme

## Teknoloji Stack

| Katman | Teknoloji |
|--------|-----------|
| Platform | Android (Native) |
| Dil | Kotlin |
| UI | Jetpack Compose |
| Veritabanı | Room (SQLite) |
| Dosya Okuma | Java IO (TXT), PdfBox (PDF), R2Reader (EPUB) |
| TTS | Android TextToSpeech API |
| Çeviri | ML Kit Translation |
| Sözlük | Yerel JSON/SQLite |

## Geliştirme

Detaylı geliştirme planı için [PROJE_GELISTIRME_PLANI.md](PROJE_GELISTIRME_PLANI.md) dosyasına bakın.

### Gereksinimler

- Android Studio Hedgehog (2023.1.1) veya üzeri
- JDK 17 veya 21
- Android SDK API 35
- Min SDK: API 24 (Android 7.0)

### Kurulum

```bash
# Projeyi klonla
git clone https://github.com/yourusername/reading-app.git
cd reading-app

# Android Studio'da aç
# File > Open > reading-app klasörünü seç
```

### Proje Durumu

| Durum | Açıklama |
|-------|----------|
| Başlangıç | Proje henüz oluşturulmadı |
| İlerleme | %0 |

## Lisans

Bu proje [LICENSE](LICENSE) lisansı altında lisanslanmıştır.
