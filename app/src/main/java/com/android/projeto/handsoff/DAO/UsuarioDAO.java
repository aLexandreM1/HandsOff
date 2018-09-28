package com.android.projeto.handsoff.DAO;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.projeto.handsoff.activity.LoginActivity;
import com.android.projeto.handsoff.activity.MainActivity;
import com.android.projeto.handsoff.domain.Status;
import com.android.projeto.handsoff.domain.Usuario;
import com.android.projeto.handsoff.util.BaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsuarioDAO {

    private DatabaseReference reference;
    private FirebaseAuth auth;
    //private BaseUtils baseUtils = new BaseUtils();
    private String TAG = "LOG USUARIO DAO: ";

    public void onCreateUser(final Usuario usuario, final Activity activity) {

        auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            try {
                                //Referencia um filho no database.
                                reference = FirebaseDatabase.getInstance().getReference().child("usuarios");

                                //Insere no firebase (o push() cria uma chave única, um Id para o registro).
                                reference.push().setValue(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.v(TAG, "Usuário cadastrado com sucesso");
                                    }
                                });

                            } catch (Exception e) {
                                Log.v(TAG, "ERRO NO DATABASE: " + e);
                            }

                            //baseUtils.showProgressBar(activity);
                            Toast.makeText(activity, "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();
                            Intent toLogin = new Intent(activity, LoginActivity.class);
                            activity.startActivity(toLogin);
                            //baseUtils.hideProgressBar(activity);
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
                    }
                });

        //Manter os dados sincronizados com o Firebase Realtime.
        reference = FirebaseDatabase.getInstance().getReference("usuarios");
        reference.keepSynced(true);
    }

    public void onSignInUser(String email, String senha, final Activity activity) {

        auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //baseUtils.showProgressBar(activity);
                            Log.i(TAG, "Usuário logado com sucesso");
                            Intent toMain = new Intent(activity, MainActivity.class);
                            activity.startActivity(toMain);
                            //baseUtils.hideProgressBar(activity);
                            activity.finish();
                        } else {
                            Toast.makeText(activity, "Falha no login, tente novamente.", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "Falha no login");
                        }
                    }
                });

    }

    //Create status - se precisar.
    /*    public void onCreateStatus(Status status, final Activity activity) {

        reference = FirebaseDatabase.getInstance().getReference().child("status");
        reference.push().setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(activity, "Status criado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Falha ao criar um novo status!", Toast.LENGTH_SHORT).show();
                Log.v(TAG, e.getMessage());
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("status");
        reference.keepSynced(true);
    }*/

    public void onSignOut(Activity activity) {
        FirebaseAuth.getInstance().signOut();
        //baseUtils.showProgressBar(activity);
        Intent backToLogin = new Intent(activity, LoginActivity.class);
        activity.startActivity(backToLogin);
        //baseUtils.hideProgressBar(activity);
        activity.finish();
    }

}
