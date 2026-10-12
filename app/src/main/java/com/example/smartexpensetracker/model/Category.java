package com.example.smartexpensetracker.model;

public class Category {

    private String name;
    private String icon;

    public Category(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }
}