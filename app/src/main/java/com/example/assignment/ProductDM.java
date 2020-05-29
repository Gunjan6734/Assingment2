package com.example.assignment;

import android.media.Image;

public class ProductDM {
    private String id, name, description,regular_price,sale_price, color;
    private byte[] product_photo;

    public ProductDM(String id, String name, String description, String regular_price,
                     String sale_price, byte[] product_photo, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.regular_price = regular_price;
        this.sale_price = sale_price;
        this.product_photo = product_photo;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegular_price() {
        return regular_price;
    }

    public void setRegular_price(String regular_price) {
        this.regular_price = regular_price;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public byte[] getProduct_photo() {
        return product_photo;
    }

    public void setProduct_photo(byte[] product_photo) {
        this.product_photo = product_photo;
    }
}
