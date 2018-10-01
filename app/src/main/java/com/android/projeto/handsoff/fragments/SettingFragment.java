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
import android.widget.EditText;
import android.widget.Toast;

import com.android.projeto.handsoff.DAO.UsuarioDAO;
import com.android.projeto.handsoff.R;
import com.android.projeto.handsoff.domain.Usuario;


public class SettingFragment extends Fragment {

    private Button btnAtualizar, btnDeletar;
    private EditText edtStatus;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        view.findViewById(R.id.btnStatusSettingFragment).setOnClickListener(this::setBtnStatus);
        edtStatus = view.findViewById(R.id.edtStatus);

        return view;
    }

    public void setBtnStatus(View view) {
        Button btnAddStatus = view.findViewById(R.id.btnStatusSettingFragment);

        btnAddStatus.setOnClickListener(addNewStatus -> {
            LayoutInflater inflater = LayoutInflater.from(addNewStatus.getContext());
            final View v = inflater.inflate(R.layout.alert_dialog_update_delete_status, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(addNewStatus.getContext());
            builder.setView(v);

            btnAtualizar = v.findViewById(R.id.updateStatus);
            btnDeletar = v.findViewById(R.id.deleteStatus);

            builder.setCancelable(true);
            final AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setGravity(Gravity.CENTER);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            Usuario usuario = new Usuario();

            edtStatus.setText(usuario.getStatus());

            //usuarioDAO.getUserStatus(edtStatus);

            btnAtualizar.setOnClickListener(btnSaveNewStatus -> {
                //Metodo para atualizar o status
            });

            btnDeletar.setOnClickListener(btnCancel -> {
                //Metodo para deletar o status
                Toast.makeText(btnCancel.getContext(), "Deletado com sucesso!", Toast.LENGTH_SHORT).show();
            });
        });
    }
}
