package com.android.projeto.handsoff.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.projeto.handsoff.DAO.UsuarioDAO;
import com.android.projeto.handsoff.R;
import com.android.projeto.handsoff.adapter.StatusAdapter;
import com.android.projeto.handsoff.domain.Status;
import com.android.projeto.handsoff.domain.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class StatusFragment extends Fragment {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        TextView emailUser = view.findViewById(R.id.emailUser);
        String userName = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        emailUser.setText(userName);


        //NO RECYCLER VIEW QUANDO CLICAR EM UM ITEM VAI ALTERAR O STATUS PARA O ITEM QUE O USUÁRIO CLICOU

        return view;
    }

    /*@Override
    public void onStart() {
        super.onStart();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        usuarioDAO.addStatusList(getActivity());
                    }
                }
        ).start();
    }*/
}
