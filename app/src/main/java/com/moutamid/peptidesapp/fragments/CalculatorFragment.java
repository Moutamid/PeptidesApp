package com.moutamid.peptidesapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.peptidesapp.Constants;
import com.moutamid.peptidesapp.MainActivity;
import com.moutamid.peptidesapp.R;
import com.moutamid.peptidesapp.databinding.FragmentCalculatorBinding;
import com.moutamid.peptidesapp.model.ProductModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CalculatorFragment extends Fragment {
    private static final String TAG = "CalculatorFragment";
    FragmentCalculatorBinding binding;
    ArrayAdapter<String> syringe, peptide_dose, peptide, bacteriostatic;
    String[] syringeList, peptideList, bacteriostaticList, peptide_dose_list;
    String volumn = "";
    ArrayList<ProductModel> productList;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCalculatorBinding.inflate(getLayoutInflater(), container, false);

        syringeList = getResources().getStringArray(R.array.syringe_volume);
        peptideList = getResources().getStringArray(R.array.peptide_amount);
        bacteriostaticList = getResources().getStringArray(R.array.amount_bacteriostatic);
        peptide_dose_list = getResources().getStringArray(R.array.peptide_dose);

        syringe = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, syringeList);
        peptide_dose = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, peptide_dose_list);
        peptide = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, peptideList);
        bacteriostatic = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, bacteriostaticList);

        productList = Stash.getArrayList(Constants.PRODUCTS_LIST, ProductModel.class);
        ArrayList<String> products = new ArrayList<>();
        for (ProductModel model : productList) {
            products.add(model.getName());
        }
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, products);
        binding.productsList.setAdapter(productAdapter);

        binding.productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setUI();
            }
        });

        syringe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.syringe.setAdapter(syringe);
        binding.peptideList.setAdapter(peptide);
        binding.peptideDoseList.setAdapter(peptide_dose);
        binding.bacteriostaticList.setAdapter(bacteriostatic);

        binding.seekBar.setEnabled(false);

        unit30();

        binding.units30.setOnClickListener(v -> {
            unit30();
        });

        binding.units50.setOnClickListener(v -> {
            unit50();
        });

        binding.insulinSyringe.setOnClickListener(v -> {
            insulinSyringe();
        });

        binding.syringe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    volumn = (String) parent.getItemAtPosition(position);
                    unit30();
                } else if (position == 2) {
                    volumn = (String) parent.getItemAtPosition(position);
                    unit50();
                } else if (position == 3) {
                    volumn = (String) parent.getItemAtPosition(position);
                    insulinSyringe();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.bacteriostaticList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeValue();
            }
        });

        binding.bacteriostatic.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeValue();
            }
        });

        binding.peptideList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeValue();
            }
        });

        binding.peptide.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeValue();
            }
        });


        binding.peptideDoseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeValue();
            }
        });

        binding.peptideDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeValue();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        ProductModel passModel = (ProductModel) Stash.getObject(Constants.DOSE, ProductModel.class);
        if (passModel != null){
            setPassData(passModel);
        }
    }

    private void setPassData(ProductModel productModel) {
        try {
            String originalText = productModel.getShortDesc() + " ";
            String learnMoreText = "Learn More";
            String combinedText = originalText + learnMoreText;
            // Create a SpannableString
            SpannableString spannableString = new SpannableString(combinedText);

            int blueColor = Color.BLUE;
            spannableString.setSpan(new ForegroundColorSpan(blueColor), originalText.length(), combinedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new UnderlineSpan(), originalText.length(), combinedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.detail.setText(spannableString);

            binding.detail.setOnClickListener(v -> {
                Stash.put(Constants.PASS, productModel);
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.bottomNavigationView.setSelectedItemId(R.id.details);
            });

            if (productModel.isSARMS()){
                binding.calculator.setVisibility(View.GONE);
                binding.cardImage.setVisibility(View.VISIBLE);
            } else {
                binding.calculator.setVisibility(View.VISIBLE);
                binding.cardImage.setVisibility(View.GONE);
            }
            Glide.with(requireContext()).load(productModel.getImage()).into(binding.imageView);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Stash.clear(Constants.DOSE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Stash.clear(Constants.DOSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        Stash.clear(Constants.DOSE);
    }

    private void setUI() {
        ProductModel productModel = productList.stream().filter(model -> model.getName().equals(binding.products.getEditText().getText().toString())).findFirst().orElse(null);
        if (productModel != null) {
            String originalText = productModel.getShortDesc() + " ";
            String learnMoreText = "Learn More";
            String combinedText = originalText + learnMoreText;
            // Create a SpannableString
            SpannableString spannableString = new SpannableString(combinedText);

            int blueColor = Color.BLUE;
            spannableString.setSpan(new ForegroundColorSpan(blueColor), originalText.length(), combinedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new UnderlineSpan(), originalText.length(), combinedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.detail.setText(spannableString);

            binding.detail.setOnClickListener(v -> {
                Stash.put(Constants.PASS, productModel);
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.bottomNavigationView.setSelectedItemId(R.id.details);
            });

            if (productModel.isSARMS()){
                binding.calculator.setVisibility(View.GONE);
                binding.cardImage.setVisibility(View.VISIBLE);
            } else {
                binding.calculator.setVisibility(View.VISIBLE);
                binding.cardImage.setVisibility(View.GONE);
            }
            Glide.with(requireContext()).load(productModel.getImage()).into(binding.imageView);
        }
    }

// 200 / 2ml water divided by 5000 (5mg) x 250 = 10
    public static float calculateSyringePull(double targetPeptideDose, double peptideAmountInVial, double bacteriostaticWaterAmount) {
        float water = (float) (bacteriostaticWaterAmount * 100);
        float vial = (float) (peptideAmountInVial * 1000);
        float dose = (float) (targetPeptideDose);
        return water/vial * dose;
    }

    private void unit30() {
        binding.units30.setStrokeWidth(5);
        binding.units50.setStrokeWidth(0);
        binding.insulinSyringe.setStrokeWidth(0);
        binding.syringe.setSelection(1, true);
        binding.seekBar.getConfigBuilder().sectionCount(6).max(30).build();
        changeValue();
    }

    private void unit50() {
        binding.units30.setStrokeWidth(0);
        binding.units50.setStrokeWidth(5);
        binding.insulinSyringe.setStrokeWidth(0);
        binding.syringe.setSelection(2, true);
        binding.seekBar.getConfigBuilder().sectionCount(10).max(50).build();
        changeValue();
    }

    private void insulinSyringe() {
        binding.units30.setStrokeWidth(0);
        binding.units50.setStrokeWidth(0);
        binding.insulinSyringe.setStrokeWidth(5);
        binding.syringe.setSelection(3, true);
        binding.seekBar.getConfigBuilder().sectionCount(10).max(100).build();
        changeValue();
    }

    private void changeValue() {
        if (valid()) {
            double peptideAmountInVial = Double.parseDouble(binding.peptide.getEditText().getText().toString());
            double bacteriostaticWaterAmount = Double.parseDouble(binding.bacteriostatic.getEditText().getText().toString());
            double targetPeptideDose = Double.parseDouble(binding.peptideDose.getEditText().getText().toString());
            double syringeSizes = Double.parseDouble(volumn);
            float syringePullInfo = calculateSyringePull(targetPeptideDose, peptideAmountInVial, bacteriostaticWaterAmount);
            binding.seekBar.setProgress(syringePullInfo);
        }
    }

    private boolean valid() {
        return !binding.peptide.getEditText().getText().toString().isEmpty() && !binding.bacteriostatic.getEditText().getText().toString().isEmpty() &&
                !binding.peptideDose.getEditText().getText().toString().isEmpty() && !volumn.isEmpty();
    }
}