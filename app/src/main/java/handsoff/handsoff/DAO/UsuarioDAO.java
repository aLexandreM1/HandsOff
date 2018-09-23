package handsoff.handsoff.DAO;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import handsoff.handsoff.activity.LoginActivity;
import handsoff.handsoff.activity.MainActivity;
import handsoff.handsoff.domain.Usuario;

public class UsuarioDAO {

    private DatabaseReference reference;
    private FirebaseAuth auth;
    private String TAG = "LOG USUARIO DAO: ";

    public void onCreateUser(Usuario usuario, final Activity activity) {

        auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();
                            Intent toLogin = new Intent(activity, LoginActivity.class);
                            activity.startActivity(toLogin);
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

        try {
            //Referencia um filho no database.
            reference = FirebaseDatabase.getInstance().getReference().child("usuarios");

            //Insere no firebase (o push() cria uma chave única, um Id para o registro).
            reference.push().setValue(usuario);

        } catch (Exception e) {
            Log.v(TAG, "ERRO NO DATABASE: " + e);
        }

        //Manter os dados sincronizados com o Firebase Realtime.
        reference = FirebaseDatabase.getInstance().getReference("usuarios");
        reference.keepSynced(true);
    }

    public void onSignInUser(String email,String senha, final Activity activity) {

        auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Usuário logado com sucesso");
                            Intent toMain = new Intent(activity, MainActivity.class);
                            activity.startActivity(toMain);
                        } else {
                            Toast.makeText(activity, "Falha no login, tente novamente.", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "Falha no login");
                        }
                    }
                });

    }

}
