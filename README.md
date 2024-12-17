# KomşuConnect 🚀

KomşuConnect, mahallenizdeki işletmelere kolay erişim sağlamak ve onlardan alışveriş yapmanıza olanak tanıyan yenilikçi bir platformdur. Kullanıcılar, mahallelerindeki işletmeleri filtreleyebilir, işletmelerin ürünlerini inceleyebilir, sepete ekleyebilir ve kolaylıkla sipariş verebilir.

## 📖 İçindekiler

- [Özellikler](#özellikler)
- [Ekran Görüntüleri](#ekran-görüntüleri)
- [Kullanılan Teknolojiler](#kullanılan-teknolojiler)
- [Proje Yapısı](#projeyapısı)
- [API Kullanımı](#api-kullanımı)
- [Kurulum](#kurulum)

## ✨ Özellikler 

- **Güvenli Giriş:** Güvenli bir şekilde kayıt olun ve giriş yapın.
- 🔍 **Mahalle Filtreleme:** Mahallenizdeki işletmeleri kolayca filtreleyin.
- 🛒 **Sepet Yönetimi:** Ürünleri sepete ekleyin, miktarları belirleyin ve sipariş verin.
-  💳 **İyzico Ödeme Entegrasyonu:** Güvenli ve hızlı ödeme işlemleri.
- 💬 **Mesajlaşma:** İşletme sahipleriyle iletişim kurun.
- ⭐ **Değerlendirme:** İşletmelere puan verin ve yorum yapın.
- 📜 **Sipariş Geçmişi:** Daha önce verdiğiniz siparişleri görüntüleyin.
- **Puan sistemi:** Beğendiğiniz işletmeleri puanlamayı unutmayın.
- **Bildirimler:** Puan veya mesaj durumunda site içerisinde ve e-postadan bildirim alın.
- **Kullanıcı ve işletme sahibi rolleri:** İşletmeleri keşfedin veya işletme hesabı oluşturun.

## 📸 Ekran Görüntüleri

### Giriş Sayfası
![image](https://github.com/user-attachments/assets/e460e6ba-a287-4211-bebd-52e986241123)

### Kayıt Sayfası
#### Giriş Yap
![image](https://github.com/user-attachments/assets/38be7cd2-343a-4022-af87-f9f6e6ddc539)

#### Kayıt Ol
##### Kullanıcı
![image](https://github.com/user-attachments/assets/6f54b7b8-6d5a-497d-b9fa-31895e304530)
##### İşletme
![image](https://github.com/user-attachments/assets/3514bbf1-f32d-4197-a8d5-c2017e7ef5ae)

### Anasayfa
![image](https://github.com/user-attachments/assets/c905ebbb-99ad-4875-9540-c229cec78cbd)

### Mahalle Filtreleme
![image](https://github.com/user-attachments/assets/92acb415-9eee-4210-8d47-3712094ee870)
### Sepet Yönetimi
![image](https://github.com/user-attachments/assets/f87c58d2-7feb-474a-85cc-518f1c5513d8)
#### Adres Ekleme
![image](https://github.com/user-attachments/assets/4500eb1d-4efc-4098-b3b0-78753a8eb69d)
#### Ödeme İşlemi
![image](https://github.com/user-attachments/assets/dc6eecb3-1ca0-4535-b50c-0d1a285ee511)
### İşletme Detayı
![image](https://github.com/user-attachments/assets/d87a40d3-378f-4a53-a4dd-c1044b7ef9d8)


---

## 🛠 Kullanılan Teknolojiler

### **Backend**
- **Java Spring Boot**
- **Hibernate (JPA)**
- **MySQL**
- **JWT Authentication**

### **Frontend**
- **React.js**
- **Axios**
- **CSS**

### **Ödeme Entegrasyonu**
- **İyzico API**
  - Ödeme işlemleri ve doğrulama
  - Kullanıcı dostu güvenli ödeme sistemi

---
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

### API Endpointler

| **Controller**              | **HTTP Method** | **Endpoint**                                   | **Açıklama**                                                               |
|-----------------------------|-----------------|----------------------------------------------|------------------------------------------------------------------------------|
| **user-controller**         | POST            | `/api/users/update-mail`                                        | Kullanıcının e-posta adresini günceller.                                     |
|                             | GET             | `/api/users/me`                                                 | Mevcut kullanıcı bilgilerini döner.                                          |
|                             | PUT             | `/api/users/me`                                                 | Oturum açmış kullanıcının bilgilerini günceller.                             |
|                             | GET             | `/api/users`                                                    | Sistemdeki tüm kullanıcıların bilgilerini getirir.                           |
|                             | GET             | `/api/users/{userId}`                                           | Belirtilen `userId`'ye sahip kullanıcının bilgilerini getirir.               |
| **notification-controller** | GET             | `/api/notifications`                                            | Kullanıcının bildirimlerini getirir.                                         |
|                             | PUT             | `/api/notifications/{notificationId}/mark-as-read`              | Belirtilen `notificationId`'ye ait bildirimi okundu olarak işaretler. |
|                             | GET             | `/api/notifications/unread-count`                               | Kullanıcının okunmamış bildirimlerinin sayısını döner.                       |
| **message-controller**      | PUT             | `/api/messages/read/{messageId}`                       | Belirtilen `messageId`'ye ait mesajı okundu olarak işaretler.                |
|                             | POST            | `/api/messages/send/{receiverId}`                      | Belirtilen `receiverId`'ye mesaj gönderir.                                   |
|                             | GET             | `/api/messages/inbox`                                  | Kullanıcının gelen kutusundaki mesajları listeler.                           |
|                             | GET             | `/api/messages/history/{userId}`                       | Belirtilen `userId` ile olan mesaj geçmişini getirir.                        |
|                             | GET             | `/api/messages/conversation/{userId}/{selectedUserId}` | Belirtilen `userId` ve `selectedUserId` arasındaki konuşmayı getirir.        |
|                             | GET             | `/api/messages/conversation-or-create/{userId}/{selectedUserId}` | Eğer konuşma mevcutsa getirir, yoksa yeni bir konuşma başlatır.    |
| **business-profile-controller** | PUT             | `/api/business/products/{productId}`              | Belirtilen `productId`'ye ait ürün bilgisini günceller.                     |
|                             | DELETE          | `/api/business/products/{productId}`              | Belirtilen `productId`'ye ait ürünü siler.                                  |
|                             | POST            | `/api/business/{businessId}/upload-photo`         | Belirtilen `businessId`'ye ait işletme için fotoğraf yükler.                |
|                             | POST            | `/api/business/rate`                              | Bir işletmeye puanlama ve yorum yapma işlemi gerçekleştirir.                |
|                             | POST            | `/api/business/products/{productId}/upload-photo` | Belirtilen `productId`'ye ait ürün için fotoğraf yükler.                    |
|                             | POST            | `/api/business/add-product`                       | Yeni bir ürün ekler.                                                        |
|                             | GET             | `/api/business/{businessId}`                      | Belirtilen `businessId`'ye ait işletme detaylarını döner.                   |
|                             | GET             | `/api/business/{businessId}/products`             | Belirtilen `businessId`'ye ait ürün listesini döner.                        |
|                             | GET             | `/api/business/{businessId}/owner`                | Belirtilen `businessId`'ye ait işletme sahibinin bilgilerini döner.         |
|                             | GET             | `/api/business/{businessId}/average-rating`       | Belirtilen `businessId`'ye ait işletmenin ortalama puanını döner.           |
|                             | GET             | `/api/business/search`                            | İşletmeler arasında arama yapar.                                            |
|                             | GET             | `/api/business/public-businesses`                 | Genel olarak erişilebilir tüm işletme bilgilerini döner.                    |
|                             | GET             | `/api/business/products`                          | Tüm işletmelere ait ürün listesini döner.                                   |
|                             | PUT             | `/api/payments/{paymentId}/status`                | Ödeme durumunu günceller.                                                    |
|                             | GET             | `/api/payments/{paymentId}`                       | Belirtilen ödemenin detayını getirir.                                        |
| **order-controller**        | POST            | `/order`                                          | Yeni bir sipariş oluşturur. Sepetteki ürünleri siparişe çevirir.          |
|                             | POST            | `/order/{orderId}/payment`                        | Belirtilen `orderId` için ödeme işlemini başlatır.                        |
|                             | GET             | `/order/history`                                  | Kullanıcının geçmiş siparişlerini listeler.                               |
| **cart-controller**         | POST            | `/cart/add`                                       | Sepete yeni bir ürün ekler. Ürün ID ve miktar parametreleri alır.         |
|                             | GET             | `/cart`                                           | Kullanıcının mevcut sepetindeki ürünleri listeler.                        |
| **auth-controller**         | POST            | `/auth/register/user`                             | Yeni bir kullanıcı kaydı oluşturur. Kullanıcı bilgileri gereklidir.       |
|                             | POST            | `/auth/register/business`                         | Yeni bir işletme hesabı kaydı oluşturur. İşletme bilgileri gereklidir.    |
|                             | POST            | `/auth/login`                                     | Kullanıcı veya işletme hesabı için giriş yapar. Kullanıcı adı ve şifre alır. |
| **product-controller**      | POST            | `/api/products/{productId}/rate`                  | Belirtilen `productId` için kullanıcı tarafından bir puanlama yapılmasını sağlar. |
|                             | GET             | `/api/products/latest-product`                    | Sistemdeki en son eklenen ürünü getirir.                                     |
| **favorite-controller**     | POST            | `/api/favorites/product/{productId}`              | Belirtilen `productId` ürününü favorilere ekler.                             |
|                             | DELETE          | `/api/favorites/product/{productId}`              | Belirtilen `productId` ürününü favorilerden kaldırır.                        |
|                             | POST            | `/api/favorites/business/{businessId}`            | Belirtilen `businessId` işletmesini favorilere ekler.                        |
|                             | DELETE          | `/api/favorites/business/{businessId}`            | Belirtilen `businessId` işletmesini favorilerden kaldırır.                   |
|                             | GET             | `/api/favorites`                                  | Kullanıcının favori ürünlerini ve işletmelerini listeler.                    |


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
  

