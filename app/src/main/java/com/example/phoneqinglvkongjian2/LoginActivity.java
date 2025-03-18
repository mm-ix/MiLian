package com.example.phoneqinglvkongjian2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private ImageButton btnWechatLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化视图
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnWechatLogin = findViewById(R.id.btnWechatLogin);

        // 登录按钮点击事件
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // 简单验证 - 用户名和密码都是"1"
            if (username.equals("1") && password.equals("1")) {
                // 登录成功，跳转到主界面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // 结束登录活动
            } else {
                // 登录失败
                Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }
        });

        // 注册按钮点击事件
        btnRegister.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "注册功能暂未实现", Toast.LENGTH_SHORT).show();
        });

        // 微信登录按钮点击事件
        btnWechatLogin.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "微信登录功能暂未实现", Toast.LENGTH_SHORT).show();
        });
    }
} 