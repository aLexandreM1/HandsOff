package com.android.projeto.handsoff.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.projeto.handsoff.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
