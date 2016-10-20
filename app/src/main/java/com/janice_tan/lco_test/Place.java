package com.janice_tan.lco_test;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by janice on 15/10/16.
 */

public class Place {

    private int id;

    private String name;

    private int rating;

    private String imagePath;

    private Date updatedAt;

    private int category_id;

    private Category category;

    public Place(int id, String name, int rating, String imagePath, Date updatedAt, int category_id) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.imagePath = imagePath;
        this.updatedAt = updatedAt;
        this.category_id = category_id;
    }

    public Place(String name, int rating, String imagePath, Date updatedAt, int category_id) {
        this.name = name;
        this.rating = rating;
        this.imagePath = imagePath;
        this.updatedAt = updatedAt;
        this.category_id = category_id;
    }

    public Place(String name, int rating, String imagePath, int category_id) {
        this.name = name;
        this.rating = rating;
        this.imagePath = imagePath;
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDisplayDate() {

        Date date = this.updatedAt;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return dateFormat.format(date);
    }

}
