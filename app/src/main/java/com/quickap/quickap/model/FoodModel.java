package com.quickap.quickap.model;

public class FoodModel {

    private final String foodID;
    private final String foodType;
    private final Integer foodImageID; // for local image
    private final String foodURL; // for remote image
    private final String name;
    private final double price;
    private final boolean isOnline; // flags if food image are online

    public FoodModel(String foodID, String foodType, Integer foodImageID, String name, double price) {
        this.foodID = foodID;
        this.foodType = foodType;
        this.foodImageID = foodImageID;
        this.name = name;
        this.price = price;
        this.foodURL = null;
        this.isOnline = false;
    }

    public FoodModel(String foodID, String foodType, String foodURL, String name, double price) {
        this.foodID = foodID;
        this.foodType = foodType;
        this.foodURL = foodURL;
        this.name = name;
        this.price = price;
        this.foodImageID = null;
        this.isOnline = true;
    }

    public String getFoodID() {
        return foodID;
    }

    public Integer getFoodImageID() {
        return foodImageID;
    }

    public String getName() {
        return name;
    }

    public String getFoodType() {
        return foodType;
    }

    public double getPrice() {
        return price;
    }

    public String getFoodURL() {
        return foodURL;
    }

    public boolean isOnline() {
        return isOnline;
    }
}
