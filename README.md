Nama: Muhammad Afwan Hafizh\
NPM: 2306208855\
Kelas: Pemrograman Lanjut B

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

</details>
