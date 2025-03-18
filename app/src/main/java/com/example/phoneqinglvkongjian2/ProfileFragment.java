package com.example.phoneqinglvkongjian2;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.phoneqinglvkongjian2.utils.ResourceUtils;

public class ProfileFragment extends Fragment {

    private TextView tvNickname;
    private TextView tvUserId;
    private TextView tvPairCode;
    private ImageButton btnCopyCode;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        // 初始化视图
        initViews(view);
        // 设置点击事件
        setupClickListeners();
        // 加载用户数据
        loadUserData();
        // 设置图标
        setupIcons(view);
        
        return view;
    }
    
    private void initViews(View view) {
        tvNickname = view.findViewById(R.id.tvNickname);
        tvUserId = view.findViewById(R.id.tvUserId);
        tvPairCode = view.findViewById(R.id.tvPairCode);
        btnCopyCode = view.findViewById(R.id.btnCopyCode);
        btnLogout = view.findViewById(R.id.btnLogout);
        
        // 甜蜜守护相关按钮
        view.findViewById(R.id.btnTrackView).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "查看足迹轨迹", Toast.LENGTH_SHORT).show();
        });
        
        view.findViewById(R.id.btnLoveDiary).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "查看恋爱日记", Toast.LENGTH_SHORT).show();
        });
        
        view.findViewById(R.id.btnCoupleAlbum).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "查看情侣相册", Toast.LENGTH_SHORT).show();
        });
        
        view.findViewById(R.id.btnAnniversary).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "查看纪念日", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void setupClickListeners() {
        // 复制配对码
        btnCopyCode.setOnClickListener(v -> {
            String pairCode = tvPairCode.getText().toString().replace("配对码: ", "");
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("配对码", pairCode);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(requireContext(), "配对码已复制到剪贴板", Toast.LENGTH_SHORT).show();
        });
        
        // 退出登录
        btnLogout.setOnClickListener(v -> {
            // 处理退出登录逻辑
            Toast.makeText(requireContext(), "退出登录", Toast.LENGTH_SHORT).show();
            // 可以跳转到登录页面
            // Intent intent = new Intent(requireContext(), LoginActivity.class);
            // startActivity(intent);
            // requireActivity().finish();
        });
    }
    
    private void loadUserData() {
        // 这里可以从SharedPreferences或数据库加载用户数据
        // 示例数据
        tvNickname.setText("昵称: 小明");
        tvUserId.setText("ID: 1234567");
        tvPairCode.setText("配对码: 851234567");
    }

    private void setupIcons(View view) {
        // 使用ResourceUtils加载所有图标，避免资源缺失问题
        try {
            // 查找布局中所有的ImageView
            ViewGroup rootView = (ViewGroup) view;
            findAndSetAllImageViews(rootView);
        } catch (Exception e) {
            // 捕获任何可能的异常，避免应用崩溃
            e.printStackTrace();
        }
    }

    private void findAndSetAllImageViews(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ImageView) {
                // 如果是ImageView，检查它的drawable是否为null
                ImageView imageView = (ImageView) child;
                if (imageView.getDrawable() == null) {
                    // 设置默认图标
                    imageView.setImageDrawable(ResourceUtils.getDrawableOrDefault(requireContext(), R.drawable.ic_default));
                }
            } else if (child instanceof ViewGroup) {
                // 递归处理子ViewGroup
                findAndSetAllImageViews((ViewGroup) child);
            }
        }
    }
} 