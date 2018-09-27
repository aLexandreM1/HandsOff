package com.android.projeto.handsoff.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.android.projeto.handsoff.DAO.UsuarioDAO;
import com.android.projeto.handsoff.R;
import com.android.projeto.handsoff.domain.Usuario;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmailLogin, edtPasswordLogin;
    private Usuario usuario = new Usuario();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Views
        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent toMainCurrentUser = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(toMainCurrentUser);
            finish();
        }

    }

    //Validação exclusiva para e-mail
    private boolean isEmail(EditText text) {
        CharSequence chrEmail = text.getText().toString();
        return (!TextUtils.isEmpty(chrEmail) && Patterns.EMAIL_ADDRESS.matcher(chrEmail).matches());
    }

    //Demais validações
    private void validateLoginForm() {
        flag = false;

        if (!isEmail(edtEmailLogin) && edtEmailLogin.getText().toString().isEmpty()) {
            edtEmailLogin.setError("E-mail inválido! Preencha corretamente seu e-mail.");
            flag = true;
        }

        if (edtPasswordLogin.getText().toString().isEmpty()) {
            edtPasswordLogin.setError("A senha não pode ficar em branco.");
            flag = true;
        }

    }

    //Método de login
    public void signInUser(View view) {
        validateLoginForm();
        if (!flag) {
            usuarioDAO.onSignInUser(
                    edtEmailLogin.getText().toString(),
                    edtPasswordLogin.getText().toString(),
                    this);

            edtEmailLogin.getText().clear();
            edtPasswordLogin.getText().clear();
            edtEmailLogin.requestFocus();
        }
    }

    //Método que manda o usuário para tela de cadastro
    public void registerNewUser(View view) {
        Intent toRegister = new Intent(this, RegisterActivity.class);
        startActivity(toRegister);
    }

}
