package com.android.projeto.handsoff.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.projeto.handsoff.R;


public class SettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        //NO RECYCLERVIEW QUANDO CLICA EM UM STATUS O USUÁRIO PODE ALTERAR OU DELETAR...
        //PRA ISSO TEM QUE TER UM DIALOG COM 3 EDITTEXT E 2 BUTTONS, PARA ATUALIZAR E DELETAR
        return view;
    }

    public void addNewStatus(View view){
        //ABRIR DIALOG COM 3 EDITTEXT E 2 BUTTONS, PARA SALVAR E CANCELAR.
    }
}
