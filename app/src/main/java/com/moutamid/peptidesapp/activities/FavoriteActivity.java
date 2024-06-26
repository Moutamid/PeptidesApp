package com.moutamid.peptidesapp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fxn.stash.Stash;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.peptidesapp.Constants;
import com.moutamid.peptidesapp.Easter;
import com.moutamid.peptidesapp.FavoritesAdapter;
import com.moutamid.peptidesapp.databinding.ActivityFavoriteBinding;
import com.moutamid.peptidesapp.model.ProductModel;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    ActivityFavoriteBinding binding;
    ArrayList<ProductModel> list;
    FavoritesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Favorites");
        binding.toolbar.menu.setVisibility(View.GONE);
        binding.toolbar.back.setVisibility(View.VISIBLE);
        binding.toolbar.list.setVisibility(View.VISIBLE);
        binding.toolbar.back.setOnClickListener(v -> finish());

        binding.favoriteRc.setLayoutManager(new LinearLayoutManager(this));
        binding.favoriteRc.setHasFixedSize(false);

        list = Stash.getArrayList(Constants.FAVORITE_LIST, ProductModel.class);

        binding.toolbar.list.setText("Total item's: " + list.size());

        if (list.size() > 5) {
            if (Stash.getBoolean(Constants.EASTER, false)) {
                if (
                        !Stash.getBoolean(Constants.EASTER_4, false)
                                || Stash.getBoolean(Constants.EASTER_FOR_ALL)
                ) {
                    new Easter(this).showEaster(Stash.getBoolean(Constants.EASTER_4, false), 4);
                }
            }
        }

        adapter = new FavoritesAdapter(this, list, clickListener);
        binding.favoriteRc.setAdapter(adapter);
    }

    public interface ClickListener {
        void onClick(int pos);
    }

    ClickListener clickListener = new ClickListener() {
        @Override
        public void onClick(int pos) {
            new MaterialAlertDialogBuilder(FavoriteActivity.this)
                    .setTitle("Remove From Favorites")
                    .setMessage("Are you sure you want to remove this item from favorites?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        list.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        Stash.put(Constants.FAVORITE_LIST, list);
                    }).setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    };

}