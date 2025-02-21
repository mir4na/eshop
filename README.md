Nama: Muhammad Afwan Hafizh\
NPM: 2306208855\
Kelas: Pemrograman Lanjut B

URL Deployment: https://unfair-smelt-adpro-eshop-mirana-9288cf1f.koyeb.app/

## Advanced Programming

<details>
    <summary><b>Tutorial 1</b></summary>

___

<details>
    <summary><b>Reflection 1</b></summary>
    
___
_You already implemented two new features using Spring Boot. Check again your source code and evaluate the coding standards that you have learned in this module. Write clean code principles and secure coding practices that have been applied to your code.  If you find any mistake in your source code, please explain how to improve your code. Please write your reflection inside the repository's README.md file._
___

Pada tutorial 1 ini, saya menggunakan Spring Boot untuk pertama kalinya. Spring Boot memanfaatkan Java sebagai bahasa pemrograman sehingga memudahkan saya yang telah mempelajari Java di mata kuliah DDP 2 untuk membaca dan membuat kode. Spring Boot mengadopsi arsitektur MVC (Model-View-Controller) sebagai alur datanya, dengan komponen-komponen seperti Model (misalnya, Product yang merepresentasikan data produk seperti productId, productName, dan productQuantity), Repository (seperti ProductRepository yang mengelola akses data untuk membuat, membaca, dan menghapus produk), Service (seperti ProductServiceImpl yang mengatur logika bisnisnya), dan Controller (menerima permintaan pengguna, berinteraksi dengan Service, dan mengembalikan respons ke View/Templates (Thymeleaf).

Di tutorial kali ini, saya juga belajar dalam menerapkan clean code principle dan secure coding. Salah satu clean code principle yang telah diaplikasikan adalah Meaningful Names. Contoh:

```
...
    @GetMapping("/edit")
    public String updateProductPage(@RequestParam String productId, Model model) {
        Product product = service.getId(productId);
        model.addAttribute("product", product);
        return "EditProduct";
    }

    @PostMapping("/edit")
    public String editProductPost(@ModelAttribute Product product) {
        service.update(product.getProductId(), product);
        return "redirect:/product/list";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam String productId) {
        service.delete(productId);
        return "redirect:list";
    }
...
```
Pada potongan code ini, terlihat bahwa masing-masing function dapat teridentifikasi fungsinya dengan hanya membaca namanya tanpa harus memberikan penjelasan melalui comment. Contohnya seperti function updateProductPage, yaitu handler method yang menangani permintaan GET ke endpoint /edit, yang digunakan untuk menampilkan halaman edit produk. Parameter @RequestParam String productId mengambil ID produk dari URL, lalu service.getId(productId) digunakan untuk mengambil data produk dari database atau sumber lain. Objek produk tersebut kemudian ditambahkan ke model dengan model.addAttribute("product", product), sehingga bisa diakses di halaman tampilan. Metode ini mengembalikan string "EditProduct" yang merupakan nama template atau halaman HTML yang akan ditampilkan, misalnya EditProduct.html dalam folder templates.

Saya juga mengaplikasikan clean code principle lainnya seperti functions (contoh methdo findAll yang ada pada services), Object and Data Structure (contoh pada model Product), dan Error Handling pada EditProduct.html yang mencegah adanya input null atau format yang tidak sesuai pada kolom productQuantity dan productName.

Selain pengaplikasian clean code principle, pada code ini terdapat juga penerapan secure coding. Contohnya, seperti berikut.

1. Penggunaan UUID sebagai identifier model Product
   
```
...
public Product create(Product product) {
        product.setProductId(UUID.randomUUID().toString());
        productData.add(product);
        return product;
    }
...
```

ID produk dihasilkan menggunakan UUID.randomUUID(), sehingga mengurangi kemungkinan resource path dapat diprediksi.

2. Output yang di-encode
    - Thymeleaf secara otomatis melakukan escaping pada HTML dalam template sehingga mengurangi risiko serangan XSS (Cross-Site Scripting).

3. Validasi Input
   
```
public void update(String productId, Product updatedProduct) {
        Product product = getId(productId);
        if (updatedProduct.getProductName() != null) {
            product.setProductName(updatedProduct.getProductName());
        }

        if (updatedProduct.getProductQuantity() > 0) {
            product.setProductQuantity(updatedProduct.getProductQuantity());
        }
    }
```

Ketika hendak melakukan edit nama atau kuantitas produk, service memeriksa apakah jumlah produk (productQuantity) bernilai negatif (productQuantity <= 0) dan memastikan nama produk tidak bernilai null.

"_If you find any mistake in your source code, please explain how to improve your code._"

Menurut saya, dari kedua fitur yang saya implementasikan, terdapat beberapa hal yang perlu untuk ditingkatkan, Salah satu contohnya adalah memberikan **Logging** setiap adanya pertukaran/pergantian data (pada edit atau delete). Berikut contoh codenya.
```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public void delete(String productId) {
        logger.info("Attempting to delete product with ID: {}", productId);
        try {
            productRepository.delete(productId);
            logger.info("Product deleted successfully.");
        } catch (Exception e) {
            logger.error("Failed to delete product: {}", e.getMessage());
        }
    }
}
```

Potongan code di atas adalah contoh dari code improvementnya. Dengan adanya logger, saya dapat memantau/mengetahui adanya aktivitas yang terjadi sehingga saya bisa memastikan keamanan tiap terjadinya pertukaran/pergantian data.

Contoh lainnya, Tambahan validasi input ketika melakukan update pada nama product. Berikut contoh codenya.

```
if (updatedProduct.getProductName() != null && !updatedProduct.getProductName().trim().isEmpty()) {
    product.setProductName(updatedProduct.getProductName());
}
```

 Dengan adanya validasi input kita dapat memastikan bahwa productName tidak hanya tidak null, tetapi juga tidak kosong atau mengandung karakter yang tidak valid.

 Dengan melakukan refleksi 1, saya menyadari bahwa menerapkan clean code principles dan secure coding sangatlah penting. Clean code membuat kode mudah dibaca, dipahami, dan dikembangkan sehingga meningkatkan efisiensi pengembangan dan mengurangi risiko bug. Sementara itu, secure coding melindungi aplikasi kita dari berbagai kerentanan keamanan, seperti broken access control, injection, dan lainnya yang pastinya dapat mengancam keamanan data pengguna.

</details>

<details>
    <summary><b>Reflection 2</b></summary>
    
___
_After writing the unit test, how do you feel? How many unit tests should be made in a class? How to make sure that our unit tests are enough to verify our program? It would be good if you learned about code coverage. Code coverage is a metric that can help you understand how much of your source is tested. If you have 100% code coverage, does that mean your code has no bugs or errors?_

_Suppose that after writing the CreateProductFunctionalTest.java along with the corresponding test case, you were asked to create another functional test suite that verifies the number of items in the product list. You decided to create a new Java class similar to the prior functional test suites with the same setup procedures and instance variables._

_What do you think about the cleanliness of the code of the new functional test suite? Will the new code reduce the code quality? Identify the potential clean code issues, explain the reasons, and suggest possible improvements to make the code cleaner! Please write your reflection inside the repository's README.md file._
___

Setelah membuat dan menjalankan unit test pada kode yang telah dibuat, saya mendapatkan beberapa pembelajaran penting, yaitu:

**Jumlah dan Kualitas Unit Test**

Dalam implementasi yang saya lakukan, semua test berhasil dijalankan dengan baik (100% passed). Unit test yang dibuat telah mencakup berbagai skenario seperti:

- Test pembuatan dan pencarian produk (testCreateAndFind)
- Test untuk kondisi list produk kosong (testFindAllIfEmpty)
- Test untuk multiple produk (testFindAllIfMoreThanOneProduct)
- Test update produk dengan berbagai kondisi (valid dan invalid)
- Test penghapusan produk

Mengenai pertanyaan "_How many unit tests should be made in a class?_", menurut saya tidak ada jumlah pasti yang dapat dijadikan acuan. Sebab, menurut saya yang terpenting dari adanya unit test, unit test harus mencakup beberapa hal berikut:

- Mencakup semua fitur utama
- Menguji berbagai skenario (positif dan negatif)
- Memverifikasi semua business requirement
- Menguji edge cases dan error handling

**Code Coverage**

Dalam project ini, saya berhasil mencapai code coverage 100%, yang berarti semua baris kode telah dieksekusi oleh test. Namun, saya memahami bahwa code coverage 100% tidak menjamin kode bebas dari bug atau error. Hal ini disebabkan oleh beberapa hal berikut:

- Code coverage hanya mengukur baris kode yang dieksekusi, bukan kebenaran logika
- Code coverage tidak menjamin
  a. Kesalahan dalam implementasi requirement
  b. Edge cases yang belum terpikirkan
  c. Masalah integrasi antar komponen
  d. Isu performa
  e. Masalah konkurensi

**Analisis Clean Code pada Functional Test**

Menurut saya, jika melihat struktur functional test di CreateProductFunctionalTest.java dan kemungkinan penambahan test suite baru, ada beberapa hal-hal yang kemungkinan dapat ditingkatkan, yaitu:

1. Adanya duplikasi kode
   - Setup code (@BeforeEach dan konfigurasi server) terduplikasi di setiap test class
   - Beberapa assertions dan utility methods ditulis berulang kali

2. Abstraksi yang Kurang Optimal
   - Interaksi dengan web element tersebar di berbagai method
   - Logic pembuatan URL diulang-ulang

Contoh perbaikan yang dapat dilakukan:

1. Membuat Base Test Class
```
public abstract class BaseProductFunctionalTest {
    @LocalServerPort
    protected int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    protected String testBaseUrl;

    protected String baseUrl;
    
    protected String buildUrl(String endpoint) {
        return String.format("%s:%d%s", testBaseUrl, serverPort, endpoint);
    }
}
```

2. Implementasi Page Object Pattern

```
public class ProductPage {
    private final ChromeDriver driver;
    
    public ProductPage(ChromeDriver driver) {
        this.driver = driver;
    }
    
    public void createProduct(String name, int quantity) {
        driver.findElement(By.id("nameInput")).sendKeys(name);
        driver.findElement(By.id("quantityInput")).sendKeys(String.valueOf(quantity));
        driver.findElement(By.tagName("button")).click();
    }
    
    public int getProductCount() {
        return driver.findElements(By.className("product-item")).size();
    }
}
```

3. Standardisasi Helper Methods

```
public class TestHelper {
    public static void verifyPageTitle(ChromeDriver driver, String expectedTitle) {
        assertEquals(expectedTitle, driver.getTitle());
    }
    
    public static void verifyProductExists(ChromeDriver driver, String name, int quantity) {
        assertTrue(driver.findElements(By.xpath("//*[contains(text(), '" + name + "')]")).size() > 0);
        assertTrue(driver.findElements(By.xpath("//*[contains(text(), '" + quantity + "')]")).size() > 0);
    }
}
```

Dengan menerapkan contoh perbaikan di atas, maka code akan menjadi:

1. Lebih mudah dimaintain karena mengurangi duplikasi
2. Lebih mudah dibaca dan dipahami
3. Lebih robust dalam penanganan web elements

Dengan merefleksikan tutorial mengenai functional test dan unit test, artinya meskipun semua test berhasil dijalankan dengan code coverage 100%, masih ada ruang untuk peningkatan dalam hal clean code dan maintainability karena tujuan utama dari testing bukan hanya mencapai coverage 100%, tetapi memastikan kualitas dan keandalan aplikasi secara keseluruhan.

</details>

___

</details>

<details>
    <summary><b>Tutorial 2</b></summary>

___
_You have implemented a CI/CD process that automatically runs the test suites, analyzes code quality, and deploys to a PaaS. Try to answer the following questions in order to reflect on your attempt completing the tutorial and exercise._

1. _List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them._
2. _Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)!_
___

Ketika saya melakukan scannning dengan SonarCloud, saya mendapatkan beberapa issue pada maintainability. Berikut beberapa issue yang saya perbaiki.

1. Group Dependency
   
   <img src="https://github.com/user-attachments/assets/b7209193-d049-49e6-8657-cc6e5e31949e" width="600">

   Disini, saya memindahkan dependensi bagian TestRuntimeOnly dari baris 49 ke baris 58 dan menambahkan beberapa baris baru yang ditandai dengan tanda "+". Perubahan ini saya lakukan untuk membuat group dependency menjadi lebih terstruktur sehingga readability pada code ini akan meningkat dan memudahkan untuk pemeliharaan kode.

2. Field Injection

   <img src="https://github.com/user-attachments/assets/fca036aa-db53-410c-be6f-210445e18d68" width="600">

   Disini, Saya mengubah cara injeksi dependensi dari field injection (@Autowired) menjadi constructor injection. Saya menghapus anotasi @Autowired pada field ProductService dan menggantinya dengan membuat constructor explicit yang menerima parameter ProductService. Selain itu, saya juga menambahkan modifier final pada field service untuk memastikan immutability, kemudian menginisialisasi field tersebut melalui constructor dengan this.service = service. Saya yakin perubahan ini membuat kode menjadi lebih aman dan testable.

   Hal ini berlaku juga dengan code ini.

    <img src="https://github.com/user-attachments/assets/16838c68-8326-4c46-bb13-a59f5667f025" width="600">

3. Fix Assertion

   Code 1 (sebelum diperbaiki):
   ```java
    package id.ac.ui.cs.advprog.eshop;
    import org.junit.jupiter.api.Test;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.ApplicationContext;
    import static org.junit.jupiter.api.Assertions.assertNotNull;
    @SpringBootTest
    class EshopApplicationTests {
        @Autowired
        private ApplicationContext applicationContext;
        @Test
        void contextLoads() {
            assertNotNull(applicationContext);
        }
        @Test
        void testMethodStartApplication() {
            EshopApplication.main(new String[] {});
        }
    }
   ```

   Code 2 (setelah diperbaiki):

   ```java
    package id.ac.ui.cs.advprog.eshop;
    import org.junit.jupiter.api.Test;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.ApplicationContext;
    
    import static org.junit.jupiter.api.Assertions.assertNotNull;
    
    @SpringBootTest
    class EshopApplicationTests {
    
        @Autowired
        private ApplicationContext applicationContext;
    
        @Test
        void contextLoads() {
           EshopApplication.main(new String[] {});
           assertNotNull(applicationContext);
        }
    }
   ```

   Disini, Saya menggabungkan dua test method yang sebenarnya memiliki tujuan yang sama. Saya menggabungkan method testMethodStartApplication() ke dalam method contextLoads() karena keduanya sama-sama menguji inisialisasi aplikasi Spring. Dalam implementasi baru, saya memastikan aplikasi dapat dijalankan dengan memanggil EshopApplication.main() terlebih dahulu, kemudian memverifikasi bahwa applicationContext berhasil diinisialisasi dengan assertNotNull. Dengan perubahan ini, maka code dapat menghindari redundancy dalam testing.
   
4. Unnecessary Exception Throws

   <img src="https://github.com/user-attachments/assets/29acad18-3b01-4609-ad5c-c6755ad68fd3" width="500">
   
   Disini, Saya menghapus throws Exception yang tidak diperlukan dari beberapa method test. Saya menghilangkan throws Exception pada method pageTitle_isCorrect(), message_createProduct_isCorrect(), dan createProduct_isCorrect() karena assertion dalam JUnit sebenarnya sudah menangani exception secara otomatis sehingga tidak perlu mendeklarasikannya secara eksplisit. Perubahan ini membuat kode menjadi lebih bersih dan menghindari penanganan exception yang tidak perlu, sesuai dengan best practice dalam penulisan unit test.

5. Unnecessary Modifier

   <img src="https://github.com/user-attachments/assets/cb4f91f7-ddd6-446c-8a03-bcbdf5ee2a53" width="600">
   
   Disini, Saya menghapus modifier public yang tidak diperlukan dari deklarasi method-methodnya. Penghapusan ini saya lakukan karena secara default, method-method dalam interface sudah bersifat public, sehingga penulisan modifier public menjadi redundant. Saya menghapus modifier public dari method create(), getId(), update(), delete(), dan findAll() agar kode menjadi lebih bersih dan sesuai dengan best practice dalam penulisan interface Java.

6. Encapsulation
  
   <img src="https://github.com/user-attachments/assets/c45896b3-18e0-447f-9129-a245add6eac4" width="500">
   
   Disini, saya memodifikasi pada file CreateProductFunctionalTest.java yang terletak di direktori src/test/java/id/ac/ui/cs/advprog/eshop/functional/. Saya mengubah deklarasi kelas CreateProductFunctionalTest dari public menjadi default (tanpa modifier), yang berarti kelas tersebut hanya dapat diakses dalam package yang sama. Perubahan ini mungkin dilakukan untuk membatasi visibilitas kelas dan mengontrol akses ke kelas tersebut, sehingga hanya kelas-kelas dalam paket yang sama yang dapat menggunakannya. Hal ini dapat meningkatkan maintainability dengan mengurangi kemungkinan kelas tersebut diakses atau diubah oleh kode di luar package-nya.

Setelah me-resolve issue maintainability yang ada, saya melihat kembali implementasi CI/CD workflows pada repository saya. Menurut saya, workflow yang telah dibuat sudah memenuhi definisi dari Continuous Integration (CI) dan Continuous Deployment (CD). Berikut alasannya:

- Menurut saya, workflow ci.yml dan build.yml telah dirancang dengan baik sebagai penerapan Continuous Integration. Setiap kali ada perubahan kode yang di-push atau pull request, workflow ini secara otomatis menjalankan ./gradlew test dan menganalisis kode menggunakan SonarCloud. Ini memastikan bahwa setiap perubahan kode diintegrasikan dengan kode utama dan diverifikasi melalui tes otomatis (scanning). Dengan demikian, saya yakin risiko adanya bug dan maintainability issue dapat diminimalisir.

- Lalu, pada workflow deploy.yml, workflow ini menunjukkan bahwa adanya implementasi Continuous Deployment (CD) yang baik. Workflow ini memeriksa status dari workflow CI (ci.yml), analisis keamanan (scorecard.yml), dan analisis kode (build.yml). Jika semua workflow tersebut berhasil, aplikasi akan otomatis di-redeploy ke Koyeb menggunakan Koyeb CLI. Ini memastikan bahwa setiap perubahan kode yang telah lolos tes, maka akan langsung diterapkan ke production.

- Selain itu, saya juga memerhatikan bahwa workflow deploy.yml menunjukkan integrasi yang baik antara berbagai tahapan CI/CD. Dengan memeriksa status dari semua workflow yang relevan sebelum melakukan deployment, saya yakin bahwa hanya kode yang telah lolos semua tahapan test yang akan di-deploy sehingga ini meminimalisir risiko terjadinya bug dan memastikan kualitas kode yang konsisten.

Secara keseluruhan, saya berpendapat bahwa implementasi ini sudah memenuhi prinsip-prinsip dasar CI/CD. Namun, pastinya saya juga menyadari bahwa saya perlu menyesuaikan workflow ini sesuai dengan kebutuhan dan perkembangan pada proyek.

</details>
