package com.example.assignment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.ProductRVHolder> {
    ArrayList<ProductDM> productList;
    Context context;
    Dialog dialog;
    ImageView imageView;
    ImageView close_btn;
    DatabaseHelper databaseHelper;

    public ProductRVAdapter(ArrayList<ProductDM> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.products_row_item_layout, null);
        return new ProductRVHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRVHolder holder, final int position) {
        final ProductDM productDM= productList.get(position);
        byte[] bitmap = productDM.getProduct_photo();
        databaseHelper = new DatabaseHelper(context);
       final Bitmap image_bitmap =  BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        holder.product_name_tv.setText(productDM.getName());
        holder.regular_price_tv.setText("Regular Price : " + productDM.getRegular_price());
        holder.sale_price_tv.setText("Sale Price : " + productDM.getSale_price());
        holder.desc_tv.setText("Description : " + productDM.getDescription());
        holder.productIV.setImageBitmap(image_bitmap);
        holder.productIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullSizeImage(image_bitmap);
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteProduct(Integer.valueOf(productDM.getId()));
                productList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,productList.size());
            }
        });
        holder.update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CreateProduct.class);
                i.putExtra("edit", "1");
                i.putExtra("id", productDM.getId());
                context.startActivity(i);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetailPage.class);
                i.putExtra("id", productDM.getId());
                context.startActivity(i);
            }
        });


    }

    private void showFullSizeImage(Bitmap bitmap) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_full_image);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        imageView = dialog.findViewById(R.id.dialog_full_image_view);
        close_btn = dialog.findViewById(R.id.dialog_image_close_iv);

        imageView.setImageBitmap(bitmap);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductRVHolder extends RecyclerView.ViewHolder{
        ImageView productIV;
        TextView product_name_tv, regular_price_tv,sale_price_tv, desc_tv;
        Button delete_btn, update_btn;
        public ProductRVHolder(@NonNull View itemView) {
            super(itemView);
            product_name_tv = itemView.findViewById(R.id.ri_product_name_tv);
            regular_price_tv = itemView.findViewById(R.id.ri_regular_price_tv);
            sale_price_tv = itemView.findViewById(R.id.ri_sale_price_tv);
            desc_tv = itemView.findViewById(R.id.ri_desc_tv);
            productIV = itemView.findViewById(R.id.ri_product_iv);
            delete_btn = itemView.findViewById(R.id.ri_delete_btn);
            update_btn = itemView.findViewById(R.id.ri_edit_btn);
        }
    }
}
