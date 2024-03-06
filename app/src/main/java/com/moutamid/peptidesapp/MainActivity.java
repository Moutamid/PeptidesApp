package com.moutamid.peptidesapp;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moutamid.peptidesapp.databinding.ActivityMainBinding;
import com.moutamid.peptidesapp.fragments.CalculatorFragment;
import com.moutamid.peptidesapp.fragments.DetailsFragment;
import com.moutamid.peptidesapp.fragments.HomeFragment;
import com.moutamid.peptidesapp.fragments.InfoFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        binding.bottomNav.setItemActiveIndicatorColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        binding.bottomNav.setOnNavigationItemSelectedListener(this);
        binding.bottomNav.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
            return true;
        } else if (item.getItemId() == R.id.calculator) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CalculatorFragment()).commit();
            return true;
        } else if (item.getItemId() == R.id.details) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new DetailsFragment()).commit();
            return true;
        } else if (item.getItemId() == R.id.info) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new InfoFragment()).commit();
            return true;
        }
        return false;
    }
}