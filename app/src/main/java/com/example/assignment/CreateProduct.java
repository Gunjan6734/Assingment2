package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.assignment.DatabaseHelper.COLOR;
import static com.example.assignment.DatabaseHelper.COLUMN_IMAGE;
import static com.example.assignment.DatabaseHelper.DESC;
import static com.example.assignment.DatabaseHelper.PRODUCT_NAME;
import static com.example.assignment.DatabaseHelper.REGULAR_PRICE;
import static com.example.assignment.DatabaseHelper.SALE_PRICE;

public class CreateProduct extends AppCompatActivity implements View.OnClickListener {
     EditText product_name_et, regular_price_et, sale_price_et, desc_et;
     Button add_image_btn,add_product_button;
     ImageView product_iv;
     String product_name, reg_price, sale_price,desc;
    private static final int REQUEST_GET_CAM = 0;
    static final int SELECT_IMAGE = 1;
    static final int CAPTURE_IMAGE = 2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public Uri fileUri;
    public String photoPath,source, message;
    public Bitmap bm=null;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    CustomAdapter  customAdapter;
    ArrayList<CustomDataModel> colorDataModel;
    String selectedItems;
    String edit,id="0";
    String regular_price;
    String color;
    String description;
    byte[] product_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        DatabaseHelper databaseHelper = new DatabaseHelper(CreateProduct.this);
        initializeViews();
        edit = getIntent().getExtras().getString("edit");
        if(edit.equals("1")){
            id = getIntent().getExtras().getString("id");
            setdata();
            add_product_button.setText("Update Product");
        }

    }
    private void setdata() {
        DatabaseHelper dbHelper = new DatabaseHelper(CreateProduct.this);
        Cursor cursor = dbHelper.getData(Integer.parseInt(id));
        if( cursor != null && cursor.moveToFirst() ) {
            product_name = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
            regular_price = cursor.getString(cursor.getColumnIndex(REGULAR_PRICE));
            sale_price = cursor.getString(cursor.getColumnIndex(SALE_PRICE));
            color = cursor.getString(cursor.getColumnIndex(COLOR));
            description = cursor.getString(cursor.getColumnIndex(DESC));
            product_image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
            product_name_et.setText(product_name);
            sale_price_et.setText(sale_price);
            regular_price_et.setText(regular_price);
            selectedItems = color;
            desc_et.setText(description);
            bm = BitmapFactory.decodeByteArray(product_image, 0, product_image.length);
            product_iv.setVisibility(View.VISIBLE);
            product_iv.setImageBitmap(bm);
            cursor.close();
        }

    }
    private void initializeViews(){
        colorDataModel = new ArrayList<>();
        colorDataModel.add(new CustomDataModel("1","Blue", false));
        colorDataModel.add(new CustomDataModel("2","Grey", false));
        colorDataModel.add(new CustomDataModel("3","White", false));
        colorDataModel.add(new CustomDataModel("4","Black", false));

        product_iv = findViewById(R.id.product_image_iv);
        product_name_et = findViewById(R.id.product_name_et);
        regular_price_et = findViewById(R.id.regular_price_et);
        sale_price_et = findViewById(R.id.sale_price_et);
        desc_et = findViewById(R.id.description_et);
        add_image_btn = findViewById(R.id.add_image_button);
        add_product_button = findViewById(R.id.add_product_button);
        add_image_btn.setOnClickListener(this);
        add_product_button.setOnClickListener(this);
        recyclerView = findViewById(R.id.colors_rv);
        gridLayoutManager=  new GridLayoutManager(CreateProduct.this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        customAdapter = new CustomAdapter(CreateProduct.this, colorDataModel);
        recyclerView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(CreateProduct.this,
                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectedItems = colorDataModel.get(position).getLabelName();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_image_button:
                   checkCameraPermission();
                break;
            case R.id.add_product_button:
                getData();
                break;
        }
    }

    private void getData() {
        sale_price = sale_price_et.getText().toString().trim();
        reg_price = regular_price_et.getText().toString().trim();
        desc = desc_et.getText().toString().trim();
        product_name = product_name_et.getText().toString().trim();
        if(product_name.equals("") || product_name.isEmpty()){
            Toast.makeText(CreateProduct.this, "Enter Product Name", Toast.LENGTH_SHORT).show();
        }
        else if(reg_price.equals("") || reg_price.isEmpty()){
            Toast.makeText(CreateProduct.this, "Enter regular Price", Toast.LENGTH_SHORT).show();
        }
        else if(sale_price.equals("") || sale_price.isEmpty()){
            Toast.makeText(CreateProduct.this, "Enter Sale Price", Toast.LENGTH_SHORT).show();
        }
        else if(desc.equals("") || desc.isEmpty()){
            Toast.makeText(CreateProduct.this, "Enter Description", Toast.LENGTH_SHORT).show();
        }
        else if(bm == null){
            Toast.makeText(CreateProduct.this, "Select Image", Toast.LENGTH_SHORT).show();
        }
        else if(selectedItems.equals("") || selectedItems.isEmpty()){
            Toast.makeText(CreateProduct.this, "Select Color", Toast.LENGTH_SHORT).show();
        }
        else{
            DatabaseHelper dbhelper = new DatabaseHelper(CreateProduct.this);
            if(edit.equals("0")){
                dbhelper.insertProducts(product_name, reg_price, sale_price,bm,desc,selectedItems);
            }
            else{
                dbhelper.updateProducts(Integer.valueOf(id),product_name, reg_price, sale_price,bm,desc,selectedItems);
            }
            finish();
            Intent i = new Intent(CreateProduct.this, ProductList.class);
            startActivity(i);
        }

    }

    public boolean checkCameraPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(CreateProduct.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions((Activity) CreateProduct.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_GET_CAM);
                return false;
            } else {
                picProfile();
                return true;
            }
        } else {
            picProfile();
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_GET_CAM) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            if (allgranted) {
                picProfile();
            }
            else if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED )
            {
                picProfile();
            }
            else
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateProduct.this);
                    builder.setCancelable(false);
                    builder.setMessage("Clickindia Jobs need to access your storage to continue");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            checkCameraPermission();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                else
                {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateProduct.this);
                    builder.setCancelable(false);
                    builder.setMessage("Clickindia Jobs need to access your storage to continue");
                    builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            //  finish();

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    builder.show();
                }
            }
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }


    public static Bitmap decodeSampledBitmapFromResource(String resId, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth)
            {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public String getPath(Uri uri)
    {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void copy(File src, File dst) throws IOException
    {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    private Bitmap getCircleBitmap(Bitmap bitmap)
    {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        // bitmap.recycle();

        return output;
    }

    private void picProfile(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        String[] pictureDialogItems;

            pictureDialog.setTitle("Select Action");
            pictureDialogItems =  new String[]{
                    "Select photo from gallery",
                    "Capture photo from camera" };

        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                loadImage();
                                //choosePhotoFromGallary();
                                break;
                            case 1:
                                captureImage();
                                // takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void loadImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, SELECT_IMAGE);
    }

    private void captureImage()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE);
    }
    public Uri getOutputMediaFileUri_1(int type)
    {
        return FileProvider.getUriForFile(CreateProduct.this,"com.example.assignment", getOutputMediaFile(type));
    }
    public Uri getOutputMediaFileUri(int type)
    {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type)
    {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".CIImages");

        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "Image_" + timeStamp + ".jpg");
        }
        else
        {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE)
        {
            if (resultCode == RESULT_OK)
            {
                try
                {
                    fileUri = data.getData();
                    try {
                        source="gallery";
                        File oldFile = new File(getPath(fileUri));
                        File tempFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                        copy(oldFile, tempFile);
                        //File tempFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                        fileUri = FileProvider.getUriForFile(getApplicationContext(),  "com.clickindia.jobs",tempFile);
                        // fileUri = Uri.fromFile(tempFile);
                        saveImageLocation();
                    }
                    catch (Exception e)
                    {
                        //  e.printStackTrace();
                    }
                }
                catch (Exception e)
                {
                    //  e.printStackTrace();
                }

            }
            else if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(), "User cancelled image loading", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Sorry! Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == CAPTURE_IMAGE)
        {
            if (resultCode == RESULT_OK)
            {
                try {
                    bm =(Bitmap) data.getExtras().get("data");

                    if (android.os.Build.VERSION.SDK_INT > 23){
                        // saveImageLocation(getOutputMediaFileUri_1(MEDIA_TYPE_IMAGE));
                        photoPath = getOutputMediaFileUri_1(MEDIA_TYPE_IMAGE).getPath();
                    } else{
                        photoPath = getOutputMediaFileUri(MEDIA_TYPE_IMAGE).getPath();
                        //saveImageLocation(getOutputMediaFileUri(MEDIA_TYPE_IMAGE));
                    }
                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("photopath", MODE_PRIVATE).edit();
                    editor.putString("name", photoPath);
                    editor.putString("bitmap",  encodeToBase64(bm, Bitmap.CompressFormat.JPEG, 100));
                    editor.apply();

                    try {
                        product_iv.setVisibility(View.VISIBLE);
                            product_iv.setImageBitmap(bm);
                        //Glide.with(Signup.this).load(bm).transform(new CircleTransform1()).into(userImageIV);
                    }
                    catch (Exception e)
                    {
                        //  e.printStackTrace();
                    }

                }
                catch (Exception e)
                {
                    //e.printStackTrace();
                }

            }
            else if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }

        else  if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        product_iv.setVisibility(View.VISIBLE);
                        product_iv.setImageBitmap(selectedImage);

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        Log.e("selected",selectedImage.toString());
                        // String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            try {
                                Bitmap myBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),selectedImage);
                                Log.e("image",myBitmap.toString());
                                product_iv.setVisibility(View.VISIBLE);
                                product_iv.setImageBitmap(myBitmap);
                               // Glide.with(Signup.this).load(myBitmap).transform(new CircleTransform1()).into(userImageIV);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }
        }

    }

    public void saveImageLocation()
    {
        photoPath = fileUri.getPath();
        bm = decodeSampledBitmapFromResource(photoPath, 100, 100);
        SharedPreferences.Editor editor = getSharedPreferences("photopath", MODE_PRIVATE).edit();
        editor.putString("name", photoPath);
        editor.putString("bitmap",  encodeToBase64(bm, Bitmap.CompressFormat.JPEG, 100));
        editor.apply();
        product_iv.setVisibility(View.VISIBLE);
         product_iv.setImageBitmap(bm);
    }

}
