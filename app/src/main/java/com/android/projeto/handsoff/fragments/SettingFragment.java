package com.android.projeto.handsoff.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.projeto.handsoff.R;


public class SettingFragment extends Fragment {

    private Button btnSalvar;
    private Button btnCancelar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        //TODO:NO RECYCLERVIEW QUANDO CLICA EM UM STATUS O USUÁRIO PODE ALTERAR OU DELETAR..

        view.findViewById(R.id.btnAddStatus).setOnClickListener(this::setBtnAddStatus);

        return view;
    }

    public void setBtnAddStatus(View view) {
        Button btnAddStatus = view.findViewById(R.id.btnAddStatus);

        btnAddStatus.setOnClickListener(addNewStatus -> {
            LayoutInflater inflater = LayoutInflater.from(addNewStatus.getContext());
            final View v = inflater.inflate(R.layout.alert_dialog_new_status, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(addNewStatus.getContext());
            builder.setView(v);

            btnSalvar = v.findViewById(R.id.saveNewUser);
            btnCancelar = v.findViewById(R.id.cancelNewStatus);

            builder.setCancelable(true);
            final AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setGravity(Gravity.CENTER);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            btnSalvar.setOnClickListener(btnSaveNewStatus -> {
                //usuarioDAO.onCreateStatus(status, getActivity());
            });

            btnCancelar.setOnClickListener(btnCancel -> {
                alertDialog.cancel();
                Toast.makeText(btnCancel.getContext(), "Cancelado!", Toast.LENGTH_SHORT).show();
            });
        });
    }
}
