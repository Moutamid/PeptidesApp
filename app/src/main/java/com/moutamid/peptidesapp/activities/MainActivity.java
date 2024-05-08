package com.moutamid.peptidesapp.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.peptidesapp.Constants;
import com.moutamid.peptidesapp.Easter;
import com.moutamid.peptidesapp.R;
import com.moutamid.peptidesapp.databinding.ActivityMainBinding;
import com.moutamid.peptidesapp.fragments.CalculatorFragment;
import com.moutamid.peptidesapp.fragments.DetailsFragment;
import com.moutamid.peptidesapp.fragments.HomeFragment;
import com.moutamid.peptidesapp.fragments.InfoFragment;
import com.moutamid.peptidesapp.model.ProductModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;
    public BottomNavigationView bottomNavigationView;
    private int clickCount = 0;
    private boolean isEasterEnable = false;

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

        Constants.databaseReference().child(Constants.EASTER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isEasterEnable = Boolean.TRUE.equals(snapshot.getValue(Boolean.class));
                Stash.put(Constants.EASTER, isEasterEnable);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Constants.databaseReference().child(Constants.EASTER_FOR_ALL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isMultiple = Boolean.TRUE.equals(snapshot.getValue(Boolean.class));
                if (isMultiple) Stash.clear(Constants.EASTER_COUNT);
                Stash.put(Constants.EASTER_FOR_ALL, isMultiple);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Constants.databaseReference().child(Constants.PROMO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String promo = snapshot.getValue(String.class);
                Stash.put(Constants.PROMO, promo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
            clickCount = 0;
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
            return true;
        } else if (item.getItemId() == R.id.calculator) {
            clickCount = 0;
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CalculatorFragment()).commit();
            return true;
        } else if (item.getItemId() == R.id.details) {
            clickCount = 0;
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new DetailsFragment()).commit();
            return true;
        } else if (item.getItemId() == R.id.info) {
            if (isEasterEnable) {
                if (!Stash.getBoolean(Constants.EASTER_1, false) || Stash.getBoolean(Constants.EASTER_FOR_ALL)) {
                    clickCount++;
                    if (clickCount == 8) {
                        clickCount = 0;
                        new Easter(this).showEaster(Stash.getBoolean(Constants.EASTER_1, false), 1);
                    }
                }
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new InfoFragment()).commit();
            return true;
        }
        return false;
    }
}