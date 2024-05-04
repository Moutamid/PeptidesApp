package com.moutamid.peptidesapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.peptidesapp.Constants;
import com.moutamid.peptidesapp.Easter;
import com.moutamid.peptidesapp.Menu;
import com.moutamid.peptidesapp.R;
import com.moutamid.peptidesapp.activities.MainActivity;
import com.moutamid.peptidesapp.databinding.FragmentDetailsBinding;
import com.moutamid.peptidesapp.model.ProductModel;

import java.util.ArrayList;

public class DetailsFragment extends Fragment {
    private static final String TAG = "DetailsFragment";
    FragmentDetailsBinding binding;
    ArrayList<ProductModel> productList;
    ArrayList<String> products;
    ArrayList<String> bodyList;
    int position = 0;
    int comparingPosition = 0;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        bodyList = Stash.getArrayList(Constants.BODY_TYPE, String.class);
        productList = Stash.getArrayList(Constants.PRODUCTS_LIST, ProductModel.class);
        products = new ArrayList<>();
        for (ProductModel model : productList) {
            products.add(model.getName());
        }

        ArrayAdapter<String> bodyAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, bodyList);
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, products);

        binding.bodyGoalsList.setAdapter(bodyAdapter);
        binding.productsList.setAdapter(productAdapter);

        position = productAdapter.getPosition(getResources().getString(R.string.secret_product_detail));
        Log.d(TAG, "onResume: " + position);

        ProductModel passModel = (ProductModel) Stash.getObject(Constants.PASS, ProductModel.class);
        if (passModel != null) {
            setPassData(passModel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(getLayoutInflater(), container, false);

        binding.toolbar.menu.setOnClickListener(v -> new Menu(requireActivity()).showPopup(v));
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
                comparingPosition = position;
                Log.d(TAG, "comparingPosition: " + comparingPosition);
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

    private void setPassData(ProductModel productModel) {
        try {
            binding.favorite.setVisibility(View.VISIBLE);
            binding.favorite.setEnabled(true);
            binding.favorite.setText("Add to favorites");
            binding.favorite.setIcon(getResources().getDrawable(R.drawable.heart_regular));
            ArrayList<ProductModel> Favrtlist = Stash.getArrayList(Constants.FAVORITE_LIST, ProductModel.class);
            for (ProductModel favr : Favrtlist) {
                if (favr.getID().equals(productModel.getID())) {
                    binding.favorite.setText("Already added to favorites");
                    binding.favorite.setIcon(getResources().getDrawable(R.drawable.heart_solid));
                    binding.favorite.setEnabled(false);
                }
            }
            binding.cardImage.setVisibility(View.VISIBLE);

            binding.link.setVisibility(View.VISIBLE);
            binding.website.setOnClickListener(v -> {
                String link = productModel.getLink() != null ? productModel.getLink() : getString(R.string.website);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
            });
            binding.calculator.setOnClickListener(v -> {
                Stash.put(Constants.DOSE, productModel);
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.bottomNavigationView.setSelectedItemId(R.id.calculator);
            });

            Glide.with(requireContext()).load(productModel.getImage()).into(binding.imageView);
            binding.longDesc.setText(productModel.getLongDesc());

            String originalText = productModel.getShortDesc() + " ";
            String learnMoreText = productModel.isSARMS() ? "Dosage Information." : "";
            String combinedText = originalText + learnMoreText;
            // Create a SpannableString
            SpannableString spannableString = new SpannableString(combinedText);

            int blueColor = Color.BLUE;
            spannableString.setSpan(new ForegroundColorSpan(blueColor), originalText.length(), combinedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new UnderlineSpan(), originalText.length(), combinedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            binding.shortDesc.setText(spannableString);

            String longDesc = productModel.getLongDesc();
            String link = productModel.getLink() != null ? productModel.getLink() : getString(R.string.website);
//            String combined = longDesc.concat("\n\n").concat(link);
//            SpannableString longString = new SpannableString(combined);
//            int startIndex = Math.min(longDesc.length(), combined.length());
//            int endIndex = combined.length();
////            longString.setSpan(new ClickableSpan() {
////                @Override
////                public void onClick(@NonNull View v) {
////                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
////                }
////            }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            longString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            longString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.longDesc.setText(longDesc);
            binding.longDesc.setOnClickListener(v -> {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
            });

            binding.shortDesc.setOnClickListener(v -> {
                Stash.put(Constants.DOSE, productModel);
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.bottomNavigationView.setSelectedItemId(R.id.calculator);
            });

            int positionInBodyList = bodyList.indexOf(productModel.getBodyType());
            int positionInProductList = products.indexOf(productModel.getName().trim());
            if (positionInBodyList != -1 && positionInBodyList < bodyList.size()) {
                binding.bodyGoalsList.setSelection(positionInBodyList);
            }
            if (positionInProductList != -1 && positionInProductList < products.size()) {
                binding.productsList.setSelection(positionInProductList);
            }

            binding.favorite.setOnClickListener(v -> {
                ArrayList<ProductModel> list = Stash.getArrayList(Constants.FAVORITE_LIST, ProductModel.class);
                list.add(productModel);
                Stash.put(Constants.FAVORITE_LIST, list);
                binding.favorite.setText("Added to favorites");
                binding.favorite.setEnabled(false);
                Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                binding.favorite.setIcon(getResources().getDrawable(R.drawable.heart_solid));
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Stash.clear(Constants.PASS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Stash.clear(Constants.PASS);
    }

    @Override
    public void onStop() {
        super.onStop();
        Stash.clear(Constants.PASS);
    }

    private void setUI() {
        ProductModel productModel = null;
        for (ProductModel model : productList) {
            if (!binding.bodyGoals.getEditText().getText().toString().isEmpty()) {
                String[] bodyType = model.getBodyType().split(", ");
                for (String type : bodyType) {
                    if (model.getName().equals(binding.products.getEditText().getText().toString()) && type.trim().equals(binding.bodyGoals.getEditText().getText().toString().trim())) {
                        productModel = model;
                        break;
                    }
                }
            } else {
                if (model.getName().equals(binding.products.getEditText().getText().toString())) {
                    productModel = model;
                    break;
                }
            }
        }
        if (productModel != null) {
            binding.favorite.setVisibility(View.VISIBLE);
            binding.favorite.setEnabled(true);
            binding.favorite.setText("Add to favorites");
            binding.favorite.setIcon(getResources().getDrawable(R.drawable.heart_regular));
            ArrayList<ProductModel> Favrtlist = Stash.getArrayList(Constants.FAVORITE_LIST, ProductModel.class);
            for (ProductModel favr : Favrtlist) {
                if (favr.getID().equals(productModel.getID())) {
                    binding.favorite.setText("Already added to favorites");
                    binding.favorite.setIcon(getResources().getDrawable(R.drawable.heart_solid));
                    binding.favorite.setEnabled(false);
                }
            }
            binding.cardImage.setVisibility(View.VISIBLE);

            Glide.with(this).load(productModel.getImage()).into(binding.imageView);

            String originalText = productModel.getShortDesc() + " ";
            String learnMoreText = productModel.isSARMS() ? "Dosage Information." : "";
            String combinedText = originalText + learnMoreText;
            // Create a SpannableString
            SpannableString spannableString = new SpannableString(combinedText);

            int blueColor = Color.BLUE;
            spannableString.setSpan(new ForegroundColorSpan(blueColor), originalText.length(), combinedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new UnderlineSpan(), originalText.length(), combinedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.shortDesc.setText(spannableString);

            String longDesc = productModel.getLongDesc();
            String link = productModel.getLink() != null ? productModel.getLink() : getString(R.string.website);
//            String combined = longDesc.concat("\n\n").concat(link);
//            SpannableString longString = new SpannableString(combined);
//            int startIndex = Math.min(longDesc.length(), combined.length());
//            int endIndex = combined.length();
////            longString.setSpan(new ClickableSpan() {
////                @Override
////                public void onClick(@NonNull View v) {
////                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
////                }
////            }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            longString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            longString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.longDesc.setText(longDesc);
            binding.longDesc.setOnClickListener(v -> {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
            });

            ProductModel finalProductModel = productModel;
            binding.shortDesc.setOnClickListener(v -> {
                Stash.put(Constants.DOSE, finalProductModel);
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.bottomNavigationView.setSelectedItemId(R.id.calculator);
            });
            binding.link.setVisibility(View.VISIBLE);
            binding.website.setOnClickListener(v -> {
                if (Stash.getBoolean(Constants.EASTER, false)) {
                    if (position == comparingPosition) {
                        Log.d(TAG, "setUI: " + Stash.getBoolean(Constants.EASTER_1, false));
                        if (Stash.getBoolean(Constants.EASTER_1, false) && !Stash.getBoolean(Constants.EASTER_2, false)) {
                            Stash.put(Constants.EASTER_2, true);
                            new Easter(requireActivity()).showEaster();
                        } else {
                            Log.d(TAG, "link: ");
                            openLink(link);
                        }
                    } else {
                        openLink(link);
                    }
                } else {
                    openLink(link);
                }
            });
            binding.calculator.setOnClickListener(v -> {
                Stash.put(Constants.DOSE, finalProductModel);
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.bottomNavigationView.setSelectedItemId(R.id.calculator);
            });
            binding.favorite.setOnClickListener(v -> {
                ArrayList<ProductModel> list = Stash.getArrayList(Constants.FAVORITE_LIST, ProductModel.class);
                list.add(finalProductModel);
                Stash.put(Constants.FAVORITE_LIST, list);
                binding.favorite.setText("Added to favorites");
                binding.favorite.setEnabled(false);
                Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                binding.favorite.setIcon(getResources().getDrawable(R.drawable.heart_solid));
            });

        }
    }

    private void openLink(String link) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

    private void setProductAdapter(boolean b) {
        if (b) {
            ArrayList<String> products = new ArrayList<>();
            for (ProductModel model : productList) {
                String[] bodyType = model.getBodyType().split(", ");
                for (String type : bodyType) {
                    if (type.trim().equals(binding.bodyGoals.getEditText().getText().toString().trim())) {
                        products.add(model.getName());
                    }
                }
            }
            ArrayAdapter<String> productAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, products);
            binding.productsList.setAdapter(productAdapter);
        } else {
            ArrayList<String> products = new ArrayList<>();
            for (ProductModel model : productList) {
                products.add(model.getName());
            }
            ArrayAdapter<String> productAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, products);
            binding.productsList.setAdapter(productAdapter);
        }
    }
}