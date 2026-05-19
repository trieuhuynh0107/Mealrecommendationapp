package com.example.mealrecommendationapp.model;

public class User {

    private String username;

    private String gender;

    private int age;

    private int weight;

    private int height;

    public User(
            String username,
            String gender,
            int age,
            int weight,
            int height
    ) {

        this.username = username;

        this.gender = gender;

        this.age = age;

        this.weight = weight;

        this.height = height;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getGender() {

        return gender;
    }

    public void setGender(String gender) {

        this.gender = gender;
    }

    public int getAge() {

        return age;
    }

    public void setAge(int age) {

        this.age = age;
    }

    public int getWeight() {

        return weight;
    }

    public void setWeight(int weight) {

        this.weight = weight;
    }

    public int getHeight() {

        return height;
    }

    public void setHeight(int height) {

        this.height = height;
    }
}