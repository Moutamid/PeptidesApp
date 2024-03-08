package com.moutamid.peptidesapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.peptidesapp.Constants;
import com.moutamid.peptidesapp.R;
import com.moutamid.peptidesapp.databinding.FragmentDetailsBinding;
import com.moutamid.peptidesapp.model.ProductModel;

import java.util.ArrayList;

public class DetailsFragment extends Fragment {
    FragmentDetailsBinding binding;
    ArrayList<ProductModel> productList;
    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(getLayoutInflater(), container, false);

        ArrayList<String> bodyList = Stash.getArrayList(Constants.BODY_TYPE, String.class);
        productList = Stash.getArrayList(Constants.PRODUCTS_LIST, ProductModel.class);
        ArrayList<String> products = new ArrayList<>();
        for (ProductModel model : productList){
            products.add(model.getName());
        }
        ArrayAdapter<String> bodyAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, bodyList);
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, products);

        binding.bodyGoalsList.setAdapter(bodyAdapter);
        binding.productsList.setAdapter(productAdapter);

        binding.bodyGoals.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setProductAdapter(!s.toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.bodyGoals.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setUI();
            }
        });

        binding.bodyGoalsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setProductAdapter(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setProductAdapter(false);
            }
        });

        binding.productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setUI();
            }
        });

        binding.productsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }

    private void setUI() {
        ProductModel productModel = null;
        for (ProductModel model : productList){
            if (!binding.bodyGoals.getEditText().getText().toString().isEmpty()){
                if (model.getName().equals(binding.products.getEditText().getText().toString()) && model.getBodyType().equals(binding.bodyGoals.getEditText().getText().toString())){
                    productModel = model;
                    break;
                }
            } else {
                if (model.getName().equals(binding.products.getEditText().getText().toString())){
                    productModel = model;
                    break;
                }
            }
        }
        if (productModel != null){
            Glide.with(this).load(productModel.getImage()).into(binding.imageView);
            binding.shortDesc.setText(productModel.getShortDesc());
            binding.longDesc.setText(productModel.getLongDesc());
        }
    }

    private void setProductAdapter(boolean b) {
        if (b){
            ArrayList<String> products = new ArrayList<>();
            for (ProductModel model : productList){
                if (model.getBodyType().equals(binding.bodyGoals.getEditText().getText().toString())){
                    products.add(model.getName());
                }
            }
            ArrayAdapter<String> productAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, products);
            binding.productsList.setAdapter(productAdapter);
        } else {
            ArrayList<String> products = new ArrayList<>();
            for (ProductModel model : productList){
                products.add(model.getName());
            }
            ArrayAdapter<String> productAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, products);
            binding.productsList.setAdapter(productAdapter);
        }
    }
}