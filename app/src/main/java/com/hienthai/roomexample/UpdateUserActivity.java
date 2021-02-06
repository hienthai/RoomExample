package com.hienthai.roomexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hienthai.roomexample.database.UserDatabase;
import com.hienthai.roomexample.entities.User;

public class UpdateUserActivity extends AppCompatActivity {

    EditText edtName, edtAddress;
    Button btnUpdateUser;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        anhXa();

        user = (User) getIntent().getExtras().get("user_object");

        if (user != null) {
            edtName.setText(user.getName());
            edtAddress.setText(user.getAddress());
        }

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }


        });


    }

    private void updateUser() {

        String name = edtName.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return;
        }

        user.setName(name);
        user.setAddress(address);

        UserDatabase.getInstance(this).userDAO().update(user);
        Toast.makeText(this, "Update successfully", Toast.LENGTH_SHORT).show();

        Intent intentResult = new Intent();
        setResult(Activity.RESULT_OK, intentResult);
        finish();
    }

    private void anhXa() {

        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        btnUpdateUser = findViewById(R.id.btnUpdateUser);


    }
}