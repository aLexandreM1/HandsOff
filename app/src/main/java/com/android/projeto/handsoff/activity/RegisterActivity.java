package com.android.projeto.handsoff.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.android.projeto.handsoff.DAO.UsuarioDAO;
import com.android.projeto.handsoff.R;
import com.android.projeto.handsoff.domain.Status;
import com.android.projeto.handsoff.domain.Usuario;
import com.android.projeto.handsoff.util.MaskEditUtil;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtSenha, edtTelefone, edtCelular, edtEstado;
    private static final String TAG = "LOG REGISTER ACTIVITY: ";
    private List<Status> statusList = new ArrayList<>();
    private Usuario usuario = new Usuario();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    boolean flag = false;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Views
        edtName = findViewById(R.id.edtName);
        edtSenha = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefone = findViewById(R.id.edtPhone);
        edtTelefone.addTextChangedListener(MaskEditUtil.mask(edtTelefone, MaskEditUtil.FORMAT_PHONE));
        edtCelular = findViewById(R.id.edtCellPhone);
        edtCelular.addTextChangedListener(MaskEditUtil.mask(edtCelular, MaskEditUtil.FORMAT_CELLPHONE));
        edtEstado = findViewById(R.id.edtState);

        //Autocomplete de estados
        AutoCompleteTextView completeTextView = (AutoCompleteTextView) findViewById(R.id.edtState);
        String[] states = getResources().getStringArray(R.array.states_array);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, states);
        completeTextView.setAdapter(arrayAdapter);
    }

    //Limpar o formulário
    public void clearForm(View view) {
        edtName.getText().clear();
        edtSenha.getText().clear();
        edtEmail.getText().clear();
        edtTelefone.getText().clear();
        edtCelular.getText().clear();
        edtEstado.getText().clear();

        edtName.requestFocus();
    }

    //Validação exclusiva para e-mail
    private boolean isEmail(EditText text) {
        CharSequence chrEmail = text.getText().toString();
        return (!TextUtils.isEmpty(chrEmail) && Patterns.EMAIL_ADDRESS.matcher(chrEmail).matches());
    }

    //Demais validações
    private void validateForm() {

        flag = false;

        if (edtName.getText().toString().isEmpty()) {
            edtName.setError("O campo nome não pode ficar em branco.");
            flag = true;
        }
        if (edtSenha.getText().toString().isEmpty()) {
            edtSenha.setError("O campo senha não pode ficar em branco.");
            flag = true;
        }
        if (!isEmail(edtEmail)) {
            edtEmail.setError("Insira um e-mail válido. (XXX@XXXX.com");
            flag = true;
        }
        if (edtTelefone.getText().toString().isEmpty()) {
            edtTelefone.setError("O campo telefone não pode ficar em branco.");
            flag = true;
        }
        if (edtCelular.getText().toString().isEmpty()) {
            edtCelular.setError("O campo celular não pode ficar em branco.");
            flag = true;
        }
        if (edtEstado.getText().toString().isEmpty()) {
            edtEstado.setError("O campo estados não pode ficar em branco");
            flag = true;
        }
    }

    //Cadastrar novo usuário
    public void saveUser(View view) {
        validateForm();
        if (!flag) {
            usuario.setName(edtName.getText().toString());
            usuario.setSenha(edtSenha.getText().toString());
            usuario.setEmail(edtEmail.getText().toString());
            usuario.setTelefone(edtTelefone.getText().toString());
            usuario.setCelular(edtCelular.getText().toString());
            usuario.setEstado(edtEstado.getText().toString());
            usuario.setStatus(statusList);

            //Chamando método para criar um novo usuário
            usuarioDAO.onCreateUser(usuario, this);

            clearForm(view);
            edtName.requestFocus();
        }
    }
}
