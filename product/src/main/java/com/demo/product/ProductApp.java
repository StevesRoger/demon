package com.demo.product;

import com.demo.product.domain.entity.Photo;
import com.demo.product.domain.entity.Product;
import com.demo.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;


@SpringBootApplication
public class ProductApp implements CommandLineRunner {

    @Autowired
    private ProductRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(ProductApp.class, args);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 2; i++) {
            Product product = new Product();
            product.setTitle("Product " + System.currentTimeMillis());
            product.setDesc("Test des");
            product.setPrice(10.0);
            product.getPhotos().add(new Photo("https://unikneeds.com/wp-content/uploads/2017/02/ecommerce_is_not_just_for_physical_products_cover_image.jpg", product));
            product.getPhotos().add(new Photo("https://cdn.shopify.com/s/files/1/0070/7032/files/how-to-price-a-product.jpg?v=1611727768&width=1024", product));
            repository.save(product);
        }
    }
}
