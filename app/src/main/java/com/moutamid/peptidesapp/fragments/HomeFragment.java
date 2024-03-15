package com.moutamid.peptidesapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.peptidesapp.Constants;
import com.moutamid.peptidesapp.R;
import com.moutamid.peptidesapp.model.ProductModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

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

        return view;
    }
}