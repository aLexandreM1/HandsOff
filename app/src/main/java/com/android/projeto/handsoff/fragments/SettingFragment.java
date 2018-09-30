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

import com.android.projeto.handsoff.DAO.UsuarioDAO;
import com.android.projeto.handsoff.R;
import com.android.projeto.handsoff.domain.Status;


public class SettingFragment extends Fragment {

    //private UsuarioDAO usuarioDAO = new UsuarioDAO();
    //private Status status = new Status();
    private Button btnAddStatus, btnSalvar, btnCancelar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);

        //NO RECYCLERVIEW QUANDO CLICA EM UM STATUS O USUÁRIO PODE ALTERAR OU DELETAR...
        //PRA ISSO TEM QUE TER UM DIALOG COM 3 EDITTEXT E 2 BUTTONS, PARA ATUALIZAR E DELETAR

    }

    public void setBtnAddStatus(View view) {
        btnAddStatus = view.findViewById(R.id.btnAddStatus);

        btnAddStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                final View v = inflater.inflate(R.layout.alert_dialog_new_status, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(v);

                btnSalvar = v.findViewById(R.id.saveNewUser);
                btnCancelar = v.findViewById(R.id.cancelNewStatus);

                builder.setCancelable(true);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setGravity(Gravity.CENTER);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                btnSalvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //PEDIR AJUDA PRO DIEGO AMANHÃ
                        //usuarioDAO.onCreateStatus(status, getActivity());
                    }
                });

                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                        Toast.makeText(view.getContext(), "Cancelado!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
