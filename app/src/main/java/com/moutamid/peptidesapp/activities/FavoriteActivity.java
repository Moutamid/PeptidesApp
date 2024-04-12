package com.moutamid.peptidesapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.moutamid.peptidesapp.R;
import com.moutamid.peptidesapp.databinding.ActivityFavoriteBinding;

public class FavoriteActivity extends AppCompatActivity {
    ActivityFavoriteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Favorites");
        binding.toolbar.menu.setVisibility(View.GONE);
        binding.toolbar.back.setVisibility(View.VISIBLE);
        binding.toolbar.back.setOnClickListener(v -> finish());

    }
}