package com.example.easyfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    private Button Registerbtn;
    private EditText NameInput, PhoneInput, PasswordInput;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Registerbtn = (Button) findViewById(R.id.register_btn);
        NameInput = (EditText) findViewById(R.id.register_name);
        PhoneInput = (EditText) findViewById(R.id.register_phone_number);
        PasswordInput = (EditText) findViewById(R.id.register_password);
        loadingbar = new ProgressDialog(this);

        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name = NameInput.getText().toString();
        String phone = PhoneInput.getText().toString();
        String password = PasswordInput.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please Write Your Name...", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Write Your Phone Number...", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Write Your Password...", Toast.LENGTH_SHORT).show();
        }

        else
            {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please wait while we are checking the credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidatePhoneNumber(name, phone, password);
        }
    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Congratulations! Your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();

                                        Intent p = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(p);
                                    }

                                    else {
                                        loadingbar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error. Please try Again", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This" +phone+ "already exists. Please try again with a different phone number.", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                    Intent q = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(q);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
