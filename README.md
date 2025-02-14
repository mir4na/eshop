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



</details>

</details>
