package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button createProduct_btn, show_prodcut_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
    }

    private void initializeViews(){
        createProduct_btn = findViewById(R.id.create_product_btn);
        show_prodcut_btn = findViewById(R.id.show_product_btn);
        createProduct_btn.setOnClickListener(this);
        show_prodcut_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.create_product_btn:
                Intent i1 = new Intent(MainActivity.this, CreateProduct.class);
                i1.putExtra("edit", "0");
                startActivity(i1);
                break;
            case R.id.show_product_btn:
                Intent i2 = new Intent(MainActivity.this, ProductList.class);
                startActivity(i2);
                break;

        }
    }
}
