package com.demo.product.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "policy")
public class Policy extends AbstractEntity {

    private static final long serialVersionUID = -7673701358158734487L;

    private Product product;
    private String owner;

    public Policy() {
    }

    public Policy(Product product, String owner) {
        this.product = product;
        this.owner = owner;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(name = "owner", nullable = false)
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
