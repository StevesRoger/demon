package com.demo.product.domain.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "photo")
public class Photo extends AbstractEntity {

    private static final long serialVersionUID = 1427432360579823390L;

    private String url;
    private Product product;

    public Photo() {
    }

    public Photo(String url, Product product) {
        this.url = url;
        this.product = product;
    }

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(name = "url", nullable = false)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof Photo && ((Photo) that).getUrl().equals(url);
    }
}
