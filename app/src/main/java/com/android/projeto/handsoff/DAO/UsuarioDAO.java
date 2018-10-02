package com.android.projeto.handsoff.DAO;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.projeto.handsoff.activity.LoginActivity;
import com.android.projeto.handsoff.activity.MainActivity;
import com.android.projeto.handsoff.domain.Usuario;
import com.android.projeto.handsoff.fragments.StatusFragment;
import com.android.projeto.handsoff.util.BaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsuarioDAO {

    private DatabaseReference reference;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private BaseUtils baseUtils = new BaseUtils();
    private String userId;
    private Usuario usuario = new Usuario();
    private String TAG = "LOG USUARIO DAO: ";

    public void onCreateUser(final Usuario usuario, final Activity activity) {

        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener
                (task -> {
                    if (task.isSuccessful()) {

                        try {
                            //Referencia um filho no database.
                            reference = FirebaseDatabase.getInstance().getReference().child("usuarios");

                            usuario.setUserId(auth.getUid());

                            //Insere no firebase (o push() cria uma chave única, um Id para o registro).
                            reference.push().setValue(usuario).addOnSuccessListener(aVoid ->
                                    Log.v(TAG, "Usuário cadastrado com sucesso"));

                        } catch (Exception e) {
                            Log.v(TAG, "ERRO NO DATABASE: " + e);
                        }

                        baseUtils.showProgressDialog(activity);
                        Toast.makeText(activity, "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();
                        Intent toLogin = new Intent(activity, LoginActivity.class);
                        activity.startActivity(toLogin);
                        activity.finish();
                    } else {

                        String erroExecucao = "";

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            erroExecucao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres e que contenha letras e números";
                            Log.v(TAG, e.toString());
                        } catch (FirebaseAuthUserCollisionException e) {
                            erroExecucao = "Esse e-mail já está cadastrado!";
                            Log.v(TAG, e.toString());
                        } catch (Exception e) {
                            erroExecucao = "Erro ao caddastrar um novo usuário";
                            Log.v(TAG, e.toString());
                        }

                        Toast.makeText(activity, erroExecucao, Toast.LENGTH_LONG).show();
                    }
                });

        //Manter os dados sincronizados com o Firebase Realtime.
        reference = FirebaseDatabase.getInstance().getReference("usuarios");
        reference.keepSynced(true);
    }

    public void onSignInUser(String email, String senha, final Activity activity) {

        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        baseUtils.showProgressDialog(activity);
                        Log.i(TAG, "Usuário logado com sucesso");
                        Intent toMain = new Intent(activity, MainActivity.class);
                        activity.startActivity(toMain);
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "Falha no login, tente novamente.", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Falha no login");
                    }
                });
    }

    public void onSignOut(Activity activity) {
        FirebaseAuth.getInstance().signOut();
        baseUtils.showProgressDialog(activity);
        Intent backToLogin = new Intent(activity, LoginActivity.class);
        activity.startActivity(backToLogin);
        activity.finish();
    }

    public void getUserStatus(EditText text){
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("usuarios");
        firebaseUser = auth.getCurrentUser();
        userId = firebaseUser.getUid();
        //reference.
    }

    /*public void getUserStatus(EditText text){
         firebaseDatabase = FirebaseDatabase.getInstance();
         reference = firebaseDatabase.getReference("usuarios");
         firebaseUser = auth.getCurrentUser();
         String userId = firebaseUser != null ? firebaseUser.getUid() : null;

         Log.d(TAG, userId);

        reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.child(userId).getValue(Usuario.class);
                Log.d("*********************", usuario.getName());
                //                String statusUser = usuario.getStatus();
//
//                text.setText(statusUser);



             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
    }*/
}
