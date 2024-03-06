package com.moutamid.peptidesapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.moutamid.peptidesapp.R;
import com.moutamid.peptidesapp.databinding.FragmentCalculatorBinding;

public class CalculatorFragment extends Fragment {
    FragmentCalculatorBinding binding;
    ArrayAdapter<String> syringe, peptide_dose, peptide, bacteriostatic;
    String[] syringeList, peptideList, bacteriostaticList, peptide_dose_list;

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

        binding.syringeList.setAdapter(syringe);
        binding.peptideList.setAdapter(peptide);
        binding.peptideDoseList.setAdapter(peptide_dose);
        binding.bacteriostaticList.setAdapter(bacteriostatic);

        return binding.getRoot();
    }
}