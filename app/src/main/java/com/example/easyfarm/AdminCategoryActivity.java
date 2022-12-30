package com.example.easyfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView veggies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        veggies = (ImageView) findViewById(R.id.vegetables);

        veggies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                a.putExtra("category", "veggies");
                startActivity(a);
            }
        });

    }
}
