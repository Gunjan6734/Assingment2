package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ProductList extends AppCompatActivity {

    RecyclerView productRV;
    ProductRVAdapter productRVAdapter;
    ArrayList<ProductDM> productArrayList;
    LinearLayoutManager llm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        DatabaseHelper dbHelper = new DatabaseHelper(ProductList.this);
        productArrayList = dbHelper.getAllProducts();
        Log.e("al", String.valueOf(productArrayList.size()));
        for(int i =0; i<productArrayList.size(); i++){
            Log.e("value", i + "-- "+productArrayList.get(i).getId()
                    + " , " + productArrayList.get(i).getName());
        }
        setViews();
    }

    private void setViews() {
        productRV = findViewById(R.id.product_list_rv);
        llm = new LinearLayoutManager(ProductList.this);
        productRV.setLayoutManager(llm);
        productRV.setHasFixedSize(true);
        productRV.addItemDecoration(new DividerItemDecoration(ProductList.this, LinearLayout.VERTICAL));
        productRVAdapter = new ProductRVAdapter(productArrayList, ProductList.this);
        productRV.setAdapter(productRVAdapter);

    }
}
