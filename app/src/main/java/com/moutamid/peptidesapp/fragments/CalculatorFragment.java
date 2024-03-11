package com.moutamid.peptidesapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

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

        return binding.getRoot();
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
        }
    }

    public static double calculatePeptideDose(double peptideAmountInVial, double bacteriostaticWaterAmount, double syringeVolume) {
        double peptideAmountMicrograms = peptideAmountInVial * 1000;
        return (peptideAmountMicrograms / bacteriostaticWaterAmount) * syringeVolume;
    }

    public static String calculateSyringePull(double targetPeptideDose, double peptideAmountInVial, double bacteriostaticWaterAmount, double syringeVolume) {
        DecimalFormat df = new DecimalFormat("#.#");
        double calculatedDose = calculatePeptideDose(peptideAmountInVial, bacteriostaticWaterAmount, syringeVolume);
        if (Math.abs(calculatedDose - targetPeptideDose) < 0.1) {
            return "To have a dose of " + df.format(targetPeptideDose) + " mcg, pull the syringe to " + df.format(syringeVolume) + " ml";
        }
        return "" + df.format(syringeVolume);
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
            String syringePullInfo = calculateSyringePull(targetPeptideDose, peptideAmountInVial, bacteriostaticWaterAmount, syringeSizes);
            Log.d(TAG, "changeValue: " + syringePullInfo);
        }
    }

    private boolean valid() {
        return !binding.peptide.getEditText().getText().toString().isEmpty() && !binding.bacteriostatic.getEditText().getText().toString().isEmpty() &&
                !binding.peptideDose.getEditText().getText().toString().isEmpty() && !volumn.isEmpty();
    }
}