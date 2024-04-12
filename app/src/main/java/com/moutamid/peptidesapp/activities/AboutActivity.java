package com.moutamid.peptidesapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.moutamid.peptidesapp.R;
import com.moutamid.peptidesapp.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {
    ActivityAboutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("About");
        binding.toolbar.menu.setVisibility(View.GONE);
        binding.toolbar.back.setVisibility(View.VISIBLE);
        binding.toolbar.back.setOnClickListener(v -> finish());


        String name = "Name : " + getString(R.string.app_name);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = pInfo.versionName;
            binding.version.setText("Version : " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        binding.name.setText(name);

    }
}