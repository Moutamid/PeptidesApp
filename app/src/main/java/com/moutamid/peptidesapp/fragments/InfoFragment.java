package com.moutamid.peptidesapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.peptidesapp.R;

public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        MaterialButton website = view.findViewById(R.id.website);
        MaterialButton email = view.findViewById(R.id.email);
        MaterialButton phone = view.findViewById(R.id.phone);
        MaterialButton whatsapp = view.findViewById(R.id.whatsapp);
        MaterialButton exit = view.findViewById(R.id.exit);

        website.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.website))));
        });
        whatsapp.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.whatsapp))));
        });

        email.setOnClickListener(v -> {
            String emailAddress = getResources().getString(R.string.email);
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + emailAddress));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject of the email");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body of the email");
            if (emailIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                startActivity(emailIntent);
            }
        });

        phone.setOnClickListener(v -> {
            String phoneNumber = getResources().getString(R.string.phone);
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + Uri.encode(phoneNumber)));
            if (dialIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                startActivity(dialIntent);
            }
        });

        exit.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setMessage("Are you sure you want to exit the app?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        requireActivity().finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        return view;
    }
}