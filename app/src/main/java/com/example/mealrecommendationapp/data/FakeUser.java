package com.example.mealrecommendationapp.data;

import com.example.mealrecommendationapp.model.User;

public class FakeUser {

    private static User currentUser =
            new User(
                    "Thuy Hien",
                    "Female",
                    22,
                    47,
                    180
            );

    public static User getCurrentUser() {

        return currentUser;
    }

    public static void updateUser(
            String username,
            String gender,
            int age,
            int weight,
            int height
    ) {

        currentUser.setUsername(username);

        currentUser.setGender(gender);

        currentUser.setAge(age);

        currentUser.setWeight(weight);

        currentUser.setHeight(height);
    }
}