package com.example.phoneqinglvkongjian2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class FullscreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        // 获取传递的图片URL
        String imageUrl = getIntent().getStringExtra("image_url");
        
        // 找到ImageView和关闭按钮
        ImageView imageView = findViewById(R.id.fullscreenImageView);
        ImageButton btnClose = findViewById(R.id.btnClose);
        
        // 使用Glide加载图片
        if (imageUrl != null && !imageUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_photo);
                
            Glide.with(this)
                .load(imageUrl)
                .apply(options)
                .into(imageView);
        } else {
            Toast.makeText(this, "无法加载图片", Toast.LENGTH_SHORT).show();
            finish();
        }
        
        // 点击图片关闭Activity
        imageView.setOnClickListener(v -> finish());
        
        // 点击关闭按钮关闭Activity
        btnClose.setOnClickListener(v -> finish());
    }
} 