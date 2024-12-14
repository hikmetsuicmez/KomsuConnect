# KomşuConnect 🚀

KomşuConnect, mahallenizdeki işletmelere kolay erişim sağlamak ve onlardan alışveriş yapmanıza olanak tanıyan yenilikçi bir platformdur. Kullanıcılar, mahallelerindeki işletmeleri filtreleyebilir, işletmelerin ürünlerini inceleyebilir, sepete ekleyebilir ve kolaylıkla sipariş verebilir.

## 📖 İçindekiler

- [Özellikler](#özellikler)
- [Ekran Görüntüleri](#ekran-görüntüleri)
- [Kullanılan Teknolojiler](#kullanılan-teknolojiler)
- [Kurulum](#kurulum)
- [API Kullanımı](#api-kullanımı)

## ✨ Özellikler

- **Güvenli Giriş:** Güvenli bir şekilde kayıt olun ve giriş yapın.
- 🔍 **Mahalle Filtreleme:** Mahallenizdeki işletmeleri kolayca filtreleyin.
- 🛒 **Sepet Yönetimi:** Ürünleri sepete ekleyin, miktarları belirleyin ve sipariş verin.
- 💬 **Mesajlaşma:** İşletme sahipleriyle iletişim kurun.
- ⭐ **Değerlendirme:** İşletmelere puan verin ve yorum yapın.
- 📜 **Sipariş Geçmişi:** Daha önce verdiğiniz siparişleri görüntüleyin.
- **Puan sistemi:** Beğendiğiniz işletmeleri puanlamayı unutmayın.
- **Bildirimler:** Puan veya mesaj durumunda site içerisinde ve e-postadan bildirim alın.
- **Kullanıcı ve işletme sahibi rolleri:** İşletmeleri keşfedin veya işletme hesabı oluşturun.

## 📸 Ekran Görüntüleri

### Giriş Sayfası
![image](https://github.com/user-attachments/assets/8c4b6d44-666d-487a-8db0-854bc0873402)

### Kayıt Sayfası
![image](https://github.com/user-attachments/assets/76f151d0-f9e6-47fc-ad97-a1d523160c8a)
![image](https://github.com/user-attachments/assets/455a95f5-5f78-4656-8d3c-6104c68fd1ad)


### Anasayfa
![image](https://github.com/user-attachments/assets/d7d3f93c-7c5d-403b-9941-7141591d737a)


### Mahalle Filtreleme
![image](https://github.com/user-attachments/assets/14e4131e-8f96-44f5-8634-57516ef9d900)

### Sepet Yönetimi
![image](https://github.com/user-attachments/assets/f87c58d2-7feb-474a-85cc-518f1c5513d8)


### İşletme Detayı
![image](https://github.com/user-attachments/assets/d87a40d3-378f-4a53-a4dd-c1044b7ef9d8)


## 🛠 Kullanılan Teknolojiler

- **Frontend:** React, CSS
- **Backend:** Spring Boot, Hibernate
- **Veritabanı:** MySQL
- **Authentication:** JWT (JSON Web Tokens)
- **Diğer:** Maven, Lombok, SwaggerUI

- ## Proje Yapısı

### Backend Proje Yapısı
```plaintext
src
├── main
│   ├── java
│   │   └── com.hikmetsuicmez.komsu_connect
│   │       ├── config
│   │       ├── controller
│   │       ├── entity
│   │       ├── enums
│   │       ├── exception
│   │       ├── handler
│   │       ├── mapper
│   │       ├── repository
│   │       ├── request
│   │       ├── response
│   │       ├── security
│   │       ├── service
│   │       └── KomsuConnectApplication.java
│   ├── resources
│   │   └── application.properties
├── test
│   ├── java
│   │   └── com.hikmetsuicmez.komsu_connect
│   │       ├── AuthControllerTest.java
│   │       ├── BusinessProfileControllerTest.java
│   │       ├── FavoriteControllerTest.java
│   │       ├── MessageControllerTest.java
│   │       ├── NotificationControllerTest.java
│   │       ├── ProductControllerTest.java
│   │       └── UserControllerTest.java

```

## ✨ Özellikler

- **config:** Uygulama konfigürasyon dosyalarını içerir.
- 📜 **controller:** API uç noktalarını kontrol eder.
- 📜 **entity:** Veritabanı modellerini içerir.
- 📜 **repository:** Veritabanı işlemlerini gerçekleştiren sınıfları içerir.
- 📜 **service:** İş mantığının bulunduğu katmandır.
- 📜 **mapper:** Veritabanı ve response nesneleri arasında dönüşüm yapar.
- 📜 **response & request:** API'den dönen/verilen veri formatlarını belirler.
- 📜 **security:** Uygulama güvenliği ile ilgili konfigürasyon ve işlemleri içerir.
- 📜 **handler:** Uygulama hatalarını yakalar.

### Frontend Proje Yapısı
```plaintext
├── public
│   ├── index.html
│   └── favicon.ico
├── src
│   ├── api
│   │   └── Api.js           # API çağrıları için ortak fonksiyonlar
│   ├── components
│   │   ├── common           # Yeniden kullanılabilir bileşenler
│   │   ├── pages            # Sayfalar (route'larla eşleşir)
│   │   └── styles           # Stil dosyaları (CSS)
│   ├── context
│   │   └── AuthContext.js   # Kimlik doğrulama ve oturum yönetimi
│   ├── App.js               # Uygulama giriş noktası
│   ├── App.test.js          # Test dosyaları
│   ├── index.js             # React DOM render işlemi
│   ├── logo.svg             # Logo dosyası
│   ├── reportWebVitals.js   # Performans ölçümleme
│   └── setupTests.js        # Jest test ayarları
├── .gitignore               # Git ignorasyonu
├── package.json             # Proje bağımlılıkları
├── package-lock.json        # Sabit bağımlılık sürümleri
└── README.md                # Proje dokümantasyonu
```

# KomşuConnect - Kurulum ve Çalıştırma Kılavuzu

Bu doküman, KomşuConnect projesinin hem **backend** hem de **frontend** kısımlarının kurulum ve çalıştırma adımlarını içermektedir.

---

## Gereksinimler

**Backend için:**
- Java 17 veya üzeri
- Maven 3.6 veya üzeri
- MySQL (veya başka bir ilişkisel veritabanı)
- IDE (IntelliJ IDEA, Eclipse vb.)

**Frontend için:**
- Node.js 16 veya üzeri
- NPM 8 veya üzeri (Node.js ile birlikte gelir)
- Tarayıcı (Google Chrome, Firefox vb.)

---

## Backend Kurulum ve Çalıştırma

1. **Proje Deposu Klonlama**
   ```bash
   git clone https://github.com/username/komsuconnect-backend.git
   cd komsuconnect-backend
   ```
2. **Veritabanı Ayarları**
    - MySQL'de bir veritabanı oluşturun
    - src/main/resources/application.properties dosyasını açın ve bilgileri düzenleyin:
  ```bash
        spring.datasource.url=jdbc:mysql://localhost:3306/?
        spring.datasource.username=root
        spring.datasource.password=
        spring.jpa.hibernate.ddl-auto=update
  ```
3. **Bağımlılıkları İndir ve Uygulamayı Çalıştır:**
     ```bash
     - mvn clean install
     - mvn spring-boot:run
     ```
4. API Testi
   - Backend başarıyla çalıştırıldığında http://localhost:8080 adresinden erişebilirsiniz.
   - API belgeleri için http://localhost:8080/swagger-ui.html adresine gidin.
    
## Frontend Kurulum ve Çalıştırma
1. **Proje Deposu Klonlama**
   ```bash
   git clone https://github.com/username/komsuconnect-backend.git
   cd komsuconnect-frontend
   ```
2. **Bağımlılıkları İndir **
     ```bash
     - npm install
     ```
3. API Adresini Ayarla
   - src/api/Api.js dosyasını açın ve backend API URL'sini düzenleyin:
     ```bash
     const API_BASE_URL = "http://localhost:8080";
     export default API_BASE_URL;
     ```
4. ** Uygulamayı Çalıştır **
     ```bash
     - npm start
     ```
  

