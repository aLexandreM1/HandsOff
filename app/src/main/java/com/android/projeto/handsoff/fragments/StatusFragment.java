package com.android.projeto.handsoff.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.projeto.handsoff.R;
import com.google.firebase.auth.FirebaseAuth;


public class StatusFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        TextView emailUser = view.findViewById(R.id.emailUser);
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        emailUser.setText(userEmail);


        /*StatusAdapter statusAdapter = new StatusAdapter(usuarioList);
        recyclerView.findViewById(R.id.recyclerViewStatus);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(statusAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));*/

        //NO RECYCLER VIEW QUANDO CLICAR EM UM ITEM VAI ALTERAR O STATUS PARA O ITEM QUE O USUÁRIO CLICOU

        return view;
    }
}
