package com.moutamid.peptidesapp;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.peptidesapp.databinding.ActivityMainBinding;
import com.moutamid.peptidesapp.fragments.CalculatorFragment;
import com.moutamid.peptidesapp.fragments.DetailsFragment;
import com.moutamid.peptidesapp.fragments.HomeFragment;
import com.moutamid.peptidesapp.fragments.InfoFragment;
import com.moutamid.peptidesapp.model.ProductModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;
    public BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        bottomNavigationView = binding.bottomNav;

        bottomNavigationView.setItemActiveIndicatorColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);


        Stash.clear(Constants.PASS);
        Stash.clear(Constants.DOSE);

        new Thread(() -> {
            Constants.databaseReference().child(Constants.PRODUCTS).get().addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    ArrayList<ProductModel> list = Stash.getArrayList(Constants.PRODUCTS_LIST, ProductModel.class);
                    list.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProductModel model = snapshot.getValue(ProductModel.class);
                        list.add(model);
                    }
                    Stash.put(Constants.PRODUCTS_LIST, list);
                    Set<String> uniqueOptions = new HashSet<>();
                    for (ProductModel item : list) {
                        String[] options = item.getBodyType().split(", ");
                        for (String option : options) {
                            uniqueOptions.add(option.trim());
                        }
                    }
                    ArrayList<String> bodyTypes = new ArrayList<>(uniqueOptions);
                    Stash.put(Constants.BODY_TYPE, bodyTypes);
                }
            });
        }).start();

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