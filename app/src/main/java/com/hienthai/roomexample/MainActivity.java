package com.hienthai.roomexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hienthai.roomexample.R;
import com.hienthai.roomexample.adapter.UserAdapter;
import com.hienthai.roomexample.database.UserDatabase;
import com.hienthai.roomexample.entities.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UserAdapter.OnclickItemUser {

    private static final int REQUEST_CODE = 123;
    EditText edtName, edtAddress, edtSearch;
    Button btnAddUser;
    RecyclerView rcvUser;
    TextView txtDeleteAll;


    private UserAdapter userAdapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anhXa();


        userAdapter = new UserAdapter(this);
        userList = new ArrayList<>();
        userAdapter.setData(userList);

        rcvUser.setLayoutManager(new LinearLayoutManager(this));
        rcvUser.setAdapter(userAdapter);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        txtDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllUser();
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchUser();
                }

                return false;
            }
        });

        loadData();

    }

    private void deleteAllUser() {

        new AlertDialog.Builder(this).setTitle("Xóa Tất cả User ?")
                .setMessage("Bạn có muốn xóa tất cả User ko ?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserDatabase.getInstance(MainActivity.this).userDAO().deleteAllUser();

                        Toast.makeText(MainActivity.this, "Delete Successfully", Toast.LENGTH_SHORT).show();

                        loadData();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void anhXa() {

        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtSearch = findViewById(R.id.edtSearch);
        btnAddUser = findViewById(R.id.btnAddUser);
        txtDeleteAll = findViewById(R.id.txtDeleteAll);
        rcvUser = findViewById(R.id.rcvUser);

    }

    private void addUser() {

        String name = edtName.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return;
        }


        User user = new User(name, address);

        if (isUserExists(user)) {
            Toast.makeText(this, "User Exists", Toast.LENGTH_SHORT).show();
            return;
        }

        UserDatabase.getInstance(this).userDAO().insert(user);
        Toast.makeText(this, "Add User Successfully", Toast.LENGTH_SHORT).show();

        edtName.setText("");
        edtAddress.setText("");

        hideSoftKeyboard();

        loadData();

    }

    private void loadData() {
        userList = UserDatabase.getInstance(this).userDAO().getAllUser();
        userAdapter.setData(userList);
    }

    public void hideSoftKeyboard() {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    private boolean isUserExists(User user) {
        List<User> userList = UserDatabase.getInstance(this).userDAO().checkUser(user.getName());
        return userList != null && !userList.isEmpty();
    }

    @Override
    public void editUser(User user) {

        Intent intent = new Intent(MainActivity.this, UpdateUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user_object", user);
        intent.putExtras(bundle);

        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    public void deleteUser(User user) {
        new AlertDialog.Builder(this).setTitle("Xóa User ?")
                .setMessage("Bạn có muốn xóa User này ko ?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserDatabase.getInstance(MainActivity.this).userDAO().delete(user);

                        Toast.makeText(MainActivity.this, "Delete Successfully", Toast.LENGTH_SHORT).show();

                        loadData();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    public void searchUser(){
        String name = edtSearch.getText().toString().trim();

        userList = new ArrayList<>();

        userList = UserDatabase.getInstance(this).userDAO().searchUser(name);

        userAdapter.setData(userList);

        hideSoftKeyboard();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            loadData();
        }

    }
}