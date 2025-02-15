Nama: Muhammad Afwan Hafizh\
NPM: 2306208855\
Kelas: Pemrograman Lanjut B

<details>
    <summary><b>Tutorial 1</b></summary>

## Reflection 1

Pada tutorial 1 ini, saya menggunakan Spring Boot untuk pertama kalinya. Spring Boot memanfaatkan Java sebagai bahasa pemrograman sehingga memudahkan saya yang telah mempelajari Java di mata kuliah DDP 2 untuk membaca dan membuat kode. Spring Boot mengadopsi arsitektur MVC (Model-View-Controller) sebagai alur datanya, dengan komponen-komponen seperti Model (misalnya, Product yang merepresentasikan data produk seperti productId, productName, dan productQuantity), Repository (seperti ProductRepository yang mengelola akses data untuk membuat, membaca, dan menghapus produk), Service (seperti ProductServiceImpl yang mengatur logika bisnisnya), dan Controller (menerima permintaan pengguna, berinteraksi dengan Service, dan mengembalikan respons ke View/Templates (Thymeleaf).

Di tutorial kali ini, saya juga belajar dalam menerapkan clean code principle dan secure coding. Beberapa clean code principle yang diterapkan pada code ini.

1. Single Responsibility Principle (SRP)
    - Kelas seperti Product, ProductRepository, ProductServiceImpl, dan ProductController memiliki fungsi utamanya masing-masing.

2. Meaningful Names
    - Nama kelas, method, dan variabel cukup deskriptif, seperti ProductRepository.create(), ProductService.findAll(), dan ProductController.createProductPage(), sehingga mudah dipahami.

3. Tidak ada unused variable dan unused library
    - Semua variabel yang diinisialisasi serta library yang di-import, masing-masing dipakai dan dimanfaatkan fungsinya.

4. Don’t Repeat Yourself (DRY)
    - Penggunaan @Getter dan @Setter dari Lombok mengurangi kode berulang (boilerplate) dalam model Product.

Selain clean code principle, pada code ini terdapat juga penerapan secure coding. Contohnya, seperti berikut.

1. Penggunaan UUID sebagai identifier model Product
    - ID produk dihasilkan menggunakan UUID.randomUUID(), sehingga mengurangi kemungkinan resource path dapat di-bruteforce.

2. Output yang di-encode
    - Thymeleaf secara otomatis melakukan escaping pada HTML dalam template sehingga mengurangi risiko serangan XSS (Cross-Site Scripting).

3. Validasi Input
    - Ketika hendak melakukan edit nama atau kuantitas produk, service memeriksa apakah jumlah produk (productQuantity) bernilai negatif (productQuantity < 0) dan memastikan nama produk tidak bernilai null.

Namun, ada beberapa kesalahan pada code yang seharusnya dapat diperbaiki. Kesalahan-kesalahan tersebut mencakup hal berikut:

1. 

</details>
