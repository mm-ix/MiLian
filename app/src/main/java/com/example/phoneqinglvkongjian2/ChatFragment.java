package com.example.phoneqinglvkongjian2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ChatFragment extends Fragment {

    private static final int PERMISSION_REQUEST_READ_STORAGE = 1001;

    private RecyclerView recyclerView;
    private EditText etMessage;
    private ImageButton btnSend;
    private ImageButton btnVoice;
    private ImageButton btnEmoji;
    private ImageButton btnMore;
    private View moreFunctionsPanel;
    private boolean isPanelVisible = false;
    
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList = new ArrayList<>();
    
    // 随机照片URL数组（作为备用，如果无法访问本地相册）
    private String[] randomPhotoUrls = {
        "https://picsum.photos/300/200?random=1",
        "https://picsum.photos/300/200?random=2",
        "https://picsum.photos/300/200?random=3",
        "https://picsum.photos/300/200?random=4",
        "https://picsum.photos/300/200?random=5",
        "https://picsum.photos/300/200?random=6",
        "https://picsum.photos/300/200?random=7",
        "https://picsum.photos/300/200?random=8",
        "https://picsum.photos/300/200?random=9",
        "https://picsum.photos/300/200?random=10"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        
        // 初始化视图
        recyclerView = view.findViewById(R.id.recyclerView);
        etMessage = view.findViewById(R.id.etMessage);
        btnVoice = view.findViewById(R.id.btnVoice);
        btnEmoji = view.findViewById(R.id.btnEmoji);
        btnMore = view.findViewById(R.id.btnMore);
        btnSend = view.findViewById(R.id.btnSend);
        
        // 初始化更多功能面板
        moreFunctionsPanel = view.findViewById(R.id.moreFunctionsPanel);
        
        // 设置RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setAdapter(chatAdapter);
        
        // 添加一些测试消息
        addTestMessages();
        
        // 设置按钮点击事件
        btnVoice.setOnClickListener(v -> {
            Toast.makeText(getContext(), "语音功能暂未实现", Toast.LENGTH_SHORT).show();
        });
        
        btnEmoji.setOnClickListener(v -> {
            Toast.makeText(getContext(), "表情功能暂未实现", Toast.LENGTH_SHORT).show();
        });
        
        btnMore.setOnClickListener(v -> {
            toggleMoreFunctionsPanel();
        });
        
        btnSend.setOnClickListener(v -> sendMessage());
        
        // 设置发送消息
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
        
        // 添加点击事件，当用户点击输入框外部区域时发送消息
        etMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                sendMessage();
            }
        });
        
        // 监听输入框文本变化，显示或隐藏发送按钮
        etMessage.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnMore.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
        
        // 设置更多功能面板中的功能按钮
        setupMoreFunctionsPanel();
        
        return view;
    }
    
    private void setupMoreFunctionsPanel() {
        if (moreFunctionsPanel == null) return;
        
        LinearLayout layoutAlbum = moreFunctionsPanel.findViewById(R.id.layoutAlbum);
        LinearLayout layoutCamera = moreFunctionsPanel.findViewById(R.id.layoutCamera);
        LinearLayout layoutRandomPhoto = moreFunctionsPanel.findViewById(R.id.layoutRandomPhoto);
        LinearLayout layoutLocation = moreFunctionsPanel.findViewById(R.id.layoutLocation);
        LinearLayout layoutFile = moreFunctionsPanel.findViewById(R.id.layoutFile);
        LinearLayout layoutVideoCall = moreFunctionsPanel.findViewById(R.id.layoutVideoCall);
        LinearLayout layoutVoiceCall = moreFunctionsPanel.findViewById(R.id.layoutVoiceCall);
        LinearLayout layoutFavorite = moreFunctionsPanel.findViewById(R.id.layoutFavorite);
        
        if (layoutAlbum != null) {
            layoutAlbum.setOnClickListener(v -> {
                Toast.makeText(getContext(), "相册功能暂未实现", Toast.LENGTH_SHORT).show();
                hideMoreFunctionsPanel();
            });
        }
        
        if (layoutCamera != null) {
            layoutCamera.setOnClickListener(v -> {
                Toast.makeText(getContext(), "拍照功能暂未实现", Toast.LENGTH_SHORT).show();
                hideMoreFunctionsPanel();
            });
        }
        
        if (layoutRandomPhoto != null) {
            layoutRandomPhoto.setOnClickListener(v -> {
                checkStoragePermissionAndPickRandomPhoto();
                hideMoreFunctionsPanel();
            });
        }
        
        if (layoutLocation != null) {
            layoutLocation.setOnClickListener(v -> {
                Toast.makeText(getContext(), "位置功能暂未实现", Toast.LENGTH_SHORT).show();
                hideMoreFunctionsPanel();
            });
        }
        
        if (layoutFile != null) {
            layoutFile.setOnClickListener(v -> {
                Toast.makeText(getContext(), "文件功能暂未实现", Toast.LENGTH_SHORT).show();
                hideMoreFunctionsPanel();
            });
        }
        
        if (layoutVideoCall != null) {
            layoutVideoCall.setOnClickListener(v -> {
                Toast.makeText(getContext(), "视频通话功能暂未实现", Toast.LENGTH_SHORT).show();
                hideMoreFunctionsPanel();
            });
        }
        
        if (layoutVoiceCall != null) {
            layoutVoiceCall.setOnClickListener(v -> {
                Toast.makeText(getContext(), "语音通话功能暂未实现", Toast.LENGTH_SHORT).show();
                hideMoreFunctionsPanel();
            });
        }
        
        if (layoutFavorite != null) {
            layoutFavorite.setOnClickListener(v -> {
                Toast.makeText(getContext(), "收藏功能暂未实现", Toast.LENGTH_SHORT).show();
                hideMoreFunctionsPanel();
            });
        }
    }
    
    private void toggleMoreFunctionsPanel() {
        if (isPanelVisible) {
            hideMoreFunctionsPanel();
        } else {
            showMoreFunctionsPanel();
        }
    }
    
    private void showMoreFunctionsPanel() {
        if (moreFunctionsPanel != null) {
            moreFunctionsPanel.setVisibility(View.VISIBLE);
            isPanelVisible = true;
        }
    }
    
    private void hideMoreFunctionsPanel() {
        if (moreFunctionsPanel != null) {
            moreFunctionsPanel.setVisibility(View.GONE);
            isPanelVisible = false;
        }
    }
    
    private void sendMessage() {
        String message = etMessage.getText().toString().trim();
        if (!message.isEmpty()) {
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            messageList.add(new ChatMessage(message, true, currentTime, false));
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
            etMessage.setText("");
            
            // 模拟对方回复
            simulateReply();
        }
    }
    
    private void simulateReply() {
        new android.os.Handler().postDelayed(() -> {
            if (getContext() == null) return; // 防止Fragment已经被销毁
            
            String[] replies = {
                "好的，我知道了",
                "嗯嗯，继续说",
                "真的吗？太棒了！",
                "我也是这么想的",
                "我们下次见面聊吧",
                "我现在有点忙，稍后回复你",
                "我很想你",
                "今天过得怎么样？",
                "晚上想吃什么？",
                "周末有什么计划？"
            };
            
            Random random = new Random();
            String reply = replies[random.nextInt(replies.length)];
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            
            messageList.add(new ChatMessage(reply, false, currentTime, false));
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
        }, 1000);
    }
    
    private void addTestMessages() {
        messageList.add(new ChatMessage("你好啊，今天过得怎么样？", true, "10:30", false));
        messageList.add(new ChatMessage("挺好的，刚忙完工作，你呢？", false, "10:31", false));
        messageList.add(new ChatMessage("我也是，今天工作很顺利", true, "10:32", false));
        messageList.add(new ChatMessage("晚上想吃什么？我来做饭", false, "10:33", false));
        messageList.add(new ChatMessage("都可以，你做什么我都喜欢吃", true, "10:35", false));
        recyclerView.scrollToPosition(messageList.size() - 1);
    }
    
    private void checkStoragePermissionAndPickRandomPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13及以上使用READ_MEDIA_IMAGES权限
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_READ_STORAGE);
            } else {
                pickRandomPhotoFromGallery();
            }
        } else {
            // Android 12及以下使用READ_EXTERNAL_STORAGE权限
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_STORAGE);
            } else {
                pickRandomPhotoFromGallery();
            }
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickRandomPhotoFromGallery();
            } else {
                Toast.makeText(getContext(), "需要存储权限才能访问相册", Toast.LENGTH_SHORT).show();
                // 使用网络随机图片作为备用
                useRandomNetworkPhoto();
            }
        }
    }
    
    private void pickRandomPhotoFromGallery() {
        List<Uri> photoUris = getPhotoUrisFromGallery();
        
        if (photoUris.isEmpty()) {
            Toast.makeText(getContext(), "没有找到照片，使用网络随机图片", Toast.LENGTH_SHORT).show();
            useRandomNetworkPhoto();
            return;
        }
        
        // 随机选择一张照片
        Random random = new Random();
        Uri randomPhotoUri = photoUris.get(random.nextInt(photoUris.size()));
        
        // 显示确认对话框
        showPhotoConfirmDialog(randomPhotoUri);
    }
    
    private List<Uri> getPhotoUrisFromGallery() {
        List<Uri> photoUris = new ArrayList<>();
        ContentResolver contentResolver = requireContext().getContentResolver();
        
        // 查询图片
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        
        String[] projection = {MediaStore.Images.Media._ID};
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
        
        try (Cursor cursor = contentResolver.query(
                collection,
                projection,
                null,
                null,
                sortOrder)) {
            
            // 获取ID列的索引
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            
            // 最多获取50张照片
            int count = 0;
            int maxPhotos = 50;
            
            while (cursor.moveToNext() && count < maxPhotos) {
                long id = cursor.getLong(idColumn);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                photoUris.add(contentUri);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "获取照片失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        
        return photoUris;
    }
    
    private void useRandomNetworkPhoto() {
        Random random = new Random();
        String randomUrl = randomPhotoUrls[random.nextInt(randomPhotoUrls.length)];
        Uri randomPhotoUri = Uri.parse(randomUrl);
        showPhotoConfirmDialog(randomPhotoUri);
    }
    
    private void showPhotoConfirmDialog(Uri imageUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm_photo, null);
        builder.setView(dialogView);
        
        ImageView ivPreview = dialogView.findViewById(R.id.ivPreview);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        Button btnRandom = dialogView.findViewById(R.id.btnRandom);
        
        // 使用Glide加载图片
        Glide.with(this)
            .load(imageUri)
            .into(ivPreview);
        
        AlertDialog dialog = builder.create();
        dialog.show();
        
        // 设置图片点击事件，点击时显示全屏预览
        ivPreview.setOnClickListener(v -> {
            showFullscreenImage(imageUri);
        });
        
        // 设置取消按钮点击事件
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        // 设置确认按钮点击事件
        btnConfirm.setOnClickListener(v -> {
            sendImageMessage(imageUri.toString());
            dialog.dismiss();
        });
        
        // 设置重新随机按钮点击事件
        btnRandom.setOnClickListener(v -> {
            dialog.dismiss();
            // 重新随机选择照片
            pickRandomPhotoFromGallery();
        });
    }
    
    private void sendImageMessage(String imageUrl) {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        messageList.add(new ChatMessage(imageUrl, true, currentTime, true));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
        
        // 模拟对方回复
        new android.os.Handler().postDelayed(() -> {
            if (getContext() == null) return; // 防止Fragment已经被销毁
            
            String[] replies = {
                "照片拍得真好！",
                "这是在哪里拍的？",
                "看起来不错！",
                "谢谢分享！",
                "我也想去这个地方"
            };
            
            Random random = new Random();
            String reply = replies[random.nextInt(replies.length)];
            String replyTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            
            messageList.add(new ChatMessage(reply, false, replyTime, false));
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
        }, 1500);
    }
    
    // 聊天消息数据类
    public static class ChatMessage {
        private String content;
        private boolean isSent;
        private String time;
        private boolean isImage;
        
        public ChatMessage(String content, boolean isSent, String time, boolean isImage) {
            this.content = content;
            this.isSent = isSent;
            this.time = time;
            this.isImage = isImage;
        }
        
        public String getContent() {
            return content;
        }
        
        public boolean isSent() {
            return isSent;
        }
        
        public String getTime() {
            return time;
        }
        
        public boolean isImage() {
            return isImage;
        }
    }
    
    // 聊天适配器
    private class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VIEW_TYPE_SENT_MESSAGE = 1;
        private static final int VIEW_TYPE_RECEIVED_MESSAGE = 2;
        private static final int VIEW_TYPE_SENT_IMAGE = 3;
        private static final int VIEW_TYPE_RECEIVED_IMAGE = 4;
        
        private List<ChatMessage> messages;
        
        public ChatAdapter(List<ChatMessage> messages) {
            this.messages = messages;
        }
        
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_SENT_MESSAGE) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_sent, parent, false);
                return new SentMessageHolder(view);
            } else if (viewType == VIEW_TYPE_RECEIVED_MESSAGE) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_received, parent, false);
                return new ReceivedMessageHolder(view);
            } else if (viewType == VIEW_TYPE_SENT_IMAGE) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_image_sent, parent, false);
                return new SentImageHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_image_received, parent, false);
                return new ReceivedImageHolder(view);
            }
        }
        
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ChatMessage message = messages.get(position);
            
            if (holder instanceof SentMessageHolder) {
                ((SentMessageHolder) holder).bind(message);
            } else if (holder instanceof ReceivedMessageHolder) {
                ((ReceivedMessageHolder) holder).bind(message);
            } else if (holder instanceof SentImageHolder) {
                ((SentImageHolder) holder).bind(message);
            } else if (holder instanceof ReceivedImageHolder) {
                ((ReceivedImageHolder) holder).bind(message);
            }
        }
        
        @Override
        public int getItemViewType(int position) {
            ChatMessage message = messages.get(position);
            if (!message.isImage()) {
                return message.isSent() ? VIEW_TYPE_SENT_MESSAGE : VIEW_TYPE_RECEIVED_MESSAGE;
            } else {
                return message.isSent() ? VIEW_TYPE_SENT_IMAGE : VIEW_TYPE_RECEIVED_IMAGE;
            }
        }
        
        @Override
        public int getItemCount() {
            return messages.size();
        }
        
        // 发送消息的ViewHolder
        class SentMessageHolder extends RecyclerView.ViewHolder {
            TextView tvMessage;
            TextView tvTime;
            
            SentMessageHolder(View itemView) {
                super(itemView);
                tvMessage = itemView.findViewById(R.id.tvMessage);
                tvTime = itemView.findViewById(R.id.tvTime);
            }
            
            void bind(ChatMessage message) {
                if (message != null && message.getContent() != null) {
                    tvMessage.setText(message.getContent());
                } else {
                    tvMessage.setText("");
                }
                tvTime.setText(message.getTime());
                
                // 显示时间（每隔几条消息显示一次）
                if (getAdapterPosition() % 5 == 0) {
                    tvTime.setVisibility(View.VISIBLE);
                } else {
                    tvTime.setVisibility(View.GONE);
                }
            }
        }
        
        // 接收消息的ViewHolder
        class ReceivedMessageHolder extends RecyclerView.ViewHolder {
            TextView tvMessage;
            TextView tvTime;
            
            ReceivedMessageHolder(View itemView) {
                super(itemView);
                tvMessage = itemView.findViewById(R.id.tvMessage);
                tvTime = itemView.findViewById(R.id.tvTime);
            }
            
            void bind(ChatMessage message) {
                if (message != null && message.getContent() != null) {
                    tvMessage.setText(message.getContent());
                } else {
                    tvMessage.setText("");
                }
                tvTime.setText(message.getTime());
                
                // 显示时间（每隔几条消息显示一次）
                if (getAdapterPosition() % 5 == 0) {
                    tvTime.setVisibility(View.VISIBLE);
                } else {
                    tvTime.setVisibility(View.GONE);
                }
            }
        }
        
        // 发送图片的ViewHolder
        class SentImageHolder extends RecyclerView.ViewHolder {
            ImageView ivImage;
            TextView tvTime;
            
            SentImageHolder(View itemView) {
                super(itemView);
                ivImage = itemView.findViewById(R.id.ivImage);
                tvTime = itemView.findViewById(R.id.tvTime);
            }
            
            void bind(ChatMessage message) {
                // 使用Glide加载图片
                RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_photo)
                    .error(R.drawable.ic_photo);
                
                if (message != null && message.getContent() != null) {
                    Glide.with(itemView.getContext())
                        .load(message.getContent())
                        .apply(options)
                        .into(ivImage);
                        
                    // 添加图片点击事件，点击时显示全屏预览
                    ivImage.setOnClickListener(v -> {
                        try {
                            Uri imageUri = Uri.parse(message.getContent());
                            showFullscreenImage(imageUri);
                        } catch (Exception e) {
                            Toast.makeText(itemView.getContext(), "无法加载图片", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
                }
                
                if (message != null && message.getTime() != null) {
                    tvTime.setText(message.getTime());
                } else {
                    tvTime.setText("");
                }
                
                // 显示时间（每隔几条消息显示一次）
                if (getAdapterPosition() % 5 == 0) {
                    tvTime.setVisibility(View.VISIBLE);
                } else {
                    tvTime.setVisibility(View.GONE);
                }
            }
        }
        
        // 接收图片的ViewHolder
        class ReceivedImageHolder extends RecyclerView.ViewHolder {
            ImageView ivImage;
            TextView tvTime;
            
            ReceivedImageHolder(View itemView) {
                super(itemView);
                ivImage = itemView.findViewById(R.id.ivImage);
                tvTime = itemView.findViewById(R.id.tvTime);
            }
            
            void bind(ChatMessage message) {
                // 使用Glide加载图片
                RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_photo)
                    .error(R.drawable.ic_photo);
                
                if (message != null && message.getContent() != null) {
                    Glide.with(itemView.getContext())
                        .load(message.getContent())
                        .apply(options)
                        .into(ivImage);
                        
                    // 添加图片点击事件，点击时显示全屏预览
                    ivImage.setOnClickListener(v -> {
                        try {
                            Uri imageUri = Uri.parse(message.getContent());
                            showFullscreenImage(imageUri);
                        } catch (Exception e) {
                            Toast.makeText(itemView.getContext(), "无法加载图片", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
                }
                
                if (message != null && message.getTime() != null) {
                    tvTime.setText(message.getTime());
                } else {
                    tvTime.setText("");
                }
                
                // 显示时间（每隔几条消息显示一次）
                if (getAdapterPosition() % 5 == 0) {
                    tvTime.setVisibility(View.VISIBLE);
                } else {
                    tvTime.setVisibility(View.GONE);
                }
            }
        }
    }
    
    // 添加回showFullscreenImage方法
    private void showFullscreenImage(Uri imageUri) {
        Intent intent = new Intent(getContext(), FullscreenImageActivity.class);
        intent.putExtra("image_url", imageUri.toString());
        startActivity(intent);
    }
} 