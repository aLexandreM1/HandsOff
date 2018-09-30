package com.android.projeto.handsoff.util;

import android.app.Activity;
import android.app.ProgressDialog;

public class BaseUtils {

    public void showProgressDialog(Activity activity) {
        ProgressDialog dialog;
        dialog = new ProgressDialog(activity);
        dialog = ProgressDialog.show(activity, "Carregando...", "Carregando, por favor aguarde...", false, false);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    //VERIFICAR
    /*public void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)  activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }*/
}
