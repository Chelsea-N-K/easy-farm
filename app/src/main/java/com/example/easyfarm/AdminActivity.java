package com.example.easyfarm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import Model.Users;
import Prevalent.Prevalent;
import io.paperdb.Paper;

public class AdminActivity extends AppCompatActivity {

    private Button registerBtn1;
    private EditText adminPhoneInput, adminPasswordInput;
    private ProgressDialog loadingBar;

    private String parentDbName = "Admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        registerBtn1 = (Button) findViewById(R.id.register_btn1);
        adminPhoneInput = (EditText) findViewById(R.id.admin_phone_number);
        adminPasswordInput = (EditText) findViewById(R.id.admin_password);
        loadingBar = new ProgressDialog(this);

        registerBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAdmin();

            }
        });
    }

    private void loginAdmin(){
        String phone = adminPhoneInput.getText().toString();
        String password = adminPasswordInput.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password", Toast.LENGTH_SHORT).show();
        }

        else {
            loadingBar.setTitle("Admin Account");
            loadingBar.setMessage("Please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (Objects.equals(usersData.getPhone(), phone))
                    {
                        if (Objects.equals(usersData.getPassword(), password))
                        {
                            Toast.makeText(AdminActivity.this, "Logged in successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent a = new Intent(AdminActivity.this, AdminCategoryActivity.class);
                            startActivity(a);
                        }
                    }
                }

                else
                {
                    Toast.makeText(AdminActivity.this, "Account with this "+phone+ " number does not exist.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
