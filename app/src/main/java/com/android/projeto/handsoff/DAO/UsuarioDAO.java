package com.android.projeto.handsoff.DAO;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.projeto.handsoff.R;
import com.android.projeto.handsoff.activity.LoginActivity;
import com.android.projeto.handsoff.activity.MainActivity;
import com.android.projeto.handsoff.adapter.StatusAdapter;
import com.android.projeto.handsoff.domain.Status;
import com.android.projeto.handsoff.domain.Usuario;
import com.android.projeto.handsoff.util.BaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private DatabaseReference reference;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private List<Status> statusList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BaseUtils baseUtils = new BaseUtils();
    private String TAG = "LOG USUARIO DAO: ";

    public void onCreateUser(final Usuario usuario, final Activity activity) {

        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            try {

                                //Default status
                                int statusId = new Status().getId();
                                statusId++;
                                statusList.add(new Status(statusId, "Driving", "Estou dirigindo no momento!"));
                                statusList.add(new Status(statusId, "Amante", "Estou com a boca ocupada, depois eu retorno"));
                                statusList.add(new Status(statusId, "Fazendo trabalho", "No momento estou fazendo um fucking trabalho da faculdade, ligo já já"));
                                statusList.add(new Status(statusId, "Vendo apresentação", "Ligue mais tarde por favor."));
                                statusList.add(new Status(statusId, "Comendo na Ragazzo", "Liga depois pq agora to comendo coxinha com a rapaziada"));
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
                    }
                });

        //Manter os dados sincronizados com o Firebase Realtime.
        reference = FirebaseDatabase.getInstance().getReference("usuarios");
        reference.keepSynced(true);
    }

    public void onSignInUser(String email, String senha, final Activity activity) {

        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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
                    }
                });

    }

    //Create status - se precisar.
    public void onCreateStatus(final Activity activity) {
       /* reference.push().setValue(statuses).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        //reference = FirebaseDatabase.getInstance().getReference("status");
        reference.keepSynced(true);*/
    }

    public void onSignOut(Activity activity) {
        FirebaseAuth.getInstance().signOut();
        baseUtils.showProgressDialog(activity);
        Intent backToLogin = new Intent(activity, LoginActivity.class);
        activity.startActivity(backToLogin);
        activity.finish();
    }

    public void addStatusList(final Activity activity) {

        try {
            reference = FirebaseDatabase.getInstance().getReference()
                    .child("usuarios")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                    .child("status");

            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Status status = dataSnapshot.getValue(Status.class);
                    StatusAdapter statusAdapter = new StatusAdapter(statusList);

                    statusList.add(status);

                    recyclerView = activity.findViewById(R.id.recyclerViewStatus);
                    recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    recyclerView.setAdapter(statusAdapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(activity, "Erro no ChildEventListner.", Toast.LENGTH_LONG).show();
                    Log.v(TAG, "ERRO: " + databaseError);
                }
            });
        } catch (DatabaseException exception) {
            exception.getMessage();
        }
    }
}
