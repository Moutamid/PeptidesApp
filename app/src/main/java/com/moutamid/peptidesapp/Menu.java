package com.moutamid.peptidesapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.peptidesapp.activities.AboutActivity;
import com.moutamid.peptidesapp.activities.FavoriteActivity;
import com.moutamid.peptidesapp.databinding.MenuBinding;

public class Menu {
    MenuBinding binding;
    Activity activity;

    public Menu(Activity activity) {
        this.activity = activity;
    }

    public void showPopup(View view) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = MenuBinding.inflate(inflater, null, false);
//        View customView = inflater.inflate(R.layout.menu, null);
        PopupWindow popupWindow = new PopupWindow(binding.getRoot(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAsDropDown(view);

        binding.about.setOnClickListener(v -> {
            popupWindow.dismiss();
            activity.startActivity(new Intent(activity, AboutActivity.class));
        });

        binding.eula.setOnClickListener(v -> {
            popupWindow.dismiss();
            showEula();
        });

        binding.close.setOnClickListener(v -> {
            popupWindow.dismiss();
            new MaterialAlertDialogBuilder(activity)
                    .setMessage("Are you sure you want to exit the app?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        activity.finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

        binding.favorite.setOnClickListener(v -> {
            popupWindow.dismiss();
            activity.startActivity(new Intent(activity, FavoriteActivity.class));
        });

        binding.rate.setOnClickListener(v -> {
            popupWindow.dismiss();
            String packageName = activity.getPackageName();
            Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            try {
                activity.startActivity(rateAppIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity, "Google Play Store not found!", Toast.LENGTH_SHORT).show();
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
                activity.startActivity(webIntent);
            }
        });

    }

    private void showEula() {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.eula);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();

        MaterialButton close = dialog.findViewById(R.id.close);
        close.setOnClickListener(v -> dialog.dismiss());
    }
}
