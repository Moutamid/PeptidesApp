package com.moutamid.peptidesapp;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;

public class Easter {
    Activity activity;

    public Easter(Activity activity) {
        this.activity = activity;
    }

    public void showEaster(boolean alreadyShown, int i) {
        String stash = "EASTER_" + i;
        Stash.put(stash, true);

        String message;
        int count = Stash.getInt(Constants.EASTER_COUNT, 0);
        if (alreadyShown){
            message = "You have already unlocked this Easter egg.";
        } else {
            count += 1;
            Stash.put(Constants.EASTER_COUNT, count);
            message = activity.getResources().getString(R.string.easter).replace(Constants.EASTER_COUNT, String.valueOf(count));
        }

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.easter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();

        TextView messageView = dialog.findViewById(R.id.message);
        TextView codeView = dialog.findViewById(R.id.code);
        String text = "Your secret code is";

        String code = text + " " + Stash.getString(Constants.PROMO).split(", ")[i-1];

        SpannableString codeString = new SpannableString(code);
        int startIndex = Math.min(text.length(), code.length());
        int endIndex = code.length();
        codeString.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.green)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        codeView.setText(codeString);
        messageView.setText(message);

        MaterialButton close = dialog.findViewById(R.id.close);
        close.setOnClickListener(v -> dialog.dismiss());
    }

}
