package com.android.projeto.handsoff.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.projeto.handsoff.DAO.UsuarioDAO;
import com.android.projeto.handsoff.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    //Permissões
    String[] PERMISSIONS = {Manifest.permission.MODIFY_PHONE_STATE, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};

    private EditText edtEmailLogin, edtPasswordLogin;
    private ImageView imageLogo;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private boolean flag;
    private List<String> neededPermissions = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Views
        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        imageLogo = findViewById(R.id.imageLogo);

        //OnClick no logo
        imageLogo.setOnLongClickListener(view -> {
            Intent toEasteregg = new Intent(this, EastereggActivity.class);
            startActivity(toEasteregg);
            return true;
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent toMainCurrentUser = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(toMainCurrentUser);
            finish();
        }

        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }
        //Requerindo permissão
        requestPermissions(neededPermissions.toArray(new String[neededPermissions.size()]), 1);
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
