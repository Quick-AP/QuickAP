package com.quickap.quickap.controller;

import android.os.Parcel;
import android.os.Parcelable;

import com.quickap.quickap.model.FoodModel;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MenuController implements Parcelable {


    private final HashMap<String, MenuItem> orderMap;

    private static class MenuItem implements Parcelable{
        public int amount;
        public double price;

        protected MenuItem() {
            amount = 0;
            price = 0.0;
        }

        protected MenuItem(Parcel in) {
            amount = in.readInt();
            price = in.readDouble();
        }

        public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
            @Override
            public MenuItem createFromParcel(Parcel in) {
                return new MenuItem(in);
            }

            @Override
            public MenuItem[] newArray(int size) {
                return new MenuItem[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(amount);
            dest.writeDouble(price);
        }
    }


    protected MenuController(Parcel in) {
        this.orderMap = new HashMap<>();
        int entryCount = in.readInt();
        for (int i = 0; i < entryCount; i++) {
            String foodID = in.readString();
            MenuItem menuItem = in.readParcelable(MenuItem.class.getClassLoader());
            this.orderMap.put(foodID, menuItem);
        }

    }

    public static final Creator<MenuController> CREATOR = new Creator<MenuController>() {
        @Override
        public MenuController createFromParcel(Parcel in) {
            return new MenuController(in);
        }

        @Override
        public MenuController[] newArray(int size) {
            return new MenuController[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderMap.size());

        for (Map.Entry<String, MenuItem> entry: orderMap.entrySet()) {
            dest.writeString(entry.getKey());
            MenuItem menuItem = entry.getValue();
            dest.writeParcelable(menuItem, flags);
        }
    }


    public MenuController() {
        orderMap = new HashMap<>();
    }

    public void setFood(FoodModel food, int amount) {
        // if food does not exist in order yet
        if (!orderMap.containsKey(food.getFoodID())) {
            MenuItem item = new MenuItem();
            item.amount = amount;
            item.price = food.getPrice();
            orderMap.put(food.getFoodID(), item);
        } else {
            Objects.requireNonNull(orderMap.get(food.getFoodID())).amount = amount;
        }
    }

    public void addFood(FoodModel food) {
        // if food does not exist in order yet
        if (!orderMap.containsKey(food.getFoodID())) {
            MenuItem item = new MenuItem();
            item.amount = 1;
            item.price = food.getPrice();
            orderMap.put(food.getFoodID(), item);
        } else {
            Objects.requireNonNull(orderMap.get(food.getFoodID())).amount += 1;
        }
    }

    /**
     * Remove food and return if food is deleted.
     * @param food food to be removed.
     * @return boolean indicating food is deleted by the order.
     */
    public boolean removeFood(FoodModel food) {
        if (orderMap.containsKey(food.getFoodID())) {
            MenuItem item = Objects.requireNonNull(orderMap.get(food.getFoodID()));
            item.amount -= 1;
            if (item.amount <= 0) {
                orderMap.remove(food.getFoodID());
                return true;
            }
        }
        return false;
    }

    public double getTotalPrice() {
        double totalPrice = 0.0;

        for (MenuItem item: orderMap.values()) {
            totalPrice += item.price * item.amount;
        }

        return totalPrice;
    }

    public int getAmountOfFood(String foodID) {
        if (orderMap.containsKey(foodID))
            return Objects.requireNonNull(orderMap.get(foodID)).amount;
        else
            return 0;
    }

    public String getFoodListJson() {
        StringBuilder foodListJson = new StringBuilder();
        foodListJson.append("[");
        for (Map.Entry<String, MenuItem> entry: orderMap.entrySet()) {
            for (int i = 0; i < entry.getValue().amount; i++) {
                foodListJson.append("\"");
                foodListJson.append(entry.getKey());
                foodListJson.append("\",");
            }
        }
        foodListJson.setCharAt(foodListJson.length()-1, ']');
        return foodListJson.toString();
    }


}
