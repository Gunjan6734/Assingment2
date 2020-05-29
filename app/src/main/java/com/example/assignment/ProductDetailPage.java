package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TintableImageSourceView;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.assignment.DatabaseHelper.COLOR;
import static com.example.assignment.DatabaseHelper.COLUMN_IMAGE;
import static com.example.assignment.DatabaseHelper.DESC;
import static com.example.assignment.DatabaseHelper.PRODUCT_NAME;
import static com.example.assignment.DatabaseHelper.REGULAR_PRICE;
import static com.example.assignment.DatabaseHelper.SALE_PRICE;

public class ProductDetailPage extends AppCompatActivity {
    TextView product_name_tv,regular_price_tv, sale_price_tv, color_tv, description_tv;
    ImageView product_image_iv;
    String id;
    String product_name;
    String regular_price;
    String sale_price;
    String color;
    String description;
    byte[] product_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_page);
        initializeViews();
        id = getIntent().getExtras().getString("id");
        setdata();
    }

    private void setdata() {
        DatabaseHelper dbHelper = new DatabaseHelper(ProductDetailPage.this);
        Cursor cursor = dbHelper.getData(Integer.parseInt(id));
        if( cursor != null && cursor.moveToFirst() ) {
            product_name = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
            regular_price = cursor.getString(cursor.getColumnIndex(REGULAR_PRICE));
            sale_price = cursor.getString(cursor.getColumnIndex(SALE_PRICE));
            color = cursor.getString(cursor.getColumnIndex(COLOR));
            description = cursor.getString(cursor.getColumnIndex(DESC));
            product_image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
            product_name_tv.setText("Product Name : " +product_name);
            sale_price_tv.setText("Sale Price : " +sale_price);
            regular_price_tv.setText("Regular Price : " +regular_price);
            color_tv.setText("Product Color : " +color);
            description_tv.setText("Product Description : " +description);
            final Bitmap image_bitmap = BitmapFactory.decodeByteArray(product_image, 0, product_image.length);
            product_image_iv.setImageBitmap(image_bitmap);
            cursor.close();
        }

    }

    private void initializeViews() {
        product_name_tv = findViewById(R.id.detail_product_name_tv);
        regular_price_tv = findViewById(R.id.detail_reg_price_tv);
        sale_price_tv = findViewById(R.id.detail_sale_price_tv);
        color_tv = findViewById(R.id.detail_color_tv);
        description_tv = findViewById(R.id.detail_description_tv);
        product_image_iv = findViewById(R.id.product_image_iv);
    }
}
