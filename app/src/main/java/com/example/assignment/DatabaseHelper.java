package com.example.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Products.db";
    public static final String PRODUCTS_TABLE_NAME = "products";
    public static final String COLUMN_ID = "id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String SALE_PRICE = "sale_price";
    public static final String REGULAR_PRICE = "regular_price";
    public static final String COLUMN_IMAGE = "product_image";
    public static final String DESC = "description";
    public static final String COLOR = "product_color";
    private HashMap hp;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table products " +
                        "(id integer primary key, product_name text,regular_price text,sale_price text, description text,product_color text,product_image blob)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    public boolean insertProducts (String name, String reg_price, String sale_price, Bitmap image,
                                   String description, String product_color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 50, stream);
        contentValues.put("product_name", name);
        contentValues.put("regular_price", reg_price);
        contentValues.put("sale_price", sale_price);
        contentValues.put("product_image", stream.toByteArray());
        contentValues.put("description", description);
        contentValues.put("product_color", product_color);
        db.insert("products", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from products where id="+id+"", null );
        return cursor;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PRODUCTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateProducts (Integer id,String name, String reg_price, String sale_price, Bitmap image,
                                   String description, String product_color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 50, stream);
        contentValues.put("product_name", name);
        contentValues.put("regular_price", reg_price);
        contentValues.put("sale_price", sale_price);
        contentValues.put("product_image", stream.toByteArray());
        contentValues.put("description", description);
        contentValues.put("product_color", product_color);
        db.update("products", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteProduct(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("products",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<ProductDM> getAllProducts() {
        //String id, String name, String description, String regular_price, String sale_price, byte[] product_photo
        ArrayList<ProductDM> array_list = new ArrayList<>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from products", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
           // array_list.add(res.getString(res.getColumnIndex(PRODUCT_NAME)));
            array_list.add(new ProductDM(res.getString(res.getColumnIndex(COLUMN_ID)),
                    res.getString(res.getColumnIndex(PRODUCT_NAME)),
                    res.getString(res.getColumnIndex(DESC)),
                    res.getString(res.getColumnIndex(REGULAR_PRICE)),
                    res.getString(res.getColumnIndex(SALE_PRICE)),
                    res.getBlob(res.getColumnIndex(COLUMN_IMAGE)),
                    res.getString(res.getColumnIndex(COLOR))
                    ));
            res.moveToNext();
        }
        return array_list;
    }
}
