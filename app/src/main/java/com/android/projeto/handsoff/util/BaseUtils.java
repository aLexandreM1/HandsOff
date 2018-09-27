package com.android.projeto.handsoff.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.android.projeto.handsoff.R;

public class BaseUtils {

    private ProgressBar progressBar;

    public void showProgressBar(Activity activity) {
        progressBar = activity.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(Activity activity) {
        progressBar.setVisibility(View.GONE);
    }

    //VERIFICAR AMANHÃ
    /*public void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)  activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }*/
}
