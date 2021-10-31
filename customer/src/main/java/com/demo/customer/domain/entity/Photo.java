package com.demo.customer.domain.entity;

import com.demo.customer.domain.SerializeCloneable;

import javax.persistence.*;

@Entity
@Table(name = "photo")
public class Photo implements SerializeCloneable {

    private static final long serialVersionUID = 4045410564332646586L;

    private Integer id;
    private String mineType;
    private String name;
    private String content;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "mine_type")
    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mimeType) {
        this.mineType = mimeType;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "content", columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
