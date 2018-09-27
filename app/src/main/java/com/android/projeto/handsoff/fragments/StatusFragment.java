package com.android.projeto.handsoff.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.projeto.handsoff.R;


public class StatusFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        return view;

        //NO RECYCLER VIEW QUANDO CLICAR EM UM ITEM VAI ALTERAR O STATUS PARA O ITEM QUE O USUÁRIO CLICOU
        //GET NO NOME DO USÁRIO E DAR UM SETTEXT NO ID: nameUser
    }
}
