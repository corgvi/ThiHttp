package com.example.thihttp.model;

import java.io.Serializable;

public class Moto implements Serializable {
    private String createdAt;
    private String name;
    private String image;
    private String color;
    private int price;
    private String id;

    public Moto() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Moto(String name, String image, int price, String color) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.color = color;
    }

    public Moto(String createdAt, String name, String image, String color, int price, String id) {
        this.createdAt = createdAt;
        this.name = name;
        this.image = image;
        this.color = color;
        this.price = price;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Moto{" +
                "createdAt='" + createdAt + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", color='" + color + '\'' +
                ", price=" + price +
                ", id=" + id +
                '}';
    }
}
