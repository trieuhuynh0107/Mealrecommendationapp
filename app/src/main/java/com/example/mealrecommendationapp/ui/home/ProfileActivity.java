package com.example.mealrecommendationapp.ui.home;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealrecommendationapp.R;
import com.example.mealrecommendationapp.data.FakeUser;
import com.example.mealrecommendationapp.model.User;

public class ProfileActivity
        extends AppCompatActivity {

    private TextView btnMenu;

    private EditText edtUsername;

    private EditText edtGender;

    private EditText edtAge;

    private EditText edtWeight;

    private EditText edtHeight;

    private Button btnSave;

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_profile
        );

        initViews();

        loadUser();

        setupButtons();
    }

    private void initViews() {

        btnMenu =
                findViewById(R.id.btnMenu);

        edtUsername =
                findViewById(R.id.edtUsername);

        edtGender =
                findViewById(R.id.edtGender);

        edtAge =
                findViewById(R.id.edtAge);

        edtWeight =
                findViewById(R.id.edtWeight);

        edtHeight =
                findViewById(R.id.edtHeight);

        btnSave =
                findViewById(R.id.btnSaveProfile);

        btnLogout =
                findViewById(R.id.btnLogout);
    }

    private void loadUser() {

        User user =
                FakeUser.getCurrentUser();

        edtUsername.setText(
                user.getUsername()
        );

        edtGender.setText(
                user.getGender()
        );

        edtAge.setText(
                String.valueOf(user.getAge())
        );

        edtWeight.setText(
                String.valueOf(user.getWeight())
        );

        edtHeight.setText(
                String.valueOf(user.getHeight())
        );
    }

    private void setupButtons() {

        btnMenu.setOnClickListener(v -> {

            finish();
        });

        btnSave.setOnClickListener(v -> {

            FakeUser.updateUser(

                    edtUsername.getText().toString(),

                    edtGender.getText().toString(),

                    Integer.parseInt(
                            edtAge.getText().toString()
                    ),

                    Integer.parseInt(
                            edtWeight.getText().toString()
                    ),

                    Integer.parseInt(
                            edtHeight.getText().toString()
                    )
            );

            finish();
        });

        btnLogout.setOnClickListener(v -> {

            finishAffinity();
        });
    }
}